package com.ong.backend.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ong.backend.dto.InscricaoDTO;
import com.ong.backend.dto.MensagemResponse;
import com.ong.backend.entities.Curso;
import com.ong.backend.entities.Inscricao;
import com.ong.backend.entities.StatusVoluntario;
import com.ong.backend.entities.Usuario;
import com.ong.backend.exceptions.DuplicadoException;
import com.ong.backend.exceptions.NaoEncontradoException;
import com.ong.backend.repositories.CursoRepository;
import com.ong.backend.repositories.InscricaoRepository;
import com.ong.backend.repositories.UsuarioRepository;
import com.ong.backend.repositories.VoluntarioRepository;

@Service
public class InscricaoService {

    @Autowired
    private InscricaoRepository inscricaoRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private CursoRepository cursoRepository;
    
    @Autowired
    private VoluntarioRepository voluntarioRepository;
    
    @Autowired
    private EmailService emailService;

    public ResponseEntity<Inscricao> inscrever(InscricaoDTO dto){
        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new NaoEncontradoException("Usuário não encontrado"));
        
        boolean isVoluntario = voluntarioRepository
                .existsByIdUsuario_IdAndStatus(usuario.getId(), StatusVoluntario.APROVADO);

        if (!isVoluntario) {
            throw new RuntimeException("Somente voluntários aprovados podem se inscrever em cursos.");
        }

        Curso curso = cursoRepository.findById(dto.getIdCurso())
                .orElseThrow(() -> new NaoEncontradoException("Curso não encontrado"));
        
        boolean inscrito = inscricaoRepository.existsByIdUsuarioAndIdCurso(usuario, curso);
        if (inscrito) {
            throw new DuplicadoException("Você já está inscrito nesta atividade.");
        }
        
        int totalInscricoes = inscricaoRepository.countByIdCurso(curso);
        if (totalInscricoes >= curso.getVagas()) {
            throw new RuntimeException("Não há vagas disponíveis para esta atividade.");
        }
        
        Inscricao inscricao = new Inscricao();
        inscricao.setDataInscricao(LocalDateTime.now());
        inscricao.setIdCurso(curso);
        inscricao.setIdUsuario(usuario);
        inscricao = inscricaoRepository.save(inscricao);
        
        curso.setVagas(curso.getVagas() - 1);
        cursoRepository.save(curso);
        
        // Enviar email de confirmação de inscrição
        try {
            emailService.enviarEmailInscricaoAtividade(
                usuario.getEmail(),
                usuario.getNome(),
                curso
            );
        } catch (Exception e) {
            // Log do erro, mas não falha o processo
            System.err.println("Erro ao enviar email de inscrição: " + e.getMessage());
        }
        
        return ResponseEntity.ok(inscricao);
    }
    
    public ResponseEntity<MensagemResponse> excluirInscricao(Long id) {
        Optional<Inscricao> inscricaoOpt = inscricaoRepository.findById(id);
        if (inscricaoOpt.isEmpty()) {
            throw new NaoEncontradoException("Inscrição não encontrada.");
        }

        Inscricao inscricao = inscricaoOpt.get();
        Curso curso = inscricao.getIdCurso();
        
        // Enviar email de cancelamento antes de deletar
        try {
            emailService.enviarEmailCancelamentoAtividade(
                inscricao.getIdUsuario().getEmail(),
                inscricao.getIdUsuario().getNome(),
                curso
            );
        } catch (Exception e) {
            // Log do erro, mas não falha o processo
            System.err.println("Erro ao enviar email de cancelamento: " + e.getMessage());
        }
        
        curso.setVagas(curso.getVagas() + 1);
        cursoRepository.save(curso);

        inscricaoRepository.delete(inscricao);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new MensagemResponse("Inscrição cancelada!"));
    }
    
    public ResponseEntity<List<InscricaoDTO>> listarInscricoesPorUsuario(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new NaoEncontradoException("Usuário não encontrado"));
        
        List<Inscricao> inscricoes = inscricaoRepository.findByIdUsuario(usuario);
        
        List<InscricaoDTO> inscricoesDTO = inscricoes.stream()
                .filter(inscricao -> inscricao.getIdCurso() != null) // Filtrar inscrições com curso NULL
                .map(inscricao -> {
                    InscricaoDTO dto = new InscricaoDTO();
                    dto.setId(inscricao.getId());
                    dto.setIdUsuario(inscricao.getIdUsuario().getId());
                    dto.setIdCurso(inscricao.getIdCurso().getId());
                    dto.setNomeCurso(inscricao.getIdCurso().getTitulo());
                    dto.setDescricaoCurso(inscricao.getIdCurso().getDescricao());
                    dto.setDiasCurso(inscricao.getIdCurso().getDias());
                    dto.setHorarioCurso(inscricao.getIdCurso().getHorario() != null ? 
                        inscricao.getIdCurso().getHorario().toString() : null);
                    dto.setImagemCurso(inscricao.getIdCurso().getImagem());
                    dto.setDataInscricao(inscricao.getDataInscricao());
                    return dto;
                }).toList();
        
        return ResponseEntity.ok(inscricoesDTO);
    }
}
