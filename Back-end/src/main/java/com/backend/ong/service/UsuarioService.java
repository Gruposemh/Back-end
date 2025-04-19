package com.backend.ong.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.backend.ong.dto.UsuarioDTO;
import com.backend.ong.entity.Usuario;
import com.backend.ong.repositories.UsuarioRepository;

@Service
public class UsuarioService {

	@Autowired
	UsuarioRepository repo;
	
	public ResponseEntity<Usuario> cadastrarUsuario(UsuarioDTO dto) {
		Usuario usuario = new Usuario(dto);
		usuario = repo.save(usuario);
		return ResponseEntity.ok(usuario);
	}
	
	public List<Usuario> listar(){
		return repo.findAll();
	}
	
	public ResponseEntity<Usuario> buscarId(Long id){
		Optional<Usuario> usuario = repo.findById(id);
		return usuario.map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
	}
	
	public String deleteUsuario(Long id) {
		repo.deleteById(id);
		return "Usuário deletado!";
	}
	
	public Usuario atualizarUsuario(Long id, Usuario atualizado) {
		Usuario usuario = repo.findById(id).get();   
		usuario.setNome(atualizado.getNome());
		usuario.setEmail(atualizado.getEmail());
		usuario.setSenha(atualizado.getSenha());
		return repo.save(usuario);
	}
}
