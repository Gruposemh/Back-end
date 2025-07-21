package com.ong.backend.dto;

import java.sql.Date;

import com.ong.backend.entities.Comentario;

public class ComentarioDTO {
	
	private Long id;
	private Long idUsuario;
	private Long idBlog;
	private Date dataComentario;
	
	public ComentarioDTO() {
	}

	public ComentarioDTO(Long id, Long idUsuario, Long idBlog, Date dataComentario) {
		this.id = id;
		this.idUsuario = idUsuario;
		this.idBlog = idBlog;
		this.dataComentario = dataComentario;
	}
	
	public ComentarioDTO(Comentario entity) {
		this.id = entity.getId();
		this.idUsuario = entity.getIdUsuario().getId();
		this.idBlog = entity.getIdBlog().getId();
		this.dataComentario = entity.getDataComentario();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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