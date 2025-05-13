package com.backend.ong.entity;

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
	private Long comentario;
	private Long idUsuario;
	private Long idBlog;
	private Date dataComentario;
	
	public Comentario() {
	}

	public Comentario(Long comentario, Long idUsuario, Long idBlog, Date dataComentario) {
		this.comentario = comentario;
		this.idUsuario = idUsuario;
		this.idBlog = idBlog;
		this.dataComentario = dataComentario;
	}

	public Long getComentario() {
		return comentario;
	}

	public void setComentario(Long comentario) {
		this.comentario = comentario;
	}

	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

	public Long getIdBlog() {
		return idBlog;
	}

	public void setIdBlog(Long idBlog) {
		this.idBlog = idBlog;
	}

	public Date getDataComentario() {
		return dataComentario;
	}

	public void setDataComentario(Date dataComentario) {
		this.dataComentario = dataComentario;
	}
}
