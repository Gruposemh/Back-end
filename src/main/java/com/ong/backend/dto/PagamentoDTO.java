package com.ong.backend.dto;

import java.time.LocalDateTime;

import com.ong.backend.entities.Pagamento;

public class PagamentoDTO {
	private Long id;
	private Long idInscricao;
	private float valorPago;
	private LocalDateTime dataPagamento;
	
	public PagamentoDTO() {
	}
	
	public PagamentoDTO(Long id, Long idInscricao, float valorPago, LocalDateTime dataPagamento) {
		this.id = id;
		this.idInscricao = idInscricao;
		this.valorPago = valorPago;
		this.dataPagamento = dataPagamento;
	}
	
	public PagamentoDTO(Pagamento entity) {
		this.id = entity.getId();
		this.idInscricao = entity.getInscricao().getId();
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
	public LocalDateTime getDataPagamento() {
		return dataPagamento;
	}
	public void setDataPagamento(LocalDateTime dataPagamento) {
		this.dataPagamento = dataPagamento;
	}
}	