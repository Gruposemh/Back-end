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

import com.ong.backend.dto.EditarPerfilDTO;
import com.ong.backend.dto.MensagemResponse;
import com.ong.backend.dto.UsuarioDTO;
import com.ong.backend.entities.Usuario;
import com.ong.backend.services.UsuarioService;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
@RequestMapping(value = "/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping(value = "/criar")
    public ResponseEntity<Usuario> cadastrar(@RequestBody UsuarioDTO dto) {
       return usuarioService.cadastrarUsuario(dto);
    }

    @GetMapping(value = "/todos")
    public ResponseEntity<List<Usuario>> listarTodos() {
        List<Usuario> usuarios = usuarioService.listar();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Long id) {
        return usuarioService.buscarId(id);
    }

    @DeleteMapping(value = "/deletar/{id}")
    public ResponseEntity<MensagemResponse> deletar(@PathVariable Long id) {
        return usuarioService.deleteUsuario(id);
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Usuario> atualizar(@PathVariable Long id, @RequestBody UsuarioDTO usuario) {
        return usuarioService.atualizarUsuario(id, usuario);
    }
    
    @PutMapping("/editar-perfil")
    public ResponseEntity<?> editarPerfil(@RequestBody EditarPerfilDTO dto) {
        Usuario usuarioLogado = getUsuarioLogado();
        if (usuarioLogado == null) {
            return ResponseEntity.status(403).body(new MensagemResponse("Usuário não autenticado"));
        }
        return usuarioService.editarPerfil(usuarioLogado.getId(), dto);
    }
    
    private Usuario getUsuarioLogado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof Usuario) {
            return (Usuario) auth.getPrincipal();
        }
        return null;
    }
}
