package com.ong.backend.dto;

import com.ong.backend.entities.ParticipacaoEvento;

public class ParticipacaoEventoDTO {

	private Long id;
	private Long idUsuario;
	private Long idEvento;
	private String tipoParticipacao;
	private Boolean confirmado;
	private String nomeEvento;
	private String dataEvento;
	private String imagemUrl;
	private String nomeUsuario;
	private String emailUsuario;
	
	public ParticipacaoEventoDTO() {
	}
	
	public ParticipacaoEventoDTO(Long id, Long idUsuario, Long idEvento, String tipoParticipacao, Boolean confirmado) {
		this.id = id;
		this.idUsuario = idUsuario;
		this.idEvento = idEvento;
		this.tipoParticipacao = tipoParticipacao;
		this.confirmado = confirmado;
	}

	public ParticipacaoEventoDTO(ParticipacaoEvento entity) {
		this.id = entity.getId();
		this.idUsuario = entity.getUsuario().getId();
		this.idEvento = entity.getEvento().getId();
		this.tipoParticipacao = entity.getTipoParticipacao();
		this.confirmado = entity.getConfirmado();
		this.nomeEvento = entity.getEvento().getNome();
		this.dataEvento = entity.getEvento().getData() != null ? entity.getEvento().getData().toString() : null;
		this.imagemUrl = entity.getEvento().getImagemUrl();
		this.nomeUsuario = entity.getUsuario().getNome();
		this.emailUsuario = entity.getUsuario().getEmail();
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
	public Boolean getConfirmado() {
		return confirmado;
	}
	public void setConfirmado(Boolean confirmado) {
		this.confirmado = confirmado;
	}
	public String getNomeEvento() {
		return nomeEvento;
	}
	public void setNomeEvento(String nomeEvento) {
		this.nomeEvento = nomeEvento;
	}
	public String getDataEvento() {
		return dataEvento;
	}
	public void setDataEvento(String dataEvento) {
		this.dataEvento = dataEvento;
	}
	public String getImagemUrl() {
		return imagemUrl;
	}
	public void setImagemUrl(String imagemUrl) {
		this.imagemUrl = imagemUrl;
	}
	public String getNomeUsuario() {
		return nomeUsuario;
	}
	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}
	public String getEmailUsuario() {
		return emailUsuario;
	}
	public void setEmailUsuario(String emailUsuario) {
		this.emailUsuario = emailUsuario;
	}
}
