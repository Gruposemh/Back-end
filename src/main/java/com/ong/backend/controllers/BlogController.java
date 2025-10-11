package com.ong.backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
import com.ong.backend.entities.StatusPublicacao;
import com.ong.backend.entities.Usuario;
import com.ong.backend.repositories.BlogRepository;
import com.ong.backend.services.BlogService;

@RestController
@RequestMapping("blog")
public class BlogController {
    
    @Autowired
    BlogService service;
    
    @Autowired
    BlogRepository blogRepository;

    @PostMapping("/criar")
    public ResponseEntity<Blog> registrarBlog(@RequestBody BlogDTO dto) {
        return service.cadastrarBlog(dto);
    }
    
    // Para ADMIN
    @GetMapping("/listar")
    public ResponseEntity<List<Blog>> listarTodos() {
        return ResponseEntity.ok(service.listar());
    }
    
    // Para USUARIO
    @GetMapping("/buscar")
    public ResponseEntity<Blog> buscarPorTitulo(@RequestParam String titulo) {
        return service.buscarPorTitulo(titulo);
    }
    
    @GetMapping("/blogs")
    public List<Blog> listarBlogsAprovados() {
        return blogRepository.findByStatus(StatusPublicacao.APROVADO);
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
    
    // Parte de validação
    @PutMapping("aprovar/{id}")
    public ResponseEntity<Blog> aprovar(@PathVariable Long id) {
        return service.aprovarBlog(id);
    }
    @DeleteMapping("negar/{id}")
    public ResponseEntity<MensagemResponse> negarBlog(@PathVariable Long id){
    	return service.negarBlog(id);
    }
}