package com.ong.backend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ong.backend.dto.EventoDTO;
import com.ong.backend.dto.MensagemResponse;
import com.ong.backend.entities.Evento;
import com.ong.backend.exceptions.DuplicadoException;
import com.ong.backend.exceptions.NaoEncontradoException;
import com.ong.backend.repositories.EventoRepository;

@Service
public class EventoService {

	@Autowired
	EventoRepository eventoRepository;
	
	public ResponseEntity<Evento> cadastrarEvento(EventoDTO dto){
		if (eventoRepository.findByNome(dto.getNome()).isPresent()) {
	        throw new DuplicadoException("Já existe um evento com esse nome.");
	    }
		Evento evento = new Evento();
		
		evento.setNome(dto.getNome());
		evento.setDescricao(dto.getDescricao());
		evento.setLocal(dto.getLocal());
		evento.setData(dto.getData());
		
		evento = eventoRepository.save(evento);
		
		return ResponseEntity.ok(evento);
	}
	
	public List<Evento> listarEventos(){
		return eventoRepository.findAll();
	}
	
	public Evento buscarEvento(Long id){
		return eventoRepository.findById(id).orElseThrow(() -> new NaoEncontradoException("Evento não encontrado"));
	}
	
	public ResponseEntity<MensagemResponse> excluirEvento(Long id){
		Optional<Evento> evento = eventoRepository.findById(id);
		if(evento.isEmpty()) {
			throw new NaoEncontradoException("Evento não encontrado");
		}
		eventoRepository.deleteById(id);
		
		return ResponseEntity.status(HttpStatus.OK)
	            .body(new MensagemResponse("Evento cancelado"));
	}
	
	public ResponseEntity<Evento> atualizarEvento(Long id, EventoDTO atualizado){
		Evento evento = eventoRepository.findById(id)
				.orElseThrow(() -> new NaoEncontradoException("Evento não encontrado"));
		if (eventoRepository.findByNome(atualizado.getNome()).isPresent()) {
	        throw new DuplicadoException("Já existe um evento com esse nome.");
	    }
		
		evento.setDescricao(atualizado.getDescricao());
		evento.setData(atualizado.getData());
		evento.setLocal(atualizado.getLocal());
		evento.setNome(atualizado.getNome());
		
		evento = eventoRepository.save(evento);
		return ResponseEntity.ok(evento);
	}
}
