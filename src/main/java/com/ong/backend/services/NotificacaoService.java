package com.ong.backend.services;

import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ong.backend.dto.NotificacaoDTO;
import com.ong.backend.entities.Notificacao;
import com.ong.backend.repositories.NotificacaoRepository;

@Service
public class NotificacaoService {

	@Autowired
	NotificacaoRepository notificacaoRepository;
	
	public ResponseEntity<Notificacao> enviarNotificacao(NotificacaoDTO dto){
		Notificacao notificacao = new Notificacao();
		
		notificacao.setAssunto(dto.getAssunto());
		notificacao.setDataEnvio(LocalTime.now());
		notificacao.setDescricao(dto.getDescricao());
		notificacao.setImgUrl(dto.getImgUrl());
		
		notificacao = notificacaoRepository.save(notificacao);
		
		return ResponseEntity.ok(notificacao);
	}
}