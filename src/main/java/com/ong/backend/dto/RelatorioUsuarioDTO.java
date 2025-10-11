package com.ong.backend.dto;
import com.ong.backend.entities.Usuario;

public class RelatorioUsuarioDTO {
	private Long id;
	private String nome;
	private String email;
    private String role;
    
	public RelatorioUsuarioDTO() {
	}
	
	public RelatorioUsuarioDTO(Long id, String nome, String email, String role) {
		this.id = id;
		this.nome = nome;
		this.email = email;
		this.role = role;
	}

	public RelatorioUsuarioDTO(Usuario entity) {
		this.id = entity.getId();
		this.nome = entity.getNome();
		this.email = entity.getEmail();
		this.role = entity.getRole().name();
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
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
}