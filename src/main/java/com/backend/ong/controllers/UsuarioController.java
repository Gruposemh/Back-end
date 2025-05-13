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

import com.backend.ong.dto.UsuarioDTO;
import com.backend.ong.entity.Usuario;
import com.backend.ong.repositories.UsuarioRepository;
import com.backend.ong.service.UsuarioService;

@RestController
@RequestMapping(value = "/usuario")
public class UsuarioController {

	@Autowired 
	UsuarioService service;	
	
	@Autowired
	UsuarioRepository repo;
	
	@PostMapping(value = "/criar")
	public ResponseEntity<Usuario> registerUsuario(@RequestBody UsuarioDTO dto) {
		return service.cadastrarUsuario(dto);
	}
	
	@GetMapping(value ="/todos")
    public ResponseEntity<List<Usuario>> listarTodos(){
        return ResponseEntity.ok(service.listar());
    }
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<Usuario> getMusicaById(@PathVariable Long id){
		return service.buscarId(id);
	}
	
	@DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<String> deletarUsuario(@PathVariable Long id){
        service.deleteUsuario(id);
        return ResponseEntity.ok("Usuário deletado");
    }
	
	@PutMapping(value = "/atualizar/{id}")
    public ResponseEntity<Usuario> atualizarUsuario(@PathVariable Long id, @RequestBody Usuario atualizado ){
        Usuario usuario = service.atualizarUsuario(id, atualizado);
        return ResponseEntity.ok(usuario); 
    }
}
