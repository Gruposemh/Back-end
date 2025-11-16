package com.ong.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ong.backend.dto.InscricaoDTO;
import com.ong.backend.dto.MensagemResponse;
import com.ong.backend.entities.Inscricao;
import com.ong.backend.services.InscricaoService;

@RestController
@RequestMapping(value = "/inscricao")
public class InscricaoController {

	@Autowired
	InscricaoService inscricaoService;
	
	@PostMapping(value = "/inscrever")
	public ResponseEntity<Inscricao> inscrever (@RequestBody InscricaoDTO dto){
		return inscricaoService.inscrever(dto);
	}
	
	@DeleteMapping(value = "/deletar/{id}")
	public ResponseEntity<MensagemResponse> excluirInscricao(@PathVariable Long id){
		return inscricaoService.excluirInscricao(id);
	}
	
}