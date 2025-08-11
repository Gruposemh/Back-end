package com.ong.backend.dto;

import java.time.LocalDateTime;

import com.ong.backend.entities.Inscricao;
import com.ong.backend.entities.StatusPagamento;

public class InscricaoDTO {

	private Long id;
	private Long idUsuario;
	private Long idCurso;
	private StatusPagamento statusPagamento;
	private LocalDateTime dataInscricao;
	
	public InscricaoDTO() {
	}
	
	public InscricaoDTO(Long id, Long idUsuario, Long idCurso, StatusPagamento statusPagamento, LocalDateTime dataInscricao) {
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
	public LocalDateTime getDataInscricao() {
		return dataInscricao;
	}
	public void setDataInscricao(LocalDateTime dataInscricao) {
		this.dataInscricao = dataInscricao;
	}
}
