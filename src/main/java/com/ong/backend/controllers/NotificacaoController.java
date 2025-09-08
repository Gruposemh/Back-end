package com.ong.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ong.backend.dto.NotificacaoDTO;
import com.ong.backend.entities.Notificacao;
import com.ong.backend.services.NotificacaoService;

@RestController
@RequestMapping(value = "notificacao")
public class NotificacaoController {

	@Autowired
	NotificacaoService notificacaoService;
	
	@PostMapping(value = "/criar")
	public ResponseEntity<Notificacao> notificar(@RequestBody NotificacaoDTO dto){
		return notificacaoService.enviarNotificacao(dto);
	}
}