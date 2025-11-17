package com.ong.backend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ong.backend.dto.MensagemResponse;
import com.ong.backend.dto.UsuarioDTO;
import com.ong.backend.entities.Usuario;
import com.ong.backend.exceptions.NaoEncontradoException;
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
		Optional<Usuario> usuarios = usuarioRepository.findById(id);
        if (usuarios.isEmpty()) {
            throw new NaoEncontradoException("Nenhum curso encontrado com o ID: " + id);
        }
		usuarioRepository.deleteById(id);
		return ResponseEntity.status(HttpStatus.OK)
	            .body(new MensagemResponse("Usuário excluido!"));
	}
	
	public ResponseEntity<Usuario> atualizarUsuario(Long id, UsuarioDTO atualizado) {
		Usuario usuario = usuarioRepository.findById(id).get();   
		usuario.setNome(atualizado.getNome());
		usuario.setEmail(atualizado.getEmail());
		usuario.setSenha(atualizado.getSenha());
		
		usuario = usuarioRepository.save(usuario);
		return ResponseEntity.ok(usuario);
	}
	
	public ResponseEntity<Usuario> editarPerfil(UsuarioDTO dto) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = (Usuario) auth.getPrincipal();
		
		// Buscar usuário atualizado do banco
		Usuario usuarioAtualizado = usuarioRepository.findById(usuario.getId())
			.orElseThrow(() -> new NaoEncontradoException("Usuário não encontrado"));
		
		// Atualizar apenas nome e foto de perfil
		if (dto.getNome() != null && !dto.getNome().trim().isEmpty()) {
			usuarioAtualizado.setNome(dto.getNome());
		}
		if (dto.getFotoPerfil() != null) {
			usuarioAtualizado.setFotoPerfil(dto.getFotoPerfil());
		}
		
		usuarioAtualizado = usuarioRepository.save(usuarioAtualizado);
		return ResponseEntity.ok(usuarioAtualizado);
	}
	
	public ResponseEntity<Usuario> buscarPerfil() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = (Usuario) auth.getPrincipal();
		
		Usuario usuarioCompleto = usuarioRepository.findById(usuario.getId())
			.orElseThrow(() -> new NaoEncontradoException("Usuário não encontrado"));
		
		return ResponseEntity.ok(usuarioCompleto);
	}
}
