package com.ong.backend.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_usuarios")
public class Usuario implements UserDetails{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	@Column(unique= true)
	private String email;
	private String senha;
	@Enumerated(EnumType.STRING)
    private StatusRole role;
	
	@OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Doacao> doacoes;
	
	@OneToMany(mappedBy = "idUsuario", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Blog> blogs;
	
	@OneToMany(mappedBy = "idUsuario", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Inscricao> inscricoes;
	
	@OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ParticipacaoEvento> participacoes = new ArrayList<>();
	
	public Usuario() {
		
	}
	
	public Usuario(Long id, String nome, String email, String senha, StatusRole role) {
		this.id = id;
		this.nome = nome;
		this.email = email;
		this.senha = senha;
		this.role = role;
	}

	public Usuario(String nome, String email, String senha, StatusRole role) {
		this.nome = nome;
		this.email = email;
		this.senha = senha;
		this.role = role;
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

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
	}

	@Override
	public String getPassword() {
		return senha;
	}

	@Override
	public String getUsername() {
		return email;
	}	
}