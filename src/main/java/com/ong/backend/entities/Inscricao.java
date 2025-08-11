package com.ong.backend.entities;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_inscricao")
public class Inscricao {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
	@ManyToOne
	@JoinColumn(name = "id_usuario")
	private Usuario idUsuario;

	@ManyToOne
	@JoinColumn(name = "id_curso")
	private Curso idCurso;
	
	@OneToMany(mappedBy = "inscricao", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Pagamento> pagamentos;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_pagamento", length = 10, nullable = false)
    private StatusPagamento statusPagamento;
    
    private LocalDateTime dataInscricao;
	
	public Inscricao() {
	}

	public Inscricao(Long id, Usuario idUsuario, Curso idCurso, StatusPagamento statusPagamento, LocalDateTime dataInscricao) {
		this.id = id;
		this.idUsuario = idUsuario;
		this.idCurso = idCurso;
		this.statusPagamento = statusPagamento;
		this.dataInscricao = dataInscricao;
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
	public Curso getIdCurso() {
		return idCurso;
	}
	public void setIdCurso(Curso idCurso) {
		this.idCurso = idCurso;
	}
	public StatusPagamento getStatusPagamento() {
		return statusPagamento;
	}
	public void setStatusPagamento(StatusPagamento statusPagamento) {
		this.statusPagamento = statusPagamento;
	}
	public LocalDateTime getDataInscricao() {
		return dataInscricao;
	}
	public void setDataInscricao(LocalDateTime dataInscricao) {
		this.dataInscricao = dataInscricao;
	}
	
}