package com.backend.ong.dto;

import java.time.LocalDateTime;

import com.backend.ong.entity.Doacao;

public class DoacaoDTO {

    private Long id;
    private Long idUsuario;
    private String tipoDoacao;
    private float valor;
    private LocalDateTime dataDoacao;

    public DoacaoDTO() {
    }

    public DoacaoDTO(Long id, Long idUsuario, String tipoDoacao, float valor, LocalDateTime dataDoacao) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.tipoDoacao = tipoDoacao;
        this.valor = valor;
        this.dataDoacao = dataDoacao;
    }

    public DoacaoDTO(Doacao doacao) {
        id = doacao.getId();
        idUsuario = doacao.getIdUsuario();
        tipoDoacao = doacao.getTipoDoacao();
        valor = doacao.getValor();
        dataDoacao = doacao.getDataDoacao();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
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

    public LocalDateTime getDataDoacao() {
        return dataDoacao;
    }

    public void setDataDoacao(LocalDateTime dataDoacao) {
        this.dataDoacao = dataDoacao;
    }

}
