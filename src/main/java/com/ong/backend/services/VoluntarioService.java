package com.ong.backend.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.ong.backend.dto.MensagemResponse;
import com.ong.backend.dto.VoluntarioDTO;
import com.ong.backend.entities.StatusVoluntario;
import com.ong.backend.entities.Usuario;
import com.ong.backend.entities.Voluntario;
import com.ong.backend.exceptions.NaoEncontradoException;
import com.ong.backend.repositories.UsuarioRepository;
import com.ong.backend.repositories.VoluntarioRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.SecureRandom;
import com.ong.backend.utils.CPFValidator;

@Service
public class VoluntarioService {

    @Autowired
    VoluntarioRepository voluntarioRepository;

    @Autowired
    UsuarioRepository usuarioRepository;
    
    @Autowired
    EmailService emailService;
    
    @Autowired
    com.ong.backend.repositories.InscricaoRepository inscricaoRepository;
    
    @Autowired
    com.ong.backend.repositories.ParticipacaoEventoRepository participacaoEventoRepository;
    
    @Autowired
    com.ong.backend.repositories.CursoRepository cursoRepository;

    public ResponseEntity<Voluntario> tornarVoluntario(VoluntarioDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new NaoEncontradoException("Usuário não encontrado"));

        // Validar CPF
        if (!CPFValidator.isValid(dto.getCpf())) {
            throw new IllegalArgumentException("CPF inválido. Verifique o número digitado.");
        }
        
        // Verificar se já existe outro voluntário com o mesmo CPF
        String cpfLimpo = dto.getCpf().replaceAll("[^0-9]", "");
        Optional<Voluntario> voluntarioComMesmoCPF = voluntarioRepository.findByCpf(cpfLimpo);
        if (voluntarioComMesmoCPF.isPresent()) {
            Voluntario volExistente = voluntarioComMesmoCPF.get();
            // Se for outro usuário (não o mesmo tentando reativar)
            if (!volExistente.getIdUsuario().getId().equals(dto.getIdUsuario())) {
                throw new IllegalArgumentException("Este CPF já está cadastrado para outro voluntário.");
            }
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate nascimento = LocalDate.parse(dto.getDataNascimento(), formatter);

        int idade = Period.between(nascimento, LocalDate.now()).getYears();
        
        System.out.println("📅 DEBUG - Data nascimento: " + dto.getDataNascimento());
        System.out.println("📅 DEBUG - Data hoje: " + LocalDate.now());
        System.out.println("📅 DEBUG - Idade calculada: " + idade);
        
        if (idade < 18) {
            throw new IllegalArgumentException("Usuário precisa ter 18 anos ou mais para se tornar voluntário. Idade atual: " + idade);
        }

        // Verificar se já existe um registro de voluntário para este usuário
        Optional<Voluntario> voluntarioExistente = voluntarioRepository.findByIdUsuarioId(dto.getIdUsuario());
        
        Voluntario voluntario;
        if (voluntarioExistente.isPresent()) {
            voluntario = voluntarioExistente.get();
            
            // Se já está PENDENTE ou APROVADO, não permitir novo pedido
            if (voluntario.getStatus() == StatusVoluntario.PENDENTE) {
                throw new IllegalArgumentException("Você já possui um pedido para se tornar voluntário em análise. Aguarde a aprovação.");
            }
            
            if (voluntario.getStatus() == StatusVoluntario.APROVADO) {
                throw new IllegalArgumentException("Você já é um voluntário aprovado.");
            }
            
            // Se foi CANCELADO, permitir reativar
            voluntario.setCpf(dto.getCpf());
            voluntario.setDataVoluntario(LocalDateTime.now());
            voluntario.setDataNascimento(dto.getDataNascimento());
            voluntario.setTelefone(dto.getTelefone());
            voluntario.setDescricao(dto.getDescricao());
            voluntario.setEndereco(dto.getEndereco());
            voluntario.setStatus(StatusVoluntario.PENDENTE);
            voluntario.setDataCancelamento(null); // Limpar data de cancelamento
            voluntario.setCodigoCancelamento(null); // Limpar código de cancelamento
            voluntario.setCodigoCancelamentoExpiracao(null); // Limpar expiração
        } else {
            // Criar novo voluntário
            voluntario = new Voluntario();
            voluntario.setCpf(dto.getCpf());
            voluntario.setDataVoluntario(LocalDateTime.now());
            voluntario.setIdUsuario(usuario);
            voluntario.setDataNascimento(dto.getDataNascimento());
            voluntario.setTelefone(dto.getTelefone());
            voluntario.setDescricao(dto.getDescricao());
            voluntario.setEndereco(dto.getEndereco());
            voluntario.setStatus(StatusVoluntario.PENDENTE);
        }

        voluntario = voluntarioRepository.save(voluntario);
        
        // Enviar email de pedido recebido
        try {
            emailService.enviarEmailPedidoVoluntario(usuario.getEmail(), usuario.getNome());
        } catch (Exception e) {
            System.err.println("Erro ao enviar email de pedido: " + e.getMessage());
        }

        return ResponseEntity.ok(voluntario);
    }

    public List<Voluntario> listar() {
        return voluntarioRepository.findAll();
    }

    public ResponseEntity<Voluntario> buscarPorId(Long id) {
        Optional<Voluntario> existe = voluntarioRepository.findById(id);
        return existe.map(ResponseEntity::ok)
                .orElseThrow(() -> new NaoEncontradoException("Voluntário não encontrado com o Id " + id));
    }

    public ResponseEntity<?> cancelar(Long id) {
        Voluntario voluntario = voluntarioRepository.findById(id)
                .orElseThrow(() -> new NaoEncontradoException("Voluntário não encontrado com o Id " + id));

        voluntario.setStatus(StatusVoluntario.CANCELADO);
        voluntario.setDataCancelamento(LocalDateTime.now());
        voluntarioRepository.save(voluntario);
        
        // Cancelar todas as inscrições em atividades do voluntário
        Usuario usuario = voluntario.getIdUsuario();
        if (usuario != null) {
            cancelarInscricoesDoUsuario(usuario.getId());
        }

        return ResponseEntity.ok(voluntario);
    }
    
    private void cancelarInscricoesDoUsuario(Long idUsuario) {
        try {
            // Cancelar inscrições em atividades e liberar vagas
            List<com.ong.backend.entities.Inscricao> inscricoes = inscricaoRepository.findByIdUsuarioId(idUsuario);
            if (!inscricoes.isEmpty()) {
                for (com.ong.backend.entities.Inscricao inscricao : inscricoes) {
                    // Liberar vaga na atividade
                    com.ong.backend.entities.Curso curso = inscricao.getIdCurso();
                    if (curso != null) {
                        curso.setVagas(curso.getVagas() + 1);
                        cursoRepository.save(curso);
                        System.out.println("✅ Vaga liberada na atividade: " + curso.getTitulo());
                    }
                }
                inscricaoRepository.deleteAll(inscricoes);
                System.out.println("✅ Canceladas " + inscricoes.size() + " inscrições em atividades do usuário " + idUsuario);
            }
            
            // Cancelar participações em eventos
            List<com.ong.backend.entities.ParticipacaoEvento> participacoes = participacaoEventoRepository.findByUsuarioId(idUsuario);
            if (!participacoes.isEmpty()) {
                participacaoEventoRepository.deleteAll(participacoes);
                System.out.println("✅ Canceladas " + participacoes.size() + " participações em eventos do usuário " + idUsuario);
            }
        } catch (Exception e) {
            System.err.println("❌ Erro ao cancelar inscrições do usuário " + idUsuario + ": " + e.getMessage());
        }
    }

    public List<Voluntario> listarPendentes() {
        return voluntarioRepository.findByStatus(StatusVoluntario.PENDENTE);
    }

    public List<Voluntario> listarAprovados() {
        return voluntarioRepository.findByStatus(StatusVoluntario.APROVADO);
    }
    
    public ResponseEntity<?> buscarPorUsuario(Long idUsuario) {
        Optional<Voluntario> voluntario = voluntarioRepository.findByIdUsuarioId(idUsuario);

        if (voluntario.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(voluntario.get());
    }
    
    public ResponseEntity<?> editarPerfil(Long id, VoluntarioDTO atualizado){
    	Voluntario voluntario = voluntarioRepository.findById(id)
                .orElseThrow(() -> new NaoEncontradoException("Voluntário não encontrado"));
    	
    	voluntario.setCpf(atualizado.getCpf());
    	voluntario.setDataNascimento(atualizado.getDataNascimento());
    	voluntario.setEndereco(atualizado.getEndereco());
    	voluntario.setTelefone(atualizado.getTelefone());
    	
    	voluntario = voluntarioRepository.save(voluntario);
    	
    	return ResponseEntity.ok("Atualização salva!");
    }

    public ResponseEntity<MensagemResponse> aprovar(Long id) {
        Voluntario voluntario = voluntarioRepository.findById(id)
                .orElseThrow(() -> new NaoEncontradoException("Voluntário não encontrado"));

        voluntario.setStatus(StatusVoluntario.APROVADO);
        voluntarioRepository.save(voluntario);
        
        // Enviar email de aprovação
        try {
            Usuario usuario = voluntario.getIdUsuario();
            emailService.enviarEmailVoluntarioAprovado(usuario.getEmail(), usuario.getNome());
        } catch (Exception e) {
            System.err.println("Erro ao enviar email de aprovação: " + e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(new MensagemResponse("Voluntário aprovado com sucesso!"));
    }

    @Scheduled(fixedRate = 300000)
    public void deletarVoluntariosCancelados() {
        LocalDateTime agora = LocalDateTime.now();
        List<Voluntario> paraExcluir = voluntarioRepository
                .findByStatusAndDataCancelamentoBefore(StatusVoluntario.CANCELADO, agora.minusHours(1));
        if (!paraExcluir.isEmpty()) {
            voluntarioRepository.deleteAll(paraExcluir);
        }
    }
    
    private String gerarCodigoAleatorio() {
        SecureRandom random = new SecureRandom();
        int codigo = 100000 + random.nextInt(900000);
        return String.valueOf(codigo);
    }
    
    private Usuario getUsuarioLogado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof Usuario) {
            return (Usuario) auth.getPrincipal();
        }
        return null;
    }
    
    public ResponseEntity<?> solicitarCancelamento() {
        Usuario usuarioLogado = getUsuarioLogado();
        if (usuarioLogado == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new MensagemResponse("Usuário não autenticado"));
        }
        
        Optional<Voluntario> voluntarioOpt = voluntarioRepository.findByIdUsuarioId(usuarioLogado.getId());
        if (voluntarioOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MensagemResponse("Você não é um voluntário"));
        }
        
        Voluntario voluntario = voluntarioOpt.get();
        
        if (voluntario.getStatus() != StatusVoluntario.APROVADO) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MensagemResponse("Apenas voluntários aprovados podem solicitar cancelamento"));
        }
        
        // Gerar código de 6 dígitos
        String codigo = gerarCodigoAleatorio();
        voluntario.setCodigoCancelamento(codigo);
        voluntario.setCodigoCancelamentoExpiracao(LocalDateTime.now().plusMinutes(15));
        voluntarioRepository.save(voluntario);
        
        // Enviar email com código
        try {
            emailService.enviarEmailCodigoCancelamento(usuarioLogado.getEmail(), usuarioLogado.getNome(), codigo);
        } catch (Exception e) {
            System.err.println("Erro ao enviar email de cancelamento: " + e.getMessage());
        }
        
        return ResponseEntity.ok(new MensagemResponse("Código de cancelamento enviado para seu email"));
    }
    
    public ResponseEntity<?> confirmarCancelamento(String codigo) {
        Usuario usuarioLogado = getUsuarioLogado();
        if (usuarioLogado == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new MensagemResponse("Usuário não autenticado"));
        }
        
        Optional<Voluntario> voluntarioOpt = voluntarioRepository.findByIdUsuarioId(usuarioLogado.getId());
        if (voluntarioOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MensagemResponse("Você não é um voluntário"));
        }
        
        Voluntario voluntario = voluntarioOpt.get();
        
        // Verificar código
        if (voluntario.getCodigoCancelamento() == null || !voluntario.getCodigoCancelamento().equals(codigo)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MensagemResponse("Código inválido"));
        }
        
        // Verificar expiração
        if (voluntario.getCodigoCancelamentoExpiracao() == null || 
            LocalDateTime.now().isAfter(voluntario.getCodigoCancelamentoExpiracao())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MensagemResponse("Código expirado. Solicite um novo código"));
        }
        
        // Cancelar voluntariado
        voluntario.setStatus(StatusVoluntario.CANCELADO);
        voluntario.setDataCancelamento(LocalDateTime.now());
        voluntario.setCodigoCancelamento(null);
        voluntario.setCodigoCancelamentoExpiracao(null);
        voluntarioRepository.save(voluntario);
        
        // Cancelar todas as inscrições do voluntário
        cancelarInscricoesDoUsuario(usuarioLogado.getId());
        
        // Enviar email de confirmação
        try {
            emailService.enviarEmailCancelamentoConfirmado(usuarioLogado.getEmail(), usuarioLogado.getNome());
        } catch (Exception e) {
            System.err.println("Erro ao enviar email de confirmação: " + e.getMessage());
        }
        
        return ResponseEntity.ok(new MensagemResponse("Voluntariado cancelado com sucesso"));
    }
    
    public ResponseEntity<?> removerVoluntarioPorAdmin(Long idVoluntario) {
        Voluntario voluntario = voluntarioRepository.findById(idVoluntario)
                .orElseThrow(() -> new NaoEncontradoException("Voluntário não encontrado"));
        
        Usuario usuario = voluntario.getIdUsuario();
        
        // Cancelar voluntariado
        voluntario.setStatus(StatusVoluntario.CANCELADO);
        voluntario.setDataCancelamento(LocalDateTime.now());
        voluntarioRepository.save(voluntario);
        
        // Cancelar todas as inscrições do voluntário
        cancelarInscricoesDoUsuario(usuario.getId());
        
        // Enviar email notificando o usuário
        try {
            emailService.enviarEmailRemovidoPorAdmin(usuario.getEmail(), usuario.getNome());
        } catch (Exception e) {
            System.err.println("Erro ao enviar email de remoção: " + e.getMessage());
        }
        
        return ResponseEntity.ok(new MensagemResponse("Voluntário removido com sucesso"));
    }
}