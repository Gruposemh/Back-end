package com.ong.backend.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.ong.backend.dto.InscricaoDTO;
import com.ong.backend.dto.MensagemResponse;
import com.ong.backend.entities.Atividade;
import com.ong.backend.entities.Inscricao;
import com.ong.backend.entities.StatusVoluntario;
import com.ong.backend.entities.Usuario;
import com.ong.backend.entities.Voluntario;
import com.ong.backend.exceptions.NaoEncontradoException;
import com.ong.backend.repositories.AtividadeRepository;
import com.ong.backend.repositories.InscricaoRepository;
import com.ong.backend.repositories.UsuarioRepository;
import com.ong.backend.repositories.VoluntarioRepository;

@Service
public class InscricaoService {

	@Autowired
	InscricaoRepository inscricaoRepository;
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@Autowired
	AtividadeRepository atividadeRepository;
	
	@Autowired
	VoluntarioRepository voluntarioRepository;
	
	public ResponseEntity<Inscricao> inscrever(InscricaoDTO dto){
		// Obtém o usuário autenticado do contexto de segurança
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = (Usuario) auth.getPrincipal();
		
		// Verifica se o usuário é voluntário aprovado
		Optional<Voluntario> voluntarioOpt = voluntarioRepository.findByIdUsuario(usuario);
		if (voluntarioOpt.isEmpty() || voluntarioOpt.get().getStatus() != StatusVoluntario.APROVADO) {
			throw new RuntimeException("Apenas voluntários aprovados podem se inscrever em atividades.");
		}
		
		Atividade atividade = atividadeRepository.findById(dto.getIdAtividade())
				.orElseThrow(() -> new NaoEncontradoException("Atividade não encontrada"));
		
		boolean inscrito = inscricaoRepository.existsByIdUsuarioAndIdAtividade(usuario, atividade);
		if (inscrito) {
			throw new RuntimeException("Usuário já inscrito nessa atividade.");
		}
		
		int totalInscricoes = inscricaoRepository.countByIdAtividade(atividade);
	    if (totalInscricoes >= atividade.getVagas()) {
	        throw new RuntimeException("Não há vagas disponíveis para esta atividade.");
	    }
		
		Inscricao inscricao = new Inscricao();
		inscricao.setDataInscricao(LocalDateTime.now());
		inscricao.setIdAtividade(atividade);
		inscricao.setIdUsuario(usuario);
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
	
	public ResponseEntity<MensagemResponse> cancelarInscricaoPorUsuarioEAtividade(Long idAtividade) {
		// Obtém o usuário autenticado do contexto de segurança
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = (Usuario) auth.getPrincipal();
		
		Atividade atividade = atividadeRepository.findById(idAtividade)
				.orElseThrow(() -> new NaoEncontradoException("Atividade não encontrada"));
		
		Optional<Inscricao> inscricaoOpt = inscricaoRepository.findByIdUsuarioAndIdAtividade(usuario, atividade);
		if (inscricaoOpt.isEmpty()) {
			throw new NaoEncontradoException("Inscrição não encontrada para este usuário e atividade.");
		}
		
		inscricaoRepository.delete(inscricaoOpt.get());
		return ResponseEntity.status(HttpStatus.OK)
				.body(new MensagemResponse("Inscrição cancelada com sucesso!"));
	}
	
}
