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

import com.ong.backend.dto.ComentarioDTO;
import com.ong.backend.dto.MensagemResponse;
import com.ong.backend.entities.Comentario;
import com.ong.backend.services.ComentarioService;

@RestController
@RequestMapping(value = "/comentario")
public class ComentarioController {

	@Autowired
	ComentarioService comentarioService;
	
	@PostMapping(value = "/postar")
    public ResponseEntity<Comentario> comentar(@RequestBody ComentarioDTO dto) {
        return comentarioService.comentar(dto);
    }
	
	@GetMapping(value = "/listar")
    public ResponseEntity<List<Comentario>> listarTodos() {
        return ResponseEntity.ok(comentarioService.listar());
    }
	@GetMapping("/blog/{id}")
	public ResponseEntity<List<ComentarioDTO>> listarComentariosPorBlog(@PathVariable Long id) {
	    return comentarioService.listarPorBlog(id);
	}

	@DeleteMapping("/deletar/{id}")
    public ResponseEntity<MensagemResponse> excluir(@PathVariable Long id) {
        return comentarioService.excluirComentario(id);
    }
	
	@PutMapping(value = "/atualizar/{id}")
    public ResponseEntity <Comentario> editar(@PathVariable Long id, @RequestBody Comentario comentario) {
        return comentarioService.editarComentario(id, comentario);
    }
}
