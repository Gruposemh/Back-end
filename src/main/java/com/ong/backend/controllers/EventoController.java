package com.ong.backend.controllers;

import java.time.format.DateTimeFormatter;
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
import com.ong.backend.services.NewsletterService;

@RestController
@RequestMapping(value = "/evento")
public class EventoController {

	@Autowired
	EventoService eventoService;
	
	@Autowired
	NewsletterService newsletterService;
	
	@PostMapping(value = "/marcar")
	public ResponseEntity<Evento> marcarEvento(@RequestBody EventoDTO dto){
		ResponseEntity<Evento> response = eventoService.cadastrarEvento(dto);
		
		// Se o evento foi criado com sucesso, notificar inscritos da newsletter
		if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
			try {
				Evento evento = response.getBody();
				System.out.println("📧 Preparando notificações de novo evento: " + evento.getNome());
				
				String dataFormatada = "A definir";
				if (evento.getData() != null) {
					try {
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");
						dataFormatada = evento.getData().format(formatter);
					} catch (Exception e) {
						System.err.println("⚠️ Erro ao formatar data, usando 'A definir': " + e.getMessage());
					}
				}
				
				String local = evento.getLocal() != null ? evento.getLocal() : "A definir";
				
				System.out.println("📧 Enviando notificações - Data: " + dataFormatada + ", Local: " + local);
				
				newsletterService.notificarNovoEvento(
					evento.getNome(),
					dataFormatada,
					local
				);
				
				System.out.println("✅ Notificações de novo evento enviadas para newsletter");
			} catch (Exception e) {
				System.err.println("❌ Erro ao enviar notificações de newsletter: " + e.getMessage());
				e.printStackTrace();
				// Não falhar a criação do evento se o email falhar
			}
		}
		
		return response;
	}
	
	@GetMapping(value = "/listar")
	public List<EventoDTO> listarEvento(){ 
		return eventoService.listarEventosDTO();
	}
	
	@GetMapping(value = "/{id}")
	public EventoDTO buscarEvento(@PathVariable Long id) {
		return eventoService.buscarEventoDTO(id);
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
