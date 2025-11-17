package com.ong.backend.entities;

import java.time.LocalTime;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "tb_atividade")
public class Atividade {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	private String descricao;
	private String dias;
	
	@JsonFormat(pattern = "HH:mm:ss")
	private LocalTime horario;
	
	private int vagas;
	
	@Lob
	@Column(columnDefinition = "LONGTEXT")
	private String imagem;
	
	@OneToMany(mappedBy = "idAtividade", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private List<Inscricao> inscricoes;
	
	public Atividade() {
	}
	
	public Atividade(Long id, String nome, String descricao, String dias, LocalTime horario, int vagas,
			String imagem, List<Inscricao> inscricoes) {
		this.id = id;
		this.nome = nome;
		this.descricao = descricao;
		this.dias = dias;
		this.horario = horario;
		this.vagas = vagas;
		this.imagem = imagem;
		this.inscricoes = inscricoes;
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

	public List<Inscricao> getInscricoes() {
		return inscricoes;
	}
	public void setInscricoes(List<Inscricao> inscricoes) {
		this.inscricoes = inscricoes;
	}
}

