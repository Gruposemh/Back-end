package com.ong.backend.services;

import java.time.LocalDateTime;
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

    public ResponseEntity<Inscricao> inscrever(InscricaoDTO dto){
        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new NaoEncontradoException("Usuário não encontrado"));
        
        boolean isVoluntario = voluntarioRepository
                .existsByIdUsuario_IdAndStatus(usuario.getId(), StatusVoluntario.APROVADO);

        if (!isVoluntario) {
            throw new RuntimeException("Somente voluntários aprovados podem se inscrever em cursos.");
        }

        // Buscar curso
        Curso curso = cursoRepository.findById(dto.getIdCurso())
                .orElseThrow(() -> new NaoEncontradoException("Curso não encontrado"));
        
        boolean inscrito = inscricaoRepository.existsByIdUsuarioAndIdCurso(usuario, curso);
        if (inscrito) {
            throw new RuntimeException("Usuário já inscrito nesse curso.");
        }
        
        int totalInscricoes = inscricaoRepository.countByIdCurso(curso);
        if (totalInscricoes >= curso.getVagas()) {
            throw new RuntimeException("Não há vagas disponíveis para este curso.");
        }
        
        Inscricao inscricao = new Inscricao();
        inscricao.setDataInscricao(LocalDateTime.now());
        inscricao.setIdCurso(curso);
        inscricao.setIdUsuario(usuario);
        inscricao = inscricaoRepository.save(inscricao);
        
        curso.setVagas(curso.getVagas() - 1);
        cursoRepository.save(curso);
        
        return ResponseEntity.ok(inscricao);
    }
    
    public ResponseEntity<MensagemResponse> excluirInscricao(Long id) {
        Optional<Inscricao> inscricaoOpt = inscricaoRepository.findById(id);
        if (inscricaoOpt.isEmpty()) {
            throw new NaoEncontradoException("Inscrição não encontrada.");
        }

        Inscricao inscricao = inscricaoOpt.get();
        Curso curso = inscricao.getIdCurso();
        curso.setVagas(curso.getVagas() + 1);
        cursoRepository.save(curso);

        inscricaoRepository.delete(inscricao);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new MensagemResponse("Inscrição cancelada!"));
    }
}
