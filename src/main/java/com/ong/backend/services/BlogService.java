package com.ong.backend.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ong.backend.dto.BlogDTO;
import com.ong.backend.dto.MensagemResponse;
import com.ong.backend.entities.Blog;
import com.ong.backend.entities.StatusPublicacao;
import com.ong.backend.entities.Usuario;
import com.ong.backend.exceptions.DuplicadoException;
import com.ong.backend.exceptions.NaoEncontradoException;
import com.ong.backend.repositories.BlogRepository;
import com.ong.backend.repositories.UsuarioRepository;

@Service
public class BlogService {

	@Autowired
	BlogRepository blogRepository;

	@Autowired
	UsuarioRepository usuarioRepository;

	public ResponseEntity<Blog> cadastrarBlog(BlogDTO dto) {
		if (blogRepository.findByTituloMateria(dto.getTituloMateria()).isPresent()) {
			throw new DuplicadoException("Já existe um blog com esse título.");
		}

		Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
				.orElseThrow(() -> new NaoEncontradoException("Usuário não encontrado"));

		Blog blog = new Blog();
		blog.setTituloMateria(dto.getTituloMateria());
		blog.setBairro(dto.getBairro());
		blog.setInformacao(dto.getInformacao());
		blog.setUrlNoticia(dto.getUrlNoticia());
		blog.setAnonima(dto.isAnonima());
		blog.setDataPostagem(LocalDateTime.now());
		blog.setIdUsuario(usuario);
		blog.setStatus(StatusPublicacao.PENDENTE);

		blog = blogRepository.save(blog);
		return ResponseEntity.ok(blog);
	}

	public List<Blog> listar() {
		return blogRepository.findAll();
	}

	public ResponseEntity<Blog> buscarPorTitulo(String titulo) {
		Optional<Blog> blog = blogRepository.findByTituloMateria(titulo);
		return blog.map(ResponseEntity::ok)
				.orElseThrow(() -> new NaoEncontradoException("Blog não encontrado com o título: " + titulo));
	}

	public ResponseEntity<MensagemResponse> deletarBlog(Long id, Usuario usuarioLogado) {
		Blog blog = blogRepository.findById(id).orElseThrow(() -> new NaoEncontradoException("Blog não encontrado"));

		if (!blog.getIdUsuario().getId().equals(usuarioLogado.getId())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body(new MensagemResponse("Você não tem permissão para deletar este blog"));
		}

		blogRepository.delete(blog);
		return ResponseEntity.ok(new MensagemResponse("Blog deletado com sucesso"));
	}

	public ResponseEntity<Blog> atualizarBlog(Long id, BlogDTO dto, Usuario usuarioLogado) {
		Blog blog = blogRepository.findById(id).orElseThrow(() -> new NaoEncontradoException("Blog não encontrado"));

		if (!blog.getIdUsuario().getId().equals(usuarioLogado.getId())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}

		blog.setTituloMateria(dto.getTituloMateria());
		blog.setInformacao(dto.getInformacao());
		blog.setUrlNoticia(dto.getUrlNoticia());
		blog.setBairro(dto.getBairro());
		blog.setAnonima(dto.isAnonima());
		blog.setDataPostagem(LocalDateTime.now());

		blogRepository.save(blog);
		return ResponseEntity.ok(blog);
	}
	
	// Parte de validação
	public ResponseEntity<Blog> aprovarBlog(Long id) {
	    Blog blog = blogRepository.findById(id)
	            .orElseThrow(() -> new NaoEncontradoException("Blog não encontrado"));

	    blog.setStatus(StatusPublicacao.APROVADO);
	    blogRepository.save(blog);

	    return ResponseEntity.ok(blog);
	}
	
	public List<Blog> listarAprovados() {
	    return blogRepository.findByStatus(StatusPublicacao.APROVADO);
	}
	
	public List<Blog> listarPendentes() {
		return blogRepository.findByStatus(StatusPublicacao.PENDENTE);
	}
	
	public ResponseEntity<MensagemResponse> negarBlog(Long id) {
	    Blog blog = blogRepository.findById(id)
	            .orElseThrow(() -> new NaoEncontradoException("Blog não encontrado"));

	    blogRepository.delete(blog);
	    return ResponseEntity.ok(new MensagemResponse("Blog negado e excluído com sucesso"));
	}
}