package com.ong.backend.dto;

import java.time.LocalDateTime;

import com.ong.backend.entities.Comentario;

public class ComentarioDTO {
	
	private Long id;
	private Long idUsuario;
	private Long idBlog;
	private LocalDateTime dataComentario;
	private String comentario;
	
	public ComentarioDTO() {
	}

	public ComentarioDTO(Long id, Long idUsuario, Long idBlog, LocalDateTime dataComentario, String comentario) {
		this.id = id;
		this.idUsuario = idUsuario;
		this.idBlog = idBlog;
		this.dataComentario = dataComentario;
		this.comentario = comentario;
	}
	
	public ComentarioDTO(Comentario entity) {
		this.id = entity.getId();
		this.idUsuario = entity.getIdUsuario().getId();
		this.idBlog = entity.getIdBlog().getId();
		this.dataComentario = entity.getDataComentario();
		this.comentario = entity.getComentario();
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

	public void setNomeUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

	public Long getIdBlog() {
		return idBlog;
	}

	public void setIdBlog(Long idBlog) {
		this.idBlog = idBlog;
	}

	public LocalDateTime getDataComentario() {
		return dataComentario;
	}

	public void setDataComentario(LocalDateTime dataComentario) {
		this.dataComentario = dataComentario;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
}