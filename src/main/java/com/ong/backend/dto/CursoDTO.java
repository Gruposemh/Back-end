package com.ong.backend.dto;

import com.ong.backend.entities.Curso;

public class CursoDTO {
	private Long id;
	private String tituloCurso;
	private String descricao;
	private float valor;
	
	public CursoDTO() {
	}
	
	public CursoDTO(Long id, String tituloCurso, String descricao, float valor) {
		this.id = id;
		this.tituloCurso = tituloCurso;
		this.descricao = descricao;
		this.valor = valor;
	}
	
	public CursoDTO(Curso entity) {
		this.id = entity.getId();
		this.tituloCurso = entity.getTitulo();
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
		return tituloCurso;
	}
	public void setTitulo(String tituloCurso) {
		this.tituloCurso = tituloCurso;
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