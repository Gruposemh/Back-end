package com.backend.ong.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.backend.ong.dto.BlogDTO;
import com.backend.ong.entity.Blog;
import com.backend.ong.entity.Usuario;
import com.backend.ong.repositories.BlogRepository;
import com.backend.ong.repositories.UsuarioRepository;

@Service
public class BlogService {
	
	@Autowired
	BlogRepository blogRepository;
	
	@Autowired
	UsuarioRepository usuarioRepository;

	public ResponseEntity<Blog> cadastrarBlog(BlogDTO dto) {
		Blog blog = new Blog();
		
		// Buscar o usuário
		Optional<Usuario> usuario = usuarioRepository.findById(dto.getIdUsuario());
		if (!usuario.isPresent()) {
			return ResponseEntity.badRequest().build();
		}
		
		blog.setIdUsuario(usuario.get());
		blog.setTitulo(dto.getTitulo());
		blog.setDescricao(dto.getDescricao());
		blog.setDataPostagem(LocalDateTime.now());
		
		blog = blogRepository.save(blog);
		return ResponseEntity.ok(blog);
	}
	
	public List<Blog> listar(){
		return blogRepository.findAll();
	}
	
	public ResponseEntity<Blog> buscarId(Long id){
		Optional<Blog> blog = blogRepository.findById(id);
		return blog.map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
	}
	
	public String deleteBlog(Long id) {
		blogRepository.deleteById(id);
		return "Blog deletado!";
	}
	
	public Blog atualizarBlog(Long id, Blog atualizado) {
		Blog blog = blogRepository.findById(id).get();   
		blog.setTitulo(atualizado.getTitulo());
		blog.setDescricao(atualizado.getDescricao());
		return blogRepository.save(blog);
	}
}