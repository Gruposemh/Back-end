package com.backend.ong.controllers;

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

import com.backend.ong.dto.BlogDTO;
import com.backend.ong.entity.Blog;
import com.backend.ong.service.BlogService;

import io.swagger.v3.oas.annotations.Parameter;

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
    
    @GetMapping(value = "/{id}")
    public ResponseEntity<Blog> buscarId(@Parameter(description = "ID do blog a ser recuperado") @PathVariable Long id) {
        return service.buscarId(id);
    }
    
    @DeleteMapping(value = "/deletar/{id}")
    public ResponseEntity<String> deletarBlog(@PathVariable Long id) {
        service.deleteBlog(id);
        return ResponseEntity.ok("Blog deletado");
    }

    @PutMapping(value = "atualizar/{id}")
    public ResponseEntity<Blog> atualizarBlog(@PathVariable Long id, @RequestBody Blog atualizado) {
        Blog blog = service.atualizarBlog(id, atualizado);
        return ResponseEntity.ok(blog);
    }
}
