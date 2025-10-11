package com.ong.backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ong.backend.dto.EventoDTO;
import com.ong.backend.dto.MensagemResponse;
import com.ong.backend.entities.Evento;
import com.ong.backend.services.EventoService;

@RestController
@RequestMapping(value = "/evento")
public class EventoController {

	@Autowired
	EventoService eventoService;
	
	@PostMapping(value = "/marcar")
	public ResponseEntity<Evento> marcarEvento(@RequestBody EventoDTO dto){
		return eventoService.cadastrarEvento(dto);
	}
	
	@GetMapping(value = "/listar")
	public List<Evento> listarEvento(){ 
		return eventoService.listarEventos();
	}
	
	@GetMapping(value = "/{id}")
	public Evento buscarEvento(@PathVariable Long id) {
		return eventoService.buscarEvento(id);
	}
	
	@DeleteMapping(value = "/deletar/{id}")
	public ResponseEntity<MensagemResponse> excluirEvento(@PathVariable Long id){
		return eventoService.excluirEvento(id);
	}
	
	@PutMapping(value = "/atualizar/{id}")
	public ResponseEntity<Evento> atualizarEvento(@PathVariable Long id, @RequestBody EventoDTO evento){
		return eventoService.atualizarEvento(id, evento);
	}
	
}
