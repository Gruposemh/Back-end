package com.ong.backend.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ong.backend.dto.MensagemResponse;
import com.ong.backend.dto.TarefasDTO;
import com.ong.backend.entities.Tarefas;
import com.ong.backend.services.TarefasService;

@RestController
@RequestMapping(value = "/tarefas")
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:3000", "http://localhost:5500", "http://127.0.0.1:5500", "http://localhost:8080", "http://127.0.0.1:8080"})
public class TarefasController {

	@Autowired
	private TarefasService tarefasService;
	
	@PostMapping(value = "/criar")
    public ResponseEntity<Tarefas> cadastrar(@RequestBody TarefasDTO dto) {
       return tarefasService.cadastrarTarefa(dto);
    }

    @GetMapping(value = "/todas")
    public ResponseEntity<List<Tarefas>> listarTodos() {
        List<Tarefas> tarefas = tarefasService.listarTarefas();
        return ResponseEntity.ok(tarefas);
    }
    
    @GetMapping(value = "/usuario/{id}")
	public List<Tarefas> porUsuario(@PathVariable Long id){
		return tarefasService.tarefasUsuario(id);
	}

    @DeleteMapping(value = "/deletar/{id}")
    public ResponseEntity<MensagemResponse> deletar(@PathVariable Long id) {
        return tarefasService.deletarTarefa(id);
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Tarefas> atualizar(@PathVariable Long id, @RequestBody TarefasDTO usuario) {
        return tarefasService.atualizarTarefa(id, usuario);
    }
}
