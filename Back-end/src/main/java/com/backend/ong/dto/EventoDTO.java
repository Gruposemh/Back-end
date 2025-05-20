package com.backend.ong.dto;

import java.sql.Date;

import com.backend.ong.entity.Evento;

public class EventoDTO {
	
	private Long id;
	private String nome;
	private String descricao;
	private Date data;
	private String local;
	
	public EventoDTO() {
	}

	public EventoDTO(Long id, String nome, String descricao, Date data, String local) {
		this.id = id;
		this.nome = nome;
		this.descricao = descricao;
		this.data = data;
		this.local = local;
	}
	
	public EventoDTO(Evento entity) {
		this.id = entity.getId();
		this.nome = entity.getNome();
		this.descricao = entity.getDescricao();
		this.data = entity.getData();
		this.local = entity.getLocal();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}
}
