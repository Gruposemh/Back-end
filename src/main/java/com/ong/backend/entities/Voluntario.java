package com.ong.backend.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_voluntario")
public class Voluntario {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "id_usuario")
    private Usuario idUsuario;
    private String cpf;
    private String telefone;
    private String dataNascimento;
    private String endereco;
    private LocalDateTime dataVoluntario;
    private StatusVoluntario status;
    private String descricao;
	
	public Voluntario() {
	}

	public Voluntario(Long id, Usuario idUsuario, String cpf, String telefone, String dataNascimento, String endereco,
			LocalDateTime dataVoluntario, String descricao, StatusVoluntario status) {
		this.id = id;
		this.idUsuario = idUsuario;
		this.cpf = cpf;
		this.telefone = telefone;
		this.dataNascimento = dataNascimento;
		this.endereco = endereco;
		this.dataVoluntario = dataVoluntario;
		this.descricao = descricao;
		this.status = status;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Usuario getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(Usuario idUsuario) {
		this.idUsuario = idUsuario;
	}
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	public String getTelefone() {
		return telefone;
	}
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	public String getDataNascimento() {
		return dataNascimento;
	}
	public void setDataNascimento(String dataNascimento) {
		this.dataNascimento = dataNascimento;
	}
	public String getEndereco() {
		return endereco;
	}
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
	public LocalDateTime getDataVoluntario() {
		return dataVoluntario;
	}
	public void setDataVoluntario(LocalDateTime dataVoluntario) {
		this.dataVoluntario = dataVoluntario;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public StatusVoluntario getStatus() {
		return status;
	}

	public void setStatus(StatusVoluntario status) {
		this.status = status;
	}
}