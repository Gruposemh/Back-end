package com.backend.ong.dto;

import java.time.LocalDateTime;

import com.backend.ong.entity.Evento;

public class EventoDTO {

    private Long id;
    private Long idEvento;
    private String nome;
    private String descricao;
    private LocalDateTime data;
    private String local;

    public EventoDTO() {
    }
    public EventoDTO(Long id, Long idEvento, String nome, String descricao, LocalDateTime data, String local) {
        this.id = id;
        this.idEvento = idEvento;
        this.nome = nome;
        this.descricao = descricao;
        this.data = data;
        this.local = local;
    }

    public EventoDTO(Evento evento) {
        id = evento.getId();
        idEvento = evento.getIdEvento();
        nome = evento.getNome();
        descricao = evento.getDescricao();
        data = evento.getData();
        local = evento.getLocal();
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
