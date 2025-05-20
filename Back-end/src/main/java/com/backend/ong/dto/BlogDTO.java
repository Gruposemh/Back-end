package com.backend.ong.dto;

import com.backend.ong.entity.Blog;

public class BlogDTO {
	private Long id;
	private String tituloMateria;
	private String noticia;
	private String urlNoticia;
	private String bairro;
	private boolean anonima;
	
	public BlogDTO() {
	}
	
	public BlogDTO(Long id, String tituloMateria, String noticia, String urlNoticia, String bairro, boolean anonima) {
		this.id = id;
		this.tituloMateria = tituloMateria;
		this.noticia = noticia;
		this.urlNoticia = urlNoticia;
		this.bairro = bairro;
		this.anonima = anonima;
	}

	public BlogDTO(Blog entity) {
		this.id = entity.getId();
		this.tituloMateria = entity.getTituloMateria();
		this.noticia = entity.getNoticia();
		this.urlNoticia = entity.getUrlNoticia();
		this.bairro = entity.getBairro();
		this.anonima = entity.isAnonima();
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
	public String getNoticia() {
		return noticia;
	}
	public void setNoticia(String noticia) {
		this.noticia = noticia;
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
}
