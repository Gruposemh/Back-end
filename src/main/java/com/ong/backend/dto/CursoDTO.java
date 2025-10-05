package com.ong.backend.dto;

import java.sql.Date;

import com.ong.backend.entities.Curso;

public class CursoDTO {
	private Long id;
	private String tituloCurso;
	private String descricao;
	private Date dia;
	
	public CursoDTO() {
	}
	
	public CursoDTO(Long id, String tituloCurso, String descricao, Date dia) {
		this.id = id;
		this.tituloCurso = tituloCurso;
		this.descricao = descricao;
		this.dia = dia;
	}
	
	public CursoDTO(Curso entity) {
		this.id = entity.getId();
		this.tituloCurso = entity.getTitulo();
		this.descricao = entity.getDescricao();
		this.dia = entity.getDia();
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
	public Date getDia() {
		return dia;
	}
	public void setDia(Date dia) {
		this.dia = dia;
	}
}