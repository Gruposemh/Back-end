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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ong.backend.dto.AtividadeDTO;
import com.ong.backend.dto.MensagemResponse;
import com.ong.backend.entities.Atividade;
import com.ong.backend.services.AtividadeService;

@RestController
@RequestMapping(value = "/atividade")
public class AtividadeController {

	@Autowired
	AtividadeService atividadeService;
	
	@PostMapping(value = "/cadastrar")
	public ResponseEntity<Atividade> cadastrarAtividade(@RequestBody AtividadeDTO dto){
		return atividadeService.cadastrarAtividade(dto);
	}
	
	@GetMapping(value = "/listar")
    public ResponseEntity<List<Atividade>> listarTodos() {
        return ResponseEntity.ok(atividadeService.listar());
    }
	
	@GetMapping("/buscar")
    public ResponseEntity<List<Atividade>> buscarPorNome(@RequestParam String nome) {
        return atividadeService.buscarPorNome(nome);
    }
	
	@DeleteMapping("/deletar/{id}")
    public ResponseEntity<MensagemResponse> excluirAtividade(@PathVariable Long id) {
        return atividadeService.excluirAtividade(id);
    }
	
	@PutMapping(value = "/atualizar/{id}")
    public ResponseEntity<Atividade> atualizarAtividade(@PathVariable Long id, @RequestBody AtividadeDTO atualizado) {
        return atividadeService.atualizarAtividade(id, atualizado);
	}
}

