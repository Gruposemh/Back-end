package com.ong.backend.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ong.backend.dto.ComentarioDTO;
import com.ong.backend.dto.MensagemResponse;
import com.ong.backend.entities.Blog;
import com.ong.backend.entities.Comentario;
import com.ong.backend.entities.Usuario;
import com.ong.backend.exceptions.NaoEncontradoException;
import com.ong.backend.repositories.BlogRepository;
import com.ong.backend.repositories.ComentarioRepository;
import com.ong.backend.repositories.UsuarioRepository;

@Service
public class ComentarioService {

	@Autowired
	ComentarioRepository comentarioRepository;
	
	@Autowired
	BlogRepository blogRepository;
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	public ResponseEntity<Comentario> comentar(ComentarioDTO dto) {
	    Blog blog = blogRepository.findById(dto.getIdBlog())
	        .orElseThrow(() -> new NaoEncontradoException("Blog não encontrado"));
	    
	    Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
	        .orElseThrow(() -> new NaoEncontradoException("Usuário não encontrado"));

	    Comentario comentario = new Comentario();
	    comentario.setIdUsuario(usuario);
	    comentario.setIdBlog(blog);
	    comentario.setDataComentario(LocalDateTime.now());
	    comentario.setComentario(dto.getComentario());

	    comentarioRepository.save(comentario);

	    return ResponseEntity.ok(comentario);
	}
	
	public ResponseEntity<MensagemResponse> excluirComentario(Long id) {
	    Optional<Comentario> comentarioOpt = comentarioRepository.findById(id);
	    if (comentarioOpt.isEmpty()) {
	        throw new NaoEncontradoException("Comentário não encontrado.");
	    }
	    comentarioRepository.delete(comentarioOpt.get());
	    return ResponseEntity.status(HttpStatus.OK)
	            .body(new MensagemResponse("Comentário excluído!"));
	}
	
	public List<Comentario> listar(){
		return comentarioRepository.findAll();
	}
	
	public ResponseEntity<List<ComentarioDTO>> listarPorBlog(Long id) {
	    Blog blog = blogRepository.findById(id)
	        .orElseThrow(() -> new NaoEncontradoException("Blog não encontrado"));

	    List<Comentario> comentarios = comentarioRepository.findByIdBlogId(id);
	    List<ComentarioDTO> dto = comentarios.stream().map(ComentarioDTO::new).toList();

	    return ResponseEntity.ok(dto);
	}

	public ResponseEntity<Comentario> editarComentario(Long id, Comentario atualizado) {
		Comentario comentario = comentarioRepository.findById(id).get(); 
		comentario.setComentario(atualizado.getComentario());
		comentario = comentarioRepository.save(comentario);
		return ResponseEntity.ok(comentario);
	}
}
