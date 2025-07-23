package com.ong.backend.dto;

import com.ong.backend.entities.ParticipacaoEvento;

public class ParticipacaoEventoDTO {

	private Long id;
	private Long idUsuario;
	private Long idEvento;
	private String tipoParticipacao;
	
	public ParticipacaoEventoDTO() {
	}
	
	public ParticipacaoEventoDTO(Long id, Long idUsuario, Long idEvento, String tipoParticipacao) {
		this.id = id;
		this.idUsuario = idUsuario;
		this.idEvento = idEvento;
		this.tipoParticipacao = tipoParticipacao;
	}

	public ParticipacaoEventoDTO(ParticipacaoEvento entity) {
		this.id = entity.getId();
		this.idUsuario = entity.getUsuario().getId();
		this.idEvento = entity.getEvento().getId();
		this.tipoParticipacao = entity.getTipoParticipacao();
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
	public Long getIdEvento() {
		return idEvento;
	}
	public void setIdEvento(Long idEvento) {
		this.idEvento = idEvento;
	}
	public String getTipoParticipacao() {
		return tipoParticipacao;
	}
	public void setTipoParticipacao(String tipoParticipacao) {
		this.tipoParticipacao = tipoParticipacao;
	}
}
