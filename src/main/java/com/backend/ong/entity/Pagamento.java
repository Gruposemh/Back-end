package com.backend.ong.entity;

import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_pagamento")
public class Pagamento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "id_inscricao")
	private Inscricao idInscricao;
	
	private float valorPago;
	private LocalDateTime dataPagamento;
	
	public Pagamento() {
		this.dataPagamento = LocalDateTime.now();
	}

	public Pagamento(Long id, Inscricao idInscricao, float valorPago, LocalDateTime dataPagamento) {
		this.id = id;
		this.idInscricao = idInscricao;
		this.valorPago = valorPago;
		this.dataPagamento = dataPagamento;
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Inscricao getIdInscricao() {
		return idInscricao;
	}
	
	public void setIdInscricao(Inscricao idInscricao) {
		this.idInscricao = idInscricao;
	}
	
	public float getValorPago() {
		return valorPago;
	}
	
	public void setValorPago(float valorPago) {
		this.valorPago = valorPago;
	}
	
	public LocalDateTime getDataPagamento() {
		return dataPagamento;
	}
	
	public void setDataPagamento(LocalDateTime dataPagamento) {
		this.dataPagamento = dataPagamento;
	}
}