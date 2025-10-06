package com.ong.backend.entities;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_tarefas")
public class Tarefas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeTarefa;

    private LocalDateTime dataTarefa;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    public Tarefas() {}

    public Tarefas(Usuario usuario, String nomeTarefa, LocalDateTime dataTarefa) {
        this.usuario = usuario;
        this.nomeTarefa = nomeTarefa;
        this.dataTarefa = dataTarefa;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNomeTarefa() {
        return nomeTarefa;
    }
    public void setNomeTarefa(String nomeTarefa) {
        this.nomeTarefa = nomeTarefa;
    }
    public LocalDateTime getDiaTarefa() {
        return dataTarefa;
    }
    public void setDiaTarefa(LocalDateTime dataTarefa) {
        this.dataTarefa = dataTarefa;
    }
    public Usuario getUsuario() {
        return usuario;
    }
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
