package com.backend.ong.dto;

import java.time.LocalDateTime;

import com.backend.ong.entity.Pagamento;

public class PagamentoDTO {

    private Long id;
    private Long idInscricao;
    private Float valorPago;
    private LocalDateTime dataPagamento;

    public PagamentoDTO() {
    }

    public PagamentoDTO(Long id, Long idInscricao, Float valorPago, LocalDateTime dataPagamento) {
        this.id = id;
        this.idInscricao = idInscricao;
        this.valorPago = valorPago;
        this.dataPagamento = dataPagamento;
    }

    public PagamentoDTO(Pagamento pagamento) {
        id = pagamento.getId();
        idInscricao = pagamento.getIdInscricao();
        valorPago = pagamento.getValorPago();
        dataPagamento = pagamento.getDataPagamento();
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

    public Float getValorPago() {
        return valorPago;
    }

    public void setValorPago(Float valorPago) {
        this.valorPago = valorPago;
    }

    public LocalDateTime getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDateTime dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

}
