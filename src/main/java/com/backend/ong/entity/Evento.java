package com.backend.ong.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_doacao")
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long idEvento;
    private String nome;
    private String descricao;
    private LocalDateTime data;
    private String local;

    @ManyToMany(mappedBy = "evento")
	private Set<ParticipacaoEvento> ParticipacaoEventos = new HashSet<>();

    public Evento() {

    }

    public Evento(Long id, Long idEvento, String nome, String descricao, LocalDateTime data, String local) {
        this.id = id;
        this.idEvento = idEvento;
        this.nome = nome;
        this.descricao = descricao;
        this.data = data;
        this.local = local;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getIdEvento() {
        return idEvento;
    }
    public void setIdEvento(Long idEvento) {
        this.idEvento = idEvento;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    public LocalDateTime getData() {
        return data;
    }
    public void setData(LocalDateTime data) {
        this.data = data;
    }
    public String getLocal() {
        return local;
    }
    public void setLocal(String local) {
        this.local = local;
    }

}
