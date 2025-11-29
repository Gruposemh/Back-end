package com.ong.backend.dto;

import java.time.LocalDateTime;

import com.ong.backend.entities.Comentario;

public class ComentarioDTO {
	
	private Long id;
	private Long idUsuario;
	private String nomeUsuario;        
	private String imagemPerfilUsuario; 
	private Long idBlog;
	private LocalDateTime dataComentario;
	private String comentario;
	
	public ComentarioDTO() {
	}
	
	public ComentarioDTO(Comentario entity) {
		this.id = entity.getId();
		this.idUsuario = entity.getIdUsuario().getId();
		this.nomeUsuario = entity.getIdUsuario().getNome();     
		this.imagemPerfilUsuario = entity.getIdUsuario().getImagemPerfil();
		this.idBlog = entity.getIdBlog().getId();
		this.dataComentario = entity.getDataComentario();
		this.comentario = entity.getComentario();
	}

	// Getters e Setters
	public String getNomeUsuario() {
		return nomeUsuario;
	}
	
	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}
	
	public String getImagemPerfilUsuario() {
		return imagemPerfilUsuario;
	}
	
	public void setImagemPerfilUsuario(String imagemPerfilUsuario) {
		this.imagemPerfilUsuario = imagemPerfilUsuario;
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