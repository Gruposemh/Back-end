package com.ong.backend.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_participacao_evento")
public class ParticipacaoEvento {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario idUsuario;
    
    @ManyToOne
    @JoinColumn(name = "id_evento")
    private Evento idEvento;
    
    private String tipoParticipacao;
	
	public ParticipacaoEvento() {
	}

	public ParticipacaoEvento(Long id, Usuario idUsuario, Evento idEvento, String tipoParticipacao) {
		this.id = id;
		this.idUsuario = idUsuario;
		this.idEvento = idEvento;
		this.tipoParticipacao = tipoParticipacao;
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
	public Evento getIdEvento() {
		return idEvento;
	}
	public void setIdEvento(Evento idEvento) {
		this.idEvento = idEvento;
	}
	public String getTipoParticipacao() {
		return tipoParticipacao;
	}
	public void setTipoParticipacao(String tipoParticipacao) {
		this.tipoParticipacao = tipoParticipacao;
	}
}