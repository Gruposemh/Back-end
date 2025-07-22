package com.ong.backend.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_blog")
public class Blog {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String tituloMateria;
	private String informacao;
	private String urlNoticia;
	private String bairro;
	private boolean anonima;
	private LocalDateTime dataPostagem;
	
	@ManyToOne
    @JoinColumn(name = "id_usuario")
	private Usuario idUsuario;
	
	public Blog() {
	}

	public Blog(Long id, String tituloMateria, String informacao, String urlNoticia, String bairro, boolean anonima, LocalDateTime dataPostagem, Usuario idUsuario) {
		this.id = id;
		this.tituloMateria = tituloMateria;
		this.informacao = informacao;
		this.urlNoticia = urlNoticia;
		this.bairro = bairro;
		this.anonima = anonima;
		this.dataPostagem = dataPostagem;
		this.idUsuario = idUsuario;
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

	public void setInformacao(String noticia) {
		this.informacao = noticia;
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

	public Usuario getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Usuario idUsuario) {
		this.idUsuario = idUsuario;
	}
}