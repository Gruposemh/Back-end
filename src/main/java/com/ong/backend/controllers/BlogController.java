package com.ong.backend.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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
import com.ong.backend.entities.Usuario;
import com.ong.backend.services.BlogService;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("blog")
public class BlogController {
    
    @Autowired
    BlogService service;

    @PostMapping("/criar")
    public ResponseEntity<Blog> registrarBlog(@RequestBody BlogDTO dto) {
        return service.cadastrarBlog(dto);
    }
    
    @GetMapping("/listar")
    public ResponseEntity<List<Blog>> listarTodos() {
        return ResponseEntity.ok(service.listar());
    }
    
    @GetMapping("/buscar")
    public ResponseEntity<Blog> buscarPorTitulo(@RequestParam String titulo) {
        return service.buscarPorTitulo(titulo);
    }
    
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<MensagemResponse> deletarBlogPorId(@PathVariable Long id) {
        Usuario usuarioLogado = getUsuarioLogado();
        return service.deletarBlog(id, usuarioLogado);
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Blog> atualizarBlogPorId(@PathVariable Long id, @RequestBody BlogDTO dto) {
        Usuario usuarioLogado = getUsuarioLogado();
        return service.atualizarBlog(id, dto, usuarioLogado);
    }

    private Usuario getUsuarioLogado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (Usuario) auth.getPrincipal();
    }
}