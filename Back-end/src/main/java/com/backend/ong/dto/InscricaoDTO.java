package com.backend.ong.dto;

import java.sql.Date;

import com.backend.ong.entity.Inscricao;
import com.backend.ong.entity.StatusPagamento;

public class InscricaoDTO {

	private Long id;
	private Long idUsuario;
	private Long idCurso;
	private StatusPagamento statusPagamento;
	private Date dataInscricao;
	
	public InscricaoDTO() {
	}
	
	public InscricaoDTO(Long id, Long idUsuario, Long idCurso, StatusPagamento statusPagamento, Date dataInscricao) {
		this.id = id;
		this.idUsuario = idUsuario;
		this.idCurso = idCurso;
		this.statusPagamento = statusPagamento;
		this.dataInscricao = dataInscricao;
	}

	public InscricaoDTO(Inscricao entity) {
		this.id = entity.getId();
		this.idUsuario = entity.getIdUsuario().getId();
		this.idCurso = entity.getIdCurso().getId();
		this.statusPagamento = entity.getStatusPagamento();
		this.dataInscricao = entity.getDataInscricao();
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
	public StatusPagamento getStatusPagamento() {
		return statusPagamento;
	}
	public void setStatusPagamento(StatusPagamento statusPagamento) {
		this.statusPagamento = statusPagamento;
	}
	public Date getDataInscricao() {
		return dataInscricao;
	}
	public void setDataInscricao(Date dataInscricao) {
		this.dataInscricao = dataInscricao;
	}
}
