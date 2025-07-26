package com.backend.ong.dto;

import java.time.LocalDateTime;
import com.backend.ong.entity.Blog;

public class BlogDTO {
	private Long id;
	private Long idUsuario;
	private String titulo;
	private String descricao;
	private LocalDateTime dataPostagem;
	
	public BlogDTO() {
	}
	
	public BlogDTO(Long id, Long idUsuario, String titulo, String descricao, LocalDateTime dataPostagem) {
		this.id = id;
		this.idUsuario = idUsuario;
		this.titulo = titulo;
		this.descricao = descricao;
		this.dataPostagem = dataPostagem;
	}

	public BlogDTO(Blog entity) {
		this.id = entity.getId();
		this.idUsuario = entity.getIdUsuario() != null ? entity.getIdUsuario().getId() : null;
		this.titulo = entity.getTitulo();
		this.descricao = entity.getDescricao();
		this.dataPostagem = entity.getDataPostagem();
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
	
	public String getTitulo() {
		return titulo;
	}
	
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public LocalDateTime getDataPostagem() {
		return dataPostagem;
	}
	
	public void setDataPostagem(LocalDateTime dataPostagem) {
		this.dataPostagem = dataPostagem;
	}
}