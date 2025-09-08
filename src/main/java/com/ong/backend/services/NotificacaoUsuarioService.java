package com.ong.backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ong.backend.dto.NotificacaoUsuarioDTO;
import com.ong.backend.entities.Notificacao;
import com.ong.backend.entities.NotificacaoUsuario;
import com.ong.backend.entities.StatusVisualizacao;
import com.ong.backend.entities.Usuario;
import com.ong.backend.exceptions.NaoEncontradoException;
import com.ong.backend.repositories.NotificacaoRepository;
import com.ong.backend.repositories.NotificacaoUsuarioRepository;
import com.ong.backend.repositories.UsuarioRepository;

@Service
public class NotificacaoUsuarioService {

	@Autowired
	NotificacaoUsuarioRepository notificacaoUsuarioRepository;
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@Autowired
	NotificacaoRepository notificacaoRepository;
	
	public ResponseEntity<NotificacaoUsuario> notificar(NotificacaoUsuarioDTO dto){
		Usuario usuario = usuarioRepository.findById(dto.getUsuario())
		    .orElseThrow(() -> new NaoEncontradoException("Usuário não encontrado"));
		Notificacao notificacao = notificacaoRepository.findById(dto.getNotificacao())
		    .orElseThrow(() -> new NaoEncontradoException("Notificação não enviada"));
		
		NotificacaoUsuario notificaoUsuario = new NotificacaoUsuario();
		notificaoUsuario.setNotificacao(notificacao);
		notificaoUsuario.setUsuario(usuario);
		notificaoUsuario.setStatus(StatusVisualizacao.NAO_VISTO);
		
		notificaoUsuario = notificacaoUsuarioRepository.save(notificaoUsuario);
		
		return ResponseEntity.ok(notificaoUsuario);
	}
	
	public List<NotificacaoUsuario> listarPorUsuario(Long id) {
	    usuarioRepository.findById(id)
	        .orElseThrow(() -> new NaoEncontradoException("Usuário não encontrado"));

	    return notificacaoUsuarioRepository.findByUsuarioId(id);
	}

	public ResponseEntity<NotificacaoUsuario> marcarComoVisto(Long id) {
	    NotificacaoUsuario notifUsuario = notificacaoUsuarioRepository.findById(id)
	        .orElseThrow(() -> new NaoEncontradoException("Notificação não encontrada para o usuário"));
	    
	    notifUsuario.setStatus(StatusVisualizacao.VISTO);
	    notificacaoUsuarioRepository.save(notifUsuario);
	    
	    return ResponseEntity.ok(notifUsuario);
	}
}