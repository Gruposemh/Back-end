package com.ong.backend.entities;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_voluntario")
public class Voluntario {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
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
}
