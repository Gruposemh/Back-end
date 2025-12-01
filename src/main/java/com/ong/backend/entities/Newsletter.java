package com.ong.backend.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_newsletter")
public class Newsletter {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    private String nome;
    
    @Column(nullable = false)
    private Boolean ativo = true;
    
    @Column(name = "data_inscricao")
    private LocalDateTime dataInscricao;
    
    @Column(name = "token_confirmacao")
    private String tokenConfirmacao;
    
    @Column(name = "email_confirmado")
    private Boolean emailConfirmado = false;
    
    @Column(name = "data_confirmacao")
    private LocalDateTime dataConfirmacao;
    
    public Newsletter() {
        this.dataInscricao = LocalDateTime.now();
    }
    
    public Newsletter(String email) {
        this.email = email;
        this.dataInscricao = LocalDateTime.now();
    }
    
    public Newsletter(String email, String nome) {
        this.email = email;
        this.nome = nome;
        this.dataInscricao = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public LocalDateTime getDataInscricao() {
        return dataInscricao;
    }

    public void setDataInscricao(LocalDateTime dataInscricao) {
        this.dataInscricao = dataInscricao;
    }

    public String getTokenConfirmacao() {
        return tokenConfirmacao;
    }

    public void setTokenConfirmacao(String tokenConfirmacao) {
        this.tokenConfirmacao = tokenConfirmacao;
    }

    public Boolean getEmailConfirmado() {
        return emailConfirmado;
    }

    public void setEmailConfirmado(Boolean emailConfirmado) {
        this.emailConfirmado = emailConfirmado;
    }

    public LocalDateTime getDataConfirmacao() {
        return dataConfirmacao;
    }

    public void setDataConfirmacao(LocalDateTime dataConfirmacao) {
        this.dataConfirmacao = dataConfirmacao;
    }
}
