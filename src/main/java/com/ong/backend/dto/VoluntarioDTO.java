package com.ong.backend.dto;

import java.sql.Date;

import com.ong.backend.entities.Voluntario;

public class VoluntarioDTO {

	private Long id;
	private Long idUsuario;
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
