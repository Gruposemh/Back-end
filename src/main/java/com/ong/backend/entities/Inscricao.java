package com.ong.backend.entities;

import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_inscricao")
public class Inscricao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario idUsuario;

    @ManyToOne
    @JoinColumn(name = "id_atividade")
    private Atividade idAtividade;

    private LocalDateTime dataInscricao;

    public Inscricao() {
    }

    public Inscricao(Long id, Usuario idUsuario, Atividade idAtividade, LocalDateTime dataInscricao) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idAtividade = idAtividade;
        this.dataInscricao = dataInscricao;
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
	public Atividade getIdAtividade() {
		return idAtividade;
	}
	public void setIdAtividade(Atividade idAtividade) {
		this.idAtividade = idAtividade;
	}
	public LocalDateTime getDataInscricao() {
		return dataInscricao;
	}
	public void setDataInscricao(LocalDateTime dataInscricao) {
		this.dataInscricao = dataInscricao;
	}
	
}