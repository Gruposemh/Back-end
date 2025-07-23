package com.ong.backend.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ong.backend.dto.DoacaoDTO;
import com.ong.backend.dto.MensagemResponse;
import com.ong.backend.entities.Doacao;
import com.ong.backend.entities.Usuario;
import com.ong.backend.exceptions.NaoEncontradoException;
import com.ong.backend.repositories.DoacaoRepository;
import com.ong.backend.repositories.UsuarioRepository;

@Service
public class DoacaoService {

	@Autowired
	DoacaoRepository doacaoRepository;
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	public ResponseEntity<Doacao> doar(DoacaoDTO dto){
		Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
				.orElseThrow(() -> new NaoEncontradoException("Usuário não encontrado"));
		
		Doacao doacao = new Doacao();
		doacao.setTipoDoacao(dto.getTipoDoacao());
		doacao.setValor(dto.getValor());
		doacao.setUsuario(usuario);
		doacao.setDataDoacao(LocalDateTime.now());
		doacao = doacaoRepository.save(doacao);
		
		return ResponseEntity.ok(doacao);
	}
	
	public List<Doacao> listarDoacoes(){
		return doacaoRepository.findAll();
	}
	
	public List<Doacao> doacoesUsuario(Long idUsuario){
		return doacaoRepository.findByUsuarioId(idUsuario);
	}
	
	public Doacao buscarDoacao(Long id) {
		return doacaoRepository.findById(id)
				.orElseThrow(() -> new NaoEncontradoException("Doação não encontrada"));
	}
	
	public Long doacoesPorUsuario(Long id) {
		return doacaoRepository.countByUsuarioId(id);
	}
	
	public ResponseEntity<MensagemResponse> cancelarDoacao(Long id){
		Optional <Doacao> doacao = doacaoRepository.findById(id);
		if(doacao.isEmpty()) {
			throw new NaoEncontradoException("Doação não encontrada");
		}
		doacaoRepository.deleteById(id);
		
		return ResponseEntity.status(HttpStatus.OK)
	            .body(new MensagemResponse("Doação cancelada"));
	}
}
