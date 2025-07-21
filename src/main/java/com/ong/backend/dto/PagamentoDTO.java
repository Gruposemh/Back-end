package com.ong.backend.dto;

import java.sql.Date;

import com.ong.backend.entities.Pagamento;

public class PagamentoDTO {
	private Long id;
	private Long idInscricao;
	private float valorPago;
	private Date dataPagamento;
	
	public PagamentoDTO() {
	}
	
	public PagamentoDTO(Long id, Long idInscricao, float valorPago, Date dataPagamento) {
		this.id = id;
		this.idInscricao = idInscricao;
		this.valorPago = valorPago;
		this.dataPagamento = dataPagamento;
	}
	
	public PagamentoDTO(Pagamento entity) {
		this.id = entity.getId();
		this.idInscricao = entity.getIdInscricao().getId();
		this.valorPago = entity.getValorPago();
		this.dataPagamento = entity.getDataPagamento();
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getIdInscricao() {
		return idInscricao;
	}
	public void setIdInscricao(Long idInscricao) {
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