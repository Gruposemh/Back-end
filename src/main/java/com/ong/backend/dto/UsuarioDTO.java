package com.ong.backend.dto;

import com.ong.backend.entities.StatusRole;
import com.ong.backend.entities.Usuario;

public class UsuarioDTO {
	private Long id;
	private String nome;
	private String email;
	private String senha;
	private StatusRole role;
	
	public UsuarioDTO() {
	}

	public UsuarioDTO(Long id, String nome, String email, String senha, StatusRole role) {
		this.id = id;
		this.nome = nome;
		this.email = email;
		this.senha = senha;
		this.role = role;
	}
	
	public UsuarioDTO(Usuario entity) {
		this.id = entity.getId();
		this.nome = entity.getNome();
		this.email = entity.getEmail();
		this.senha = entity.getSenha();
		this.role = entity.getRole();
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
	public StatusRole getRole() {
		return role;
	}
	public void setRole(StatusRole role) {
		this.role = role;
	}
}
