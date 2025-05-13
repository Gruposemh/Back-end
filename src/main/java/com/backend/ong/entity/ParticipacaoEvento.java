package com.backend.ong.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_participacao_evento")
public class ParticipacaoEvento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long idUsuario;
    private Long idEvento;
    private String tipoParticipacao;

    @ManyToMany
	@JoinTable(name = "tb_participacaoEvento", joinColumns = @JoinColumn(name = "participacaoEvento_id"), inverseJoinColumns = @JoinColumn(name = "Evento_id"))
	private Set<Evento> eventos = new HashSet<>();

    @ManyToOne
	@JoinColumn(name = "usuario_id")
	private Usuario usuario;

    public ParticipacaoEvento() {
    }

    public ParticipacaoEvento(Long id, Long idUsuario, Long idEvento, String tipoParticipacao) {
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

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Long getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(Long idEvento) {
        this.idEvento = idEvento;
    }

    public String getTipoParticipacao() {
        return tipoParticipacao;
    }

    public void setTipoParticipacao(String tipoParticipacao) {
        this.tipoParticipacao = tipoParticipacao;
    }

}
