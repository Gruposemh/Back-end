package com.ong.backend.dto;

import java.time.LocalDateTime;
import com.ong.backend.entities.Inscricao;

public class InscricaoDTO {

	private Long id;
	private Long idUsuario;
	private Long idAtividade;
	private LocalDateTime dataInscricao;
	
	public InscricaoDTO() {
	}
	
	public InscricaoDTO(Long id, Long idUsuario, Long idAtividade, LocalDateTime dataInscricao) {
		this.id = id;
		this.idUsuario = idUsuario;
		this.idAtividade = idAtividade;
		this.dataInscricao = dataInscricao;
	}

	public InscricaoDTO(Inscricao entity) {
		this.id = entity.getId();
		this.idUsuario = entity.getIdUsuario().getId();
		this.idAtividade = entity.getIdAtividade().getId();
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
	public Long getIdAtividade() {
		return idAtividade;
	}
	public void setIdAtividade(Long idAtividade) {
		this.idAtividade = idAtividade;
	}
	public LocalDateTime getDataInscricao() {
		return dataInscricao;
	}
	public void setDataInscricao(LocalDateTime dataInscricao) {
		this.dataInscricao = dataInscricao;
	}
}
