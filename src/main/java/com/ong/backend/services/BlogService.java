package com.ong.backend.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ong.backend.dto.BlogDTO;
import com.ong.backend.entities.Blog;
import com.ong.backend.entities.Usuario;
import com.ong.backend.exceptions.BlogDuplicadoException;
import com.ong.backend.exceptions.BlogNaoEncontradoException;
import com.ong.backend.exceptions.UsuarioNaoEncontradoException;
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
	        throw new BlogDuplicadoException("Já existe um blog com esse título.");
	    }

	    Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
	        .orElseThrow(() -> new UsuarioNaoEncontradoException("Este usuário não existe!"));

	    Blog blog = new Blog();
	    blog.setTituloMateria(dto.getTituloMateria());
	    blog.setBairro(dto.getBairro());
	    blog.setInformacao(dto.getInformacao());
	    blog.setUrlNoticia(dto.getUrlNoticia());
	    blog.setAnonima(dto.isAnonima());
	    blog.setDataPostagem(LocalDateTime.now());
	    blog.setIdUsuario(usuario);

	    blog = blogRepository.save(blog);
	    return ResponseEntity.ok(blog);
	}
	
	public List<Blog> listar(){
		return blogRepository.findAll();
	}
	
	 public ResponseEntity<Optional<Blog>> buscarPorTitulo(String titulo) {
	        Optional<Blog> blog = blogRepository.findByTituloMateria(titulo);
	        return ResponseEntity.ok(blog);
	    }

	
	 public ResponseEntity<Void> deletarBlog(String tituloMateria) {
		    Optional<Blog> blogOpt = blogRepository.findByTituloMateria(tituloMateria);
		    if (blogOpt.isEmpty()) {
		        throw new BlogNaoEncontradoException("Blog com título '" + tituloMateria + "' não encontrado.");
		    }
		    blogRepository.delete(blogOpt.get());
		    return ResponseEntity.noContent().build();
		}

	
	 public ResponseEntity<Blog> atualizarBlog(String tituloMateria, BlogDTO dto) {
		    Optional<Blog> blogOpt = blogRepository.findByTituloMateria(tituloMateria);
		    if (blogOpt.isEmpty()) {
		        throw new BlogNaoEncontradoException("Blog com título '" + tituloMateria + "' não encontrado.");
		    }

		    Blog blog = blogOpt.get();
		    blog.setTituloMateria(dto.getTituloMateria());
		    blog.setInformacao(dto.getInformacao());
		    blog.setUrlNoticia(dto.getUrlNoticia());
		    blog.setBairro(dto.getBairro());
		    blog.setAnonima(dto.isAnonima());
		    blog.setDataPostagem(LocalDateTime.now());

		    blog = blogRepository.save(blog);
		    return ResponseEntity.ok(blog);
		}
}