package com.ong.backend.entities;

<<<<<<< HEAD
import java.time.LocalDateTime;
=======
<<<<<<< HEAD
import java.sql.Date;
=======
import java.time.LocalDateTime;
>>>>>>> f6080b2 (feat: Adicionando relacionamento entre Usuario e Voluntario)
>>>>>>> 82557ff52ad45f527bf6d73463fe64dc6bfdf6fe

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
<<<<<<< HEAD
import jakarta.persistence.OneToOne;
=======
<<<<<<< HEAD
import jakarta.persistence.ManyToOne;
=======
import jakarta.persistence.OneToOne;
>>>>>>> f6080b2 (feat: Adicionando relacionamento entre Usuario e Voluntario)
>>>>>>> 82557ff52ad45f527bf6d73463fe64dc6bfdf6fe
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_voluntario")
public class Voluntario {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
<<<<<<< HEAD
    
	@OneToOne
	@JoinColumn(name = "id_usuario")
    private Usuario idUsuario;
=======
<<<<<<< HEAD
    
    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario idUsuario;
    
    private String tipoAjuda;
    private Date dataVoluntario;
	
	public Voluntario() {
	}
	
	public Voluntario(Long id, Usuario idUsuario, String tipoAjuda, Date dataVoluntario) {
		this.id = id;
		this.idUsuario = idUsuario;
		this.tipoAjuda = tipoAjuda;
		this.dataVoluntario = dataVoluntario;
=======

    @OneToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id")
    private Usuario usuario;

>>>>>>> 82557ff52ad45f527bf6d73463fe64dc6bfdf6fe
    private int cpf;
    private String telefone;
    private String dataNascimento;
    private String endereco;
    private LocalDateTime dataVoluntario;
    private String descricao;
	
	public Voluntario() {
	}

<<<<<<< HEAD
	public Voluntario(Long id, Usuario idUsuario, int cpf, String telefone, String dataNascimento, String endereco,
			LocalDateTime dataVoluntario, String descricao) {
		this.id = id;
		this.idUsuario = idUsuario;
=======
	public Voluntario(Long id, int cpf, String telefone, String dataNascimento, String endereco,
			LocalDateTime dataVoluntario, String descricao) {
		this.id = id;
>>>>>>> 82557ff52ad45f527bf6d73463fe64dc6bfdf6fe
		this.cpf = cpf;
		this.telefone = telefone;
		this.dataNascimento = dataNascimento;
		this.endereco = endereco;
		this.dataVoluntario = dataVoluntario;
		this.descricao = descricao;
<<<<<<< HEAD
=======
>>>>>>> f6080b2 (feat: Adicionando relacionamento entre Usuario e Voluntario)
>>>>>>> 82557ff52ad45f527bf6d73463fe64dc6bfdf6fe
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
<<<<<<< HEAD
=======
<<<<<<< HEAD
>>>>>>> 82557ff52ad45f527bf6d73463fe64dc6bfdf6fe
	public Usuario getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(Usuario idUsuario) {
		this.idUsuario = idUsuario;
	}
<<<<<<< HEAD
=======
	public String getTipoAjuda() {
		return tipoAjuda;
	}
	public void setTipoAjuda(String tipoAjuda) {
		this.tipoAjuda = tipoAjuda;
	}
	public Date getDataVoluntario() {
		return dataVoluntario;
	}
	public void setDataVoluntario(Date dataVoluntario) {
		this.dataVoluntario = dataVoluntario;
	}
=======
>>>>>>> 82557ff52ad45f527bf6d73463fe64dc6bfdf6fe
	public int getCpf() {
		return cpf;
	}
	public void setCpf(int cpf) {
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
<<<<<<< HEAD
=======
    public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
>>>>>>> f6080b2 (feat: Adicionando relacionamento entre Usuario e Voluntario)
>>>>>>> 82557ff52ad45f527bf6d73463fe64dc6bfdf6fe
}
