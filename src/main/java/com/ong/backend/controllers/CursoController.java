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
import com.ong.backend.dto.CursoDTO;
import com.ong.backend.dto.MensagemResponse;
import com.ong.backend.entities.Curso;
import com.ong.backend.services.CursoService;

@RestController
@RequestMapping(value = "/curso")
public class CursoController {

	@Autowired
	CursoService cursoService;
	
	@PostMapping(value = "/cadastrar")
	public ResponseEntity<Curso> cadastrarCurso(@RequestBody CursoDTO dto){
		return cursoService.cadastrarCurso(dto);
	}
	
	@GetMapping(value = "/listar")
    public ResponseEntity<List<CursoDTO>> listarTodos() {
        return ResponseEntity.ok(cursoService.listar());
    }
	
	@GetMapping(value = "/inscricoes/{id}")
	public ResponseEntity<CursoDTO> listarInscricoesCurso(@PathVariable Long id){
		return ResponseEntity.ok(cursoService.listarInscricoesCurso(id));
	}
	
	@GetMapping("/buscar")
    public ResponseEntity<List<Curso>> buscarPorTitulo(@RequestParam String titulo) {
        return cursoService.buscarPorTitulo(titulo);
    }
	
	@DeleteMapping("/deletar/{id}")
    public ResponseEntity<MensagemResponse> excluirCurso(@PathVariable Long id) {
        return cursoService.excluirCurso(id);
    }
	
	@PutMapping(value = "/atualizar/{id}")
    public ResponseEntity<?> atualizarCurso(@PathVariable Long id, @RequestBody CursoDTO atualizado) {
        return cursoService.atualizarCurso(id, atualizado);
	}
}

