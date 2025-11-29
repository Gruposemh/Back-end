package com.ong.backend.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.ong.backend.entities.Evento;

public class EventoDTO {
	
	private Long id;
	private String nome;
	private String descricao;
	private LocalDate data;
	private String local;
	private String imagemUrl;
	private LocalTime hora;
	
	public EventoDTO() {
	}

	public EventoDTO(Long id, String nome, String descricao, LocalDate data, String local, String imagemUrl,
			LocalTime hora) {
		this.id = id;
		this.nome = nome;
		this.descricao = descricao;
		this.data = data;
		this.local = local;
		this.imagemUrl = imagemUrl;
		this.hora = hora;
	}

	public EventoDTO(Evento entity) {
		this.id = entity.getId();
		this.nome = entity.getNome();
		this.descricao = entity.getDescricao();
		this.data = entity.getData();
		this.local = entity.getLocal();
		this.imagemUrl = entity.getImagemUrl();
		this.hora = entity.getHora();
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

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public String getImagemUrl() {
		return imagemUrl;
	}

	public void setImagemUrl(String imagemUrl) {
		this.imagemUrl = imagemUrl;
	}

	public LocalTime getHora() {
		return hora;
	}

	public void setHora(LocalTime hora) {
		this.hora = hora;
	}
}