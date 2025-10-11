package com.ong.backend.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_notificacao_usuario")
public class NotificacaoUsuario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "usuario_id")
	private Usuario usuario;
	
	@ManyToOne
	@JoinColumn(name = "notificacao_id")
	private Notificacao notificacao;

	@Enumerated(EnumType.STRING)
	private StatusVisualizacao status = StatusVisualizacao.NAO_VISTO;
	
	public NotificacaoUsuario() {}

	public NotificacaoUsuario(Long id, Usuario usuario, Notificacao notificacao, StatusVisualizacao status) {
		this.id = id;
		this.usuario = usuario;
		this.notificacao = notificacao;
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Notificacao getNotificacao() {
		return notificacao;
	}

	public void setNotificacao(Notificacao notificacao) {
		this.notificacao = notificacao;
	}

	public StatusVisualizacao getStatus() {
		return status;
	}

	public void setStatus(StatusVisualizacao status) {
		this.status = status;
	}
}