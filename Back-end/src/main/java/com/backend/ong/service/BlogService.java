package com.backend.ong.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.backend.ong.dto.BlogDTO;
import com.backend.ong.entity.Blog;
import com.backend.ong.repositories.BlogRepository;

@Service
public class BlogService {
	
	@Autowired
	BlogRepository blogRepository;

	public ResponseEntity<Blog> cadastrarBlog(BlogDTO dto) {
		Blog blog = new Blog(dto);
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
		blog.setTituloMateria(atualizado.getTituloMateria());
		blog.setNoticia(atualizado.getNoticia());
		blog.setUrlNoticia(atualizado.getUrlNoticia());
		blog.setBairro(atualizado.getBairro());
		blog.setAnonima(atualizado.isAnonima());
		return blogRepository.save(blog);
	}
}
