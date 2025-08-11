package com.ong.backend.dto;

import java.time.LocalDateTime;
import com.ong.backend.entities.Blog;

public class BlogDTO {
	private Long id;
	private Long idUsuario;
	private String tituloMateria;
	private String informacao;
	private String urlNoticia;
	private String bairro;
	private boolean anonima;
	private LocalDateTime dataPostagem;
	
	public BlogDTO() {
	}
	
	public BlogDTO(Long id, Long idUsuario, String tituloMateria, String informacao, String urlNoticia, String bairro, boolean anonima, LocalDateTime dataPostagem) {
		this.id = id;
		this.idUsuario = idUsuario;
		this.tituloMateria = tituloMateria;
		this.informacao = informacao;
		this.urlNoticia = urlNoticia;
		this.bairro = bairro;
		this.anonima = anonima;
		this.dataPostagem = dataPostagem;
	}

	public BlogDTO(Blog entity) {
		this.id = entity.getId();
		this.idUsuario = entity.getIdUsuario().getId();
		this.tituloMateria = entity.getTituloMateria();
		this.informacao = entity.getInformacao();
		this.urlNoticia = entity.getUrlNoticia();
		this.bairro = entity.getBairro();
		this.anonima = entity.isAnonima();
		this.dataPostagem = entity.getDataPostagem();
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTituloMateria() {
		return tituloMateria;
	}
	public void setTituloMateria(String tituloMateria) {
		this.tituloMateria = tituloMateria;
	}
	public String getInformacao() {
		return informacao;
	}
	public void setInformacao(String informacao) {
		this.informacao = informacao;
	}
	public String getUrlNoticia() {
		return urlNoticia;
	}
	public void setUrlNoticia(String urlNoticia) {
		this.urlNoticia = urlNoticia;
	}
	public String getBairro() {
		return bairro;
	}
	public void setBairro(String bairro) {
		this.bairro = bairro;
	}
	public boolean isAnonima() {
		return anonima;
	}
	public void setAnonima(boolean anonima) {
		this.anonima = anonima;
	}

	public LocalDateTime getDataPostagem() {
		return dataPostagem;
	}

	public void setDataPostagem(LocalDateTime dataPostagem) {
		this.dataPostagem = dataPostagem;
	}

	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}
}