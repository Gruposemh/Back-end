package com.ong.backend.dto;

import com.ong.backend.entities.NotificacaoUsuario;

public class NotificacaoUsuarioDTO {
	private Long id;
	private Long usuario;
	private Long notificacao;
	
	public NotificacaoUsuarioDTO() {
	}
	
	public NotificacaoUsuarioDTO(Long id, Long usuario, Long notificacao) {
		this.id = id;
		this.usuario = usuario;
		this.notificacao = notificacao;
	}

	public NotificacaoUsuarioDTO(NotificacaoUsuario entity) {
		this.id = entity.getId();
		this.usuario = entity.getUsuario().getId();
		this.notificacao = entity.getNotificacao().getId();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUsuario() {
		return usuario;
	}

	public void setUsuario(Long usuario) {
		this.usuario = usuario;
	}

	public Long getNotificacao() {
		return notificacao;
	}

	public void setNotificacao(Long notificacao) {
		this.notificacao = notificacao;
	}
}