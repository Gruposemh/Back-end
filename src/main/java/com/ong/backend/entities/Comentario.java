package com.ong.backend.entities;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_comentario")
public class Comentario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Usuario idUsuario;
	private Blog idBlog;
	private Date dataComentario;
	
	public Comentario() {
	}
	
	public Comentario(Long id, Usuario idUsuario, Blog idBlog, Date dataComentario) {
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
	public Date getDataComentario() {
		return dataComentario;
	}
	public void setDataComentario(Date dataComentario) {
		this.dataComentario = dataComentario;
	}
}