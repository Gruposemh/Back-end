package com.ong.backend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ong.backend.dto.UsuarioDTO;
import com.ong.backend.entities.Usuario;
import com.ong.backend.repositories.UsuarioRepository;

@Service
public class UsuarioService{

	@Autowired
	UsuarioRepository usuarioRepository;
	
	@Autowired
	PasswordEncoder config;
	
	public UsuarioDTO cadastrarUsuario(UsuarioDTO dto) {
		Usuario usuario = new Usuario();

		usuario.setNome(dto.getNome());
		usuario.setEmail(dto.getEmail());
		usuario.setSenha(config.encode(dto.getSenha()));

		usuario = usuarioRepository.save(usuario);

		return new UsuarioDTO(usuario);
	}
	
	public List<Usuario> listar(){
		return usuarioRepository.findAll();
	}
	
	public ResponseEntity<Usuario> buscarId(Long id){
		Optional<Usuario> usuario = usuarioRepository.findById(id);
		return usuario.map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
	}
	
	public String deleteUsuario(Long id) {
		usuarioRepository.deleteById(id);
		return "Usuário deletado!";
	}
	
	public Usuario atualizarUsuario(Long id, Usuario atualizado) {
		Usuario usuario = usuarioRepository.findById(id).get();   
		usuario.setNome(atualizado.getNome());
		usuario.setEmail(atualizado.getEmail());
		usuario.setSenha(atualizado.getSenha());
		return usuarioRepository.save(usuario);
	}
}
