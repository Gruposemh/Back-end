package com.ong.backend.dto;

<<<<<<< HEAD
import java.sql.Date;
=======
import java.time.LocalDateTime;
>>>>>>> f6080b2 (feat: Adicionando relacionamento entre Usuario e Voluntario)

import com.ong.backend.entities.Voluntario;

public class VoluntarioDTO {

	private Long id;
	private Long idUsuario;
<<<<<<< HEAD
	private String tipoAjuda;
	private Date dataVoluntario;
	
	public VoluntarioDTO() {
	}
	
	public VoluntarioDTO(Long id, Long idUsuario, String tipoAjuda, Date dataVoluntario) {
		this.id = id;
		this.idUsuario = idUsuario;
		this.tipoAjuda = tipoAjuda;
		this.dataVoluntario = dataVoluntario;
	}
	
	public VoluntarioDTO(Voluntario entity) {
		this.id = entity.getId();
		this.idUsuario = entity.getIdUsuario().getId();
		this.tipoAjuda = entity.getTipoAjuda();
		this.dataVoluntario = entity.getDataVoluntario();
=======
    private int cpf;
    private String telefone;
    private String dataNascimento;
    private String endereco;
    private LocalDateTime dataVoluntario;
    private String descricao;
	
	public VoluntarioDTO() {
	}

	public VoluntarioDTO(Long id, Long idUsuario, int cpf, String telefone, String dataNascimento, String endereco,
			LocalDateTime dataVoluntario, String descricao) {
		this.id = id;
		this.idUsuario = idUsuario;
		this.cpf = cpf;
		this.telefone = telefone;
		this.dataNascimento = dataNascimento;
		this.endereco = endereco;
		this.dataVoluntario = dataVoluntario;
		this.descricao = descricao;
	}

	public VoluntarioDTO(Voluntario entity) {
		this.id = entity.getId();
		this.cpf = entity.getCpf();
		this.idUsuario = entity.getUsuario().getId();
		this.telefone = entity.getTelefone();
		this.dataNascimento = entity.getDataNascimento();
		this.endereco = entity.getEndereco();
		this.dataVoluntario = entity.getDataVoluntario();
		this.descricao = entity.getDescricao();
>>>>>>> f6080b2 (feat: Adicionando relacionamento entre Usuario e Voluntario)
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}
<<<<<<< HEAD
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
>>>>>>> f6080b2 (feat: Adicionando relacionamento entre Usuario e Voluntario)
}
