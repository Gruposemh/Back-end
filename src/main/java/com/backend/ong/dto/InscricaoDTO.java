package com.backend.ong.dto;

import java.time.LocalDateTime;
import com.backend.ong.entity.Inscricao;
import com.backend.ong.entity.StatusDoPagamento;

public class InscricaoDTO {

    private Long id;
    private Long idUsuario;
    private Long idCurso;
    private StatusDoPagamento status;
    private LocalDateTime dataInscricao;

    public InscricaoDTO() {
    }

    public InscricaoDTO(Long id, Long idUsuario, Long idCurso, StatusDoPagamento status, LocalDateTime dataInscricao) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idCurso = idCurso;
        this.status = status;
        this.dataInscricao = dataInscricao;
    }

    public InscricaoDTO(Inscricao inscricao) {
        id = inscricao.getId();
        idUsuario = inscricao.getIdUsuario();
        idCurso = inscricao.getIdCurso();
        status = inscricao.getStatus();
        dataInscricao = inscricao.getDataInscricao();
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

    public Long getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(Long idCurso) {
        this.idCurso = idCurso;
    }

    public StatusDoPagamento getStatus() {
        return status;
    }

    public void setStatus(StatusDoPagamento status) {
        this.status = status;
    }

    public LocalDateTime getDataInscricao() {
        return dataInscricao;
    }

    public void setDataInscricao(LocalDateTime dataInscricao) {
        this.dataInscricao = dataInscricao;
    }

}
