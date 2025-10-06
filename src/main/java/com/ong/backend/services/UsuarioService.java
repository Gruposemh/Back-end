package com.ong.backend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ong.backend.dto.MensagemResponse;
import com.ong.backend.dto.UsuarioDTO;
import com.ong.backend.entities.Usuario;
import com.ong.backend.repositories.UsuarioRepository;

@Service
public class UsuarioService{

	@Autowired
	UsuarioRepository usuarioRepository;
	
	@Autowired
	PasswordEncoder config;
	
	public ResponseEntity<Usuario> cadastrarUsuario(UsuarioDTO dto) {
		Usuario usuario = new Usuario();

		usuario.setNome(dto.getNome());
		usuario.setEmail(dto.getEmail());
		usuario.setSenha(config.encode(dto.getSenha()));
		usuario.setRole(dto.getRole()); 

		usuario = usuarioRepository.save(usuario);

		return ResponseEntity.ok(usuario);
	}
	
	public List<Usuario> listar(){
		return usuarioRepository.findAll();
	}
	
	public ResponseEntity<Usuario> buscarId(Long id){
		Optional<Usuario> usuario = usuarioRepository.findById(id);
		return usuario.map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
	}
	
	public ResponseEntity<MensagemResponse> deleteUsuario(Long id) {
		usuarioRepository.deleteById(id);
		return ResponseEntity.status(HttpStatus.OK)
	            .body(new MensagemResponse("Usuário excluido!"));
	}
	
	public ResponseEntity<Usuario> atualizarUsuario(Long id, UsuarioDTO atualizado) {
	    Usuario usuario = usuarioRepository.findById(id).orElseThrow();

	    usuario.setNome(atualizado.getNome());
	    usuario.setEmail(atualizado.getEmail());

	    if (atualizado.getSenha() != null && !atualizado.getSenha().isBlank()) {
	        usuario.setSenha(config.encode(atualizado.getSenha()));
	    }

	    usuario = usuarioRepository.save(usuario);
	    return ResponseEntity.ok(usuario);
	}

}
