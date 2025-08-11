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
import com.ong.backend.entities.StatusPagamento;
import com.ong.backend.entities.Usuario;
import com.ong.backend.exceptions.NaoEncontradoException;
import com.ong.backend.repositories.CursoRepository;
import com.ong.backend.repositories.InscricaoRepository;
import com.ong.backend.repositories.UsuarioRepository;

@Service
public class InscricaoService {

	@Autowired
	InscricaoRepository inscricaoRepository;
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@Autowired
	CursoRepository cursoRepository;
	
	public ResponseEntity<Inscricao> inscrever(InscricaoDTO dto){
		Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
		        .orElseThrow(() -> new NaoEncontradoException("Usuário não encontrado"));
		
		Curso curso = cursoRepository.findById(dto.getIdCurso())
				.orElseThrow(() -> new NaoEncontradoException("Curso não encontrado"));
		
		boolean inscrito = inscricaoRepository.existsByIdUsuarioAndIdCurso(usuario, curso);
		if (inscrito) {
			throw new RuntimeException("Usuário já inscrito nesse curso.");
		}
		
		Inscricao inscricao = new Inscricao();
		inscricao.setDataInscricao(LocalDateTime.now());
		inscricao.setIdCurso(curso);
		inscricao.setIdUsuario(usuario);
		inscricao.setStatusPagamento(StatusPagamento.PENDENTE);
		inscricao = inscricaoRepository.save(inscricao);
		
		return ResponseEntity.ok(inscricao);
	}
	
	public List<Inscricao> listar(){
		return inscricaoRepository.findAll();
	}
	
	public ResponseEntity<MensagemResponse> excluirInscricao(Long id) {
	    Optional<Inscricao> inscricaoOpt = inscricaoRepository.findById(id);
	    if (inscricaoOpt.isEmpty()) {
	        throw new NaoEncontradoException("Inscrição não encontrada.");
	    }
	    inscricaoRepository.delete(inscricaoOpt.get());
	    return ResponseEntity.status(HttpStatus.OK)
	            .body(new MensagemResponse("Inscrição cancelada!"));
	}
	
}
