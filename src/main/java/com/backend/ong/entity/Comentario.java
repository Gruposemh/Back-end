package com.backend.ong.entity;

import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_comentario")
public class Comentario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "id_usuario")
	private Usuario idUsuario;
	
	@ManyToOne
	@JoinColumn(name = "id_blog")
	private Blog idBlog;
	
	private LocalDateTime dataComentario;
	
	public Comentario() {
		this.dataComentario = LocalDateTime.now();
	}
	
	public Comentario(Long id, Usuario idUsuario, Blog idBlog, LocalDateTime dataComentario) {
		this.id = id;
		this.idUsuario = idUsuario;
		this.idBlog = idBlog;
		this.dataComentario = dataComentario;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Usuario getIdUsuario() {
		return idUsuario;
	}
	
	public void setIdUsuario(Usuario idUsuario) {
		this.idUsuario = idUsuario;
	}
	
	public Blog getIdBlog() {
		return idBlog;
	}
	
	public void setIdBlog(Blog idBlog) {
		this.idBlog = idBlog;
	}
	
	public LocalDateTime getDataComentario() {
		return dataComentario;
	}
	
	public void setDataComentario(LocalDateTime dataComentario) {
		this.dataComentario = dataComentario;
	}
}