package com.backend.ong.dto;

import java.time.LocalDateTime;
import com.backend.ong.entity.Voluntario;

public class VoluntarioDTO {

	private Long id;
	private Long idUsuario;
	private String tipoAjuda;
	private LocalDateTime dataVoluntario;
	
	public VoluntarioDTO() {
	}
	
	public VoluntarioDTO(Long id, Long idUsuario, String tipoAjuda, LocalDateTime dataVoluntario) {
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

	// Getters e Setters
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
	
	public LocalDateTime getDataVoluntario() {
		return dataVoluntario;
	}
	
	public void setDataVoluntario(LocalDateTime dataVoluntario) {
		this.dataVoluntario = dataVoluntario;
	}
}