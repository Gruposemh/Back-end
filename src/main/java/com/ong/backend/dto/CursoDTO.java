package com.ong.backend.dto;

import java.time.LocalTime;
import java.util.List;

import com.ong.backend.entities.Curso;

public class CursoDTO {
    private Long id;
    private String titulo;
    private String descricao;
    private String dias;
    private LocalTime horario;
    private int vagas;
    private List<InscricaoDTO> inscricoes;
    private Long idInscricao;

    public CursoDTO() {
    }

    public CursoDTO(Long id, String titulo, String descricao, String dias, LocalTime horario, int vagas) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.dias = dias;
        this.horario = horario;
        this.vagas = vagas;
    }

    public CursoDTO(Curso curso, Long idInscricao) {
        this.id = curso.getId();
        this.titulo = curso.getTitulo();
        this.descricao = curso.getDescricao();
        this.dias = curso.getDias();
        this.horario = curso.getHorario();
        this.vagas = curso.getVagas();
        this.idInscricao = idInscricao;
    }

    public CursoDTO(Curso entity) {
        this.id = entity.getId();
        this.titulo = entity.getTitulo();
        this.descricao = entity.getDescricao();
        this.dias = entity.getDias();
        this.horario = entity.getHorario();
        this.vagas = entity.getVagas();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getDias() { return dias; }
    public void setDias(String dias) { this.dias = dias; }

    public LocalTime getHorario() { return horario; }
    public void setHorario(LocalTime horario) { this.horario = horario; }

    public int getVagas() { return vagas; }
    public void setVagas(int vagas) { this.vagas = vagas; }

    public List<InscricaoDTO> getInscricoes() { return inscricoes; }
    public void setInscricoes(List<InscricaoDTO> inscricoes) { this.inscricoes = inscricoes; }

    public Long getIdInscricao() { return idInscricao; }
    public void setIdInscricao(Long idInscricao) { this.idInscricao = idInscricao; }
}
