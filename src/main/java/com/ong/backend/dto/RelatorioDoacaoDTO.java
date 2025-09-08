package com.ong.backend.dto;

import java.time.LocalDateTime;

import com.ong.backend.entities.Doacao;

public class RelatorioDoacaoDTO {
	private Long id;
    private String usuarioNome;
    private String tipoDoacao;
    private Float valor;
    private LocalDateTime dataDoacao;
    
    public RelatorioDoacaoDTO() {
	}

	public RelatorioDoacaoDTO(Long id, String usuarioNome, String tipoDoacao, Float valor, LocalDateTime dataDoacao) {
		this.id = id;
		this.usuarioNome = usuarioNome;
		this.tipoDoacao = tipoDoacao;
		this.valor = valor;
		this.dataDoacao = dataDoacao;
	}
	
	public RelatorioDoacaoDTO(Doacao entity) {
        this.id = entity.getId();
        this.usuarioNome = entity.getUsuario().getNome();
        this.tipoDoacao = entity.getTipoDoacao();
        this.valor = entity.getValor();
        this.dataDoacao = entity.getDataDoacao();
    }
	 
	 public Long getId() {
		 return id;
	 }
	 public void setId(Long id) {
		 this.id = id;
	 }
	 
	 public String getUsuarioNome() {
		return usuarioNome;
	}

	 public void setUsuarioNome(String usuarioNome) {
		 this.usuarioNome = usuarioNome;
	 }

	 public void setValor(Float valor) {
		 this.valor = valor;
	 }

	 public String getTipoDoacao() {
		 return tipoDoacao;
	 }
	 public void setTipoDoacao(String tipoDoacao) {
		 this.tipoDoacao = tipoDoacao;
	 }
	 public float getValor() {
		 return valor;
	 }
	 public void setValor(float valor) {
		 this.valor = valor;
	 }
	 public LocalDateTime getDataDoacao() {
		 return dataDoacao;
	 }
	 public void setDataDoacao(LocalDateTime dataDoacao) {
		 this.dataDoacao = dataDoacao;
	 }
}
