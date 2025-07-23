package com.ong.backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ong.backend.dto.DoacaoDTO;
import com.ong.backend.dto.MensagemResponse;
import com.ong.backend.entities.Doacao;
import com.ong.backend.services.DoacaoService;

@RestController
@RequestMapping(value = "/doacao")
public class DoacaoController {

	@Autowired
	DoacaoService doacaoService;
	
	@PostMapping(value = "/doar")
	public ResponseEntity<Doacao> doar(@RequestBody DoacaoDTO dto){
		return doacaoService.doar(dto);
	}
	
	@GetMapping(value = "/doacoes")
	public List<Doacao> listarDoacoes(){
		return doacaoService.listarDoacoes();
	}
	
	@GetMapping(value = "/usuario/{id}")
	public List<Doacao> porUsuario(@PathVariable Long id){
		return doacaoService.doacoesUsuario(id);
	}
	
	@GetMapping(value = "/{id}")
	public Doacao buscarDoacao(@PathVariable Long id) {
		return doacaoService.buscarDoacao(id);
	}
	
	@DeleteMapping(value = "/deletar/{id}")
	public ResponseEntity<MensagemResponse> excluirDoacao(@PathVariable Long id){
		return doacaoService.cancelarDoacao(id);
		}
	
}
