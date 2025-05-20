package com.backend.ong.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_doacao")
public class Doacao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Usuario idUsuario;
	private String tipoDoacao;
	private float valor;
	
	public Doacao() {
	}
	
	public Doacao(Long id, Usuario idUsuario, String tipoDoacao, float valor) {
		this.id = id;
		this.idUsuario = idUsuario;
		this.tipoDoacao = tipoDoacao;
		this.valor = valor;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Usuario getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(Usuario idUsuario) {
		this.idUsuario = idUsuario;
	}
	public String getTipoDoacao() {
		return tipoDoacao;
	}
	public void setTipoDoacao(String tipoDoacao) {
		this.tipoDoacao = tipoDoacao;
	}
	public float getValor() {
		return valor;
	}
	public void setValor(float valor) {
		this.valor = valor;
	}
}
