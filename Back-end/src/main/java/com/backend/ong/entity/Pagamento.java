package com.backend.ong.entity;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_pagamento")
public class Pagamento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Inscricao idInscricao;
	private float valorPago;
	private Date dataPagamento;
	
	public Pagamento() {
	}

	public Pagamento(Long id, Inscricao idInscricao, float valorPago, Date dataPagamento) {
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
	public Date getDataPagamento() {
		return dataPagamento;
	}
	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}
}
