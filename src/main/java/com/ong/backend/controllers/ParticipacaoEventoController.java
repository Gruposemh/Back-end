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

import com.ong.backend.dto.MensagemResponse;
import com.ong.backend.dto.ParticipacaoEventoDTO;
import com.ong.backend.entities.ParticipacaoEvento;
import com.ong.backend.services.ParticipacaoEventoService;

@RestController
@RequestMapping(value = "/participar")
public class ParticipacaoEventoController {

    @Autowired
    ParticipacaoEventoService participacaoEventoService;
    
    @PostMapping
    public ResponseEntity<ParticipacaoEvento> participar(@RequestBody ParticipacaoEventoDTO dto){
        return participacaoEventoService.participar(dto);
    }
    
    @GetMapping(value = "/listar")
    public ResponseEntity<List<ParticipacaoEvento>> participacoes(){
        List<ParticipacaoEvento> list = participacaoEventoService.participacoes();
        return ResponseEntity.ok(list);
    }
    
    @GetMapping(value = "/{id}")
    public ResponseEntity<ParticipacaoEvento> participacao(@PathVariable Long id) {
        ParticipacaoEvento pe = participacaoEventoService.buscarPartipacao(id);
        return ResponseEntity.ok(pe);        
    }
    
    @GetMapping(value = "/usuario/{id}")
    public ResponseEntity<List<ParticipacaoEventoDTO>> listarPorUsuario(@PathVariable Long id){
        List<ParticipacaoEventoDTO> dtos = participacaoEventoService.listarPorUsuario(id);
        return ResponseEntity.ok(dtos);
    }
    
    @DeleteMapping(value = "/deletar/{id}")
    public ResponseEntity<MensagemResponse> cancelarParticipacao(@PathVariable Long id){
        return participacaoEventoService.excluirParticipacao(id);
    }
}