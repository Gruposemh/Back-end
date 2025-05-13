package com.backend.ong.dto;

import java.time.Instant;

import com.backend.ong.entity.Usuario;

public class UsuarioDTO {
	private Long id;
	private String nome;
	private String email;
	private String senha;
	private Instant dataCadastro;
	
	public UsuarioDTO() {
	}

	public UsuarioDTO(Long id, String nome, String email, String senha, Instant dataCadastro) {
		this.id = id;
		this.nome = nome;
		this.email = email;
		this.senha = senha;
		this.dataCadastro = dataCadastro;
	}
	
	public UsuarioDTO(Usuario entity) {
		this.id = entity.getId();
		this.nome = entity.getNome();
		this.email = entity.getEmail();
		this.senha = entity.getSenha();
		this.dataCadastro = entity.getDataCadastro();
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public Instant getDataCadastro() {
		return dataCadastro;
	}
	public void setDataCadastro(Instant dataCadastro) {
		this.dataCadastro = dataCadastro;
	}
}
