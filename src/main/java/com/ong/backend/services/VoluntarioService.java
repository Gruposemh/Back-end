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

@Service
public class VoluntarioService {

    @Autowired
    VoluntarioRepository voluntarioRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    public ResponseEntity<Voluntario> tornarVoluntario(VoluntarioDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new NaoEncontradoException("Usuário não encontrado"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate nascimento = LocalDate.parse(dto.getDataNascimento(), formatter);

        int idade = Period.between(nascimento, LocalDate.now()).getYears();
        if (idade < 18) {
            throw new IllegalArgumentException("Usuário precisa ter mais de 18 anos para se tornar voluntário.");
        }

        Voluntario voluntario = new Voluntario();
        voluntario.setCpf(dto.getCpf());
        voluntario.setDataVoluntario(LocalDateTime.now());
        voluntario.setIdUsuario(usuario);
        voluntario.setDataNascimento(dto.getDataNascimento());
        voluntario.setTelefone(dto.getTelefone());
        voluntario.setDescricao(dto.getDescricao());
        voluntario.setEndereco(dto.getEndereco());
        voluntario.setStatus(StatusVoluntario.PENDENTE);

        voluntario = voluntarioRepository.save(voluntario);

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

        return ResponseEntity.ok(voluntario);
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
}