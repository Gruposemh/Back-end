package com.ong.backend.entities;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_curso")
public class Curso {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String titulo;
	private String descricao;
	private float valor;
	
	@OneToMany(mappedBy = "idCurso", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Inscricao> inscricoes;
	
	public Curso() {
	}
	
	public Curso(Long id, String titulo, String descricao, float valor) {
		this.id = id;
		this.titulo = titulo;
		this.descricao = descricao;
		this.valor = valor;
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