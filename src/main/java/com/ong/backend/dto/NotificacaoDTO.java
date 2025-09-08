package com.ong.backend.dto;

import com.ong.backend.entities.Notificacao;

public class NotificacaoDTO {
	private Long id;
	private String assunto;
	private String descricao;
	private String imgUrl;
	
	public NotificacaoDTO() {
	}

	public NotificacaoDTO(Long id, String assunto, String descricao, String imgUrl) {
		this.id = id;
		this.assunto = assunto;
		this.descricao = descricao;
		this.imgUrl = imgUrl;
	}

	public NotificacaoDTO(Notificacao entity) {
		this.id = entity.getId();
		this.assunto = entity.getAssunto();
		this.descricao = entity.getDescricao();
		this.imgUrl = entity.getImgUrl();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAssunto() {
		return assunto;
	}

	public void setAssunto(String assunto) {
		this.assunto = assunto;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
}