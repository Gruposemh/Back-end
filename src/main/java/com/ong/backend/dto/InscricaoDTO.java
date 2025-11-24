package com.ong.backend.dto;

import java.time.LocalDateTime;
import com.ong.backend.entities.Inscricao;

public class InscricaoDTO {

	private Long id;
	private Long idUsuario;
	private Long idCurso;
	private LocalDateTime dataInscricao;
	private String nomeCurso;
	private String descricaoCurso;
	private String diasCurso;
	private String horarioCurso;
	private String imagemCurso;
	private String nomeUsuario;
	private String emailUsuario;
	
	public InscricaoDTO() {
	}
	
	public InscricaoDTO(Long id, Long idUsuario, Long idCurso, LocalDateTime dataInscricao) {
		this.id = id;
		this.idUsuario = idUsuario;
		this.idCurso = idCurso;
		this.dataInscricao = dataInscricao;
	}

	public InscricaoDTO(Inscricao entity) {
		this.id = entity.getId();
		this.idUsuario = entity.getIdUsuario().getId();
		this.idCurso = entity.getIdCurso().getId();
		this.dataInscricao = entity.getDataInscricao();
		this.nomeCurso = entity.getIdCurso().getTitulo();
		this.descricaoCurso = entity.getIdCurso().getDescricao();
		this.diasCurso = entity.getIdCurso().getDias();
		this.horarioCurso = entity.getIdCurso().getHorario() != null ? 
			entity.getIdCurso().getHorario().toString() : null;
		this.imagemCurso = entity.getIdCurso().getImagem();
		this.nomeUsuario = entity.getIdUsuario().getNome();
		this.emailUsuario = entity.getIdUsuario().getEmail();
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
	public Long getIdCurso() {
		return idCurso;
	}
	public void setIdCurso(Long idCurso) {
		this.idCurso = idCurso;
	}
	public LocalDateTime getDataInscricao() {
		return dataInscricao;
	}
	public void setDataInscricao(LocalDateTime dataInscricao) {
		this.dataInscricao = dataInscricao;
	}
	public String getNomeCurso() {
		return nomeCurso;
	}
	public void setNomeCurso(String nomeCurso) {
		this.nomeCurso = nomeCurso;
	}
	public String getDescricaoCurso() {
		return descricaoCurso;
	}
	public void setDescricaoCurso(String descricaoCurso) {
		this.descricaoCurso = descricaoCurso;
	}
	public String getDiasCurso() {
		return diasCurso;
	}
	public void setDiasCurso(String diasCurso) {
		this.diasCurso = diasCurso;
	}
	public String getHorarioCurso() {
		return horarioCurso;
	}
	public void setHorarioCurso(String horarioCurso) {
		this.horarioCurso = horarioCurso;
	}
	public String getImagemCurso() {
		return imagemCurso;
	}
	public void setImagemCurso(String imagemCurso) {
		this.imagemCurso = imagemCurso;
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
