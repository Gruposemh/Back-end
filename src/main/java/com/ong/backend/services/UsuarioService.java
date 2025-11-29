package com.ong.backend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ong.backend.dto.EditarPerfilDTO;
import com.ong.backend.dto.MensagemResponse;
import com.ong.backend.dto.UsuarioDTO;
import com.ong.backend.entities.Usuario;
import com.ong.backend.entities.Voluntario;
import com.ong.backend.exceptions.NaoEncontradoException;
import com.ong.backend.repositories.UsuarioRepository;
import com.ong.backend.repositories.VoluntarioRepository;

@Service
public class UsuarioService{

	@Autowired
	UsuarioRepository usuarioRepository;
	
	@Autowired
	VoluntarioRepository voluntarioRepository;
	
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
		
		// Validar se o email já está em uso por outro usuário
		if (atualizado.getEmail() != null && !atualizado.getEmail().equals(usuario.getEmail())) {
			Optional<Usuario> usuarioComEmail = usuarioRepository.findByEmail(atualizado.getEmail());
			if (usuarioComEmail.isPresent()) {
				throw new IllegalArgumentException("Este email já está em uso por outro usuário.");
			}
		}
		
		usuario.setNome(atualizado.getNome());
		usuario.setEmail(atualizado.getEmail());
		
		if (atualizado.getSenha() != null && !atualizado.getSenha().isBlank()) {
		    usuario.setSenha(config.encode(atualizado.getSenha()));
		}
		
		usuario = usuarioRepository.save(usuario);
		return ResponseEntity.ok(usuario);
	}
	
	public ResponseEntity<?> editarPerfil(Long idUsuario, EditarPerfilDTO dto) {
		System.out.println("🔍 EditarPerfil - Iniciando para usuário ID: " + idUsuario);
		System.out.println("📝 Dados recebidos - Nome: " + dto.getNome() + ", ImagemPerfil: " + dto.getImagemPerfil());
		
		Usuario usuario = usuarioRepository.findById(idUsuario)
				.orElseThrow(() -> new NaoEncontradoException("Usuário não encontrado"));
		
		System.out.println("👤 Usuário encontrado: " + usuario.getNome() + ", ImagemPerfil atual: " + usuario.getImagemPerfil());
		
		// Atualizar campos básicos (todos os usuários)
		if (dto.getNome() != null && !dto.getNome().isBlank()) {
			usuario.setNome(dto.getNome());
			System.out.println("✅ Nome atualizado para: " + dto.getNome());
		}
		
		if (dto.getImagemPerfil() != null) {
			usuario.setImagemPerfil(dto.getImagemPerfil());
			System.out.println("✅ ImagemPerfil atualizada para: " + dto.getImagemPerfil());
		}
		
		// Atualizar campos de voluntário (se for voluntário)
		Optional<Voluntario> voluntarioOpt = voluntarioRepository.findByIdUsuarioId(idUsuario);
		if (voluntarioOpt.isPresent()) {
			Voluntario voluntario = voluntarioOpt.get();
			
			if (dto.getTelefone() != null && !dto.getTelefone().isBlank()) {
				voluntario.setTelefone(dto.getTelefone());
			}
			
			if (dto.getEndereco() != null && !dto.getEndereco().isBlank()) {
				voluntario.setEndereco(dto.getEndereco());
			}
			
			voluntarioRepository.save(voluntario);
			System.out.println("✅ Dados de voluntário atualizados");
		}
		
		usuario = usuarioRepository.save(usuario);
		System.out.println("💾 Usuário salvo no banco. ImagemPerfil final: " + usuario.getImagemPerfil());
		
		return ResponseEntity.ok(new MensagemResponse("Perfil atualizado com sucesso!"));
	}
}
