package com.ong.backend.dto;

import java.time.LocalDateTime;

import com.ong.backend.entities.Comentario;

public class ComentarioDTO {
	
	private Long id;
	private String nomeUsuario;
	private String tituloBlog;
	private LocalDateTime dataComentario;
	private String comentario;
	
	public ComentarioDTO() {
	}

	public ComentarioDTO(Long id, String nomeUsuario, String tituloBlog, LocalDateTime dataComentario, String comentario) {
		this.id = id;
		this.nomeUsuario = nomeUsuario;
		this.tituloBlog = tituloBlog;
		this.dataComentario = dataComentario;
		this.comentario = comentario;
	}
	
	public ComentarioDTO(Comentario entity) {
		this.id = entity.getId();
		this.nomeUsuario = entity.getIdUsuario().getNome();
		this.tituloBlog = entity.getIdBlog().getTituloMateria();
		this.dataComentario = entity.getDataComentario();
		this.comentario = entity.getComentario();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNomeUsuario() {
		return nomeUsuario;
	}

	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}

	public String getTituloBlog() {
		return tituloBlog;
	}

	public void setTituloBlog(String tituloBlog) {
		this.tituloBlog = tituloBlog;
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