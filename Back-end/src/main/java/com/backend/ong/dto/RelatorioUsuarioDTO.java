package com.backend.ong.dto;

import java.time.format.DateTimeFormatter;

import com.backend.ong.entity.Usuario;

public class RelatorioUsuarioDTO {
	
	private Long usuario;
	private String nome;
	private String email;
	private String cadastro;
	
	public RelatorioUsuarioDTO(Usuario entity) {
		this.usuario = entity.getId();
		this.nome = entity.getNome();
		this.email = entity.getEmail();
		this.cadastro = entity.getDataCadastro().atZone(java.time.ZoneId.systemDefault())
				.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));;
	}
	
	public Long getUsuario() {
		return usuario;
	}
	public String getNome() {
		return nome;
	}
	public String getEmail() {
		return email;
	}
	public String getCadastro() {
		return cadastro;
	}	
}
