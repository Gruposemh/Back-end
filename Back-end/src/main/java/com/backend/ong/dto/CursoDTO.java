package com.backend.ong.dto;

import com.backend.ong.entity.Curso;

public class CursoDTO {

	private Long id;
	private String titulo;
	private String descricao;
	private float valor;
	
	public CursoDTO() {
	}
	
	public CursoDTO(Long id, String titulo, String descricao, float valor) {
		this.id = id;
		this.titulo = titulo;
		this.descricao = descricao;
		this.valor = valor;
	}
	
	public CursoDTO(Curso entity) {
		this.id = entity.getId();
		this.titulo = entity.getTitulo();
		this.descricao = entity.getDescricao();
		this.valor = entity.getValor();
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public float getValor() {
		return valor;
	}
	public void setValor(float valor) {
		this.valor = valor;
	}
}
