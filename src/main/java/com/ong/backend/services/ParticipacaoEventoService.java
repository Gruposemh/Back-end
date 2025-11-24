package com.ong.backend.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.ong.backend.dto.MensagemResponse;
import com.ong.backend.dto.ParticipacaoEventoDTO;
import com.ong.backend.entities.Evento;
import com.ong.backend.entities.ParticipacaoEvento;
import com.ong.backend.entities.Usuario;
import com.ong.backend.exceptions.NaoEncontradoException;
import com.ong.backend.repositories.EventoRepository;
import com.ong.backend.repositories.ParticipacaoEventoRepository;
import com.ong.backend.repositories.UsuarioRepository;

@Service
public class ParticipacaoEventoService {

	@Autowired
	ParticipacaoEventoRepository participacaoEventoRepository;
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@Autowired
	EventoRepository eventoRepository;
	
	@Autowired
	EmailService emailService;
	
	public ResponseEntity<ParticipacaoEvento> participar(@RequestBody ParticipacaoEventoDTO dto){
		Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
		        .orElseThrow(() -> new NaoEncontradoException("Usuário não encontrado"));
		
		Evento evento = eventoRepository.findById(dto.getIdEvento())
				.orElseThrow(() -> new NaoEncontradoException("Evento não encontrado"));
		
		boolean inscrito = participacaoEventoRepository.existsByUsuarioAndEvento(usuario, evento);
		if (inscrito) {
			throw new RuntimeException("Usuário já inscrito nesse evento.");
		}
		
		ParticipacaoEvento participarEvento = new ParticipacaoEvento();
		participarEvento.setEvento(evento);
		participarEvento.setTipoParticipacao(dto.getTipoParticipacao());
		participarEvento.setUsuario(usuario);
		participarEvento = participacaoEventoRepository.save(participarEvento);
		
		// Enviar email de confirmação de participação
		try {
			emailService.enviarEmailInscricaoEvento(
				usuario.getEmail(),
				usuario.getNome(),
				evento
			);
		} catch (Exception e) {
			// Log do erro, mas não falha o processo
			System.err.println("Erro ao enviar email de participação: " + e.getMessage());
		}
		
		return ResponseEntity.ok(participarEvento);
	}
	
	public List<ParticipacaoEvento> participacoes(){
		return participacaoEventoRepository.findAll();
	}
	
	public ParticipacaoEvento buscarPartipacao(Long id){
		return participacaoEventoRepository.findById(id).orElseThrow(() -> new NaoEncontradoException("Participação não encontrada"));
	}
	
	public List<ParticipacaoEventoDTO> listarPorUsuario(Long idUsuario) {
	    Usuario usuario = usuarioRepository.findById(idUsuario)
	            .orElseThrow(() -> new NaoEncontradoException("Usuário não encontrado"));

	    List<ParticipacaoEvento> participacoes = participacaoEventoRepository.findByUsuario(usuario);
	    return participacoes.stream()
	            .map(ParticipacaoEventoDTO::new)
	            .collect(Collectors.toList());
	}

	public ResponseEntity<MensagemResponse> excluirParticipacao(Long id){
		Optional<ParticipacaoEvento> participacaoEvento = participacaoEventoRepository.findById(id);
		if(participacaoEvento.isEmpty()) {
			throw new NaoEncontradoException("Participação não encontrada");
		}
		
		ParticipacaoEvento participacao = participacaoEvento.get();
		
		// Enviar email de cancelamento antes de deletar
		try {
			emailService.enviarEmailCancelamentoEvento(
				participacao.getUsuario().getEmail(),
				participacao.getUsuario().getNome(),
				participacao.getEvento()
			);
		} catch (Exception e) {
			// Log do erro, mas não falha o processo
			System.err.println("Erro ao enviar email de cancelamento: " + e.getMessage());
		}
		
		participacaoEventoRepository.deleteById(id);
		
		return ResponseEntity.status(HttpStatus.OK)
	            .body(new MensagemResponse("Participação cancelada"));
	}
	
	public ResponseEntity<ParticipacaoEventoDTO> confirmarPresenca(Long id) {
		ParticipacaoEvento participacao = participacaoEventoRepository.findById(id)
				.orElseThrow(() -> new NaoEncontradoException("Participação não encontrada"));
		
		participacao.setConfirmado(true);
		participacao = participacaoEventoRepository.save(participacao);
		
		// Enviar email de confirmação
		try {
			emailService.enviarEmailInscricaoEvento(
				participacao.getUsuario().getEmail(),
				participacao.getUsuario().getNome(),
				participacao.getEvento()
			);
		} catch (Exception e) {
			// Log do erro, mas não falha o processo
			System.err.println("Erro ao enviar email de confirmação: " + e.getMessage());
		}
		
		return ResponseEntity.ok(new ParticipacaoEventoDTO(participacao));
	}
	
	public List<ParticipacaoEventoDTO> listarPorEvento(Long eventoId) {
		Evento evento = eventoRepository.findById(eventoId)
				.orElseThrow(() -> new NaoEncontradoException("Evento não encontrado"));
		
		List<ParticipacaoEvento> participacoes = participacaoEventoRepository.findByEvento(evento);
		return participacoes.stream()
				.map(ParticipacaoEventoDTO::new)
				.collect(Collectors.toList());
	}
}