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

import com.ong.backend.dto.BlogDTO;
import com.ong.backend.dto.MensagemResponse;
import com.ong.backend.entities.Blog;
import com.ong.backend.services.BlogService;

@RestController
@RequestMapping(value = "blog")
public class BlogController {
	
	@Autowired
    BlogService service;

    @PostMapping(value = "/criar")
    public ResponseEntity<Blog> registrarBlog(@RequestBody BlogDTO dto) {
        return service.cadastrarBlog(dto);
    }
    
    @GetMapping(value = "/listar")
    public ResponseEntity<List<Blog>> listarTodos() {
        return ResponseEntity.ok(service.listar());
    }
    
    @GetMapping(value = "/buscar")
    public ResponseEntity<Blog> buscarPorTitulo(@RequestParam String titulo) {
        return service.buscarPorTitulo(titulo);
    }
    
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<MensagemResponse> deletarBlogPorTitulo(@PathVariable Long id) {
        return service.deletarBlog(id);
    }

    @PutMapping(value = "/atualizar/{id}")
    public ResponseEntity<MensagemResponse> atualizarBlogPorTitulo(@PathVariable Long id, @RequestBody BlogDTO dto) {
        return service.atualizarBlog(id, dto);
    }

}