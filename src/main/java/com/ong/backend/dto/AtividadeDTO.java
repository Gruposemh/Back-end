package com.ong.backend.dto;

import java.time.LocalTime;
import com.ong.backend.entities.Atividade;

public class AtividadeDTO {
	private Long id;
	private String nome;
	private String descricao;
	private String dias;
	private LocalTime horario;
	private int vagas;
	private String imagem;
	
	public AtividadeDTO() {
	}
	
	public AtividadeDTO(Long id, String nome, String descricao, String dias, LocalTime horario, int vagas, String imagem) {
		this.id = id;
		this.nome = nome;
		this.descricao = descricao;
		this.dias = dias;
		this.horario = horario;
		this.vagas = vagas;
		this.imagem = imagem;
	}

	public AtividadeDTO(Atividade entity) {
		this.id = entity.getId();
		this.nome = entity.getNome();
		this.descricao = entity.getDescricao();
		this.dias = entity.getDias();
		this.horario = entity.getHorario();
		this.vagas = entity.getVagas();
		this.imagem = entity.getImagem();
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

	public String getDias() {
		return dias;
	}

	public void setDias(String dias) {
		this.dias = dias;
	}

	public LocalTime getHorario() {
		return horario;
	}

	public void setHorario(LocalTime horario) {
		this.horario = horario;
	}

	public int getVagas() {
		return vagas;
	}

	public void setVagas(int vagas) {
		this.vagas = vagas;
	}

	public String getImagem() {
		return imagem;
	}

	public void setImagem(String imagem) {
		this.imagem = imagem;
	}
}

