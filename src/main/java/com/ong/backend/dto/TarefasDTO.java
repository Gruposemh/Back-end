package com.ong.backend.dto;

import java.time.LocalDateTime;

import com.ong.backend.entities.Tarefas;

public class TarefasDTO {
	private Long usuarioId;
	private String nomeTarefa;
	private LocalDateTime dataTarefa;
	
	public TarefasDTO() {
	}

	public TarefasDTO(Long usuarioId, String nomeTarefa, LocalDateTime dataTarefa) {
		this.usuarioId = usuarioId;
		this.nomeTarefa = nomeTarefa;
		this.dataTarefa = dataTarefa;
	}

	public TarefasDTO(Tarefas entity) {
		this.usuarioId = entity.getUsuario().getId();
		this.nomeTarefa = entity.getNomeTarefa();
		this.dataTarefa = entity.getDiaTarefa();
	}

	public Long getUsuarioId() {
		return usuarioId;
	}
	public void setUsuarioId(Long usuarioId) {
		this.usuarioId = usuarioId;
	}
	public String getNomeTarefa() {
		return nomeTarefa;
	}
	public void setNomeTarefa(String nomeTarefa) {
		this.nomeTarefa = nomeTarefa;
	}
	public LocalDateTime getDataTarefa() {
		return dataTarefa;
	}
	public void setDataTarefa(LocalDateTime dataTarefa) {
		this.dataTarefa = dataTarefa;
	}
}
