package com.ong.backend.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_tokens_otp")
public class TokenOTP {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String token;
    
    @Column(nullable = false)
    private LocalDateTime criadoEm;
    
    @Column(nullable = false)
    private LocalDateTime expiraEm;
    
    private boolean usado = false;
    
    public TokenOTP() {
    }
    
    public TokenOTP(String email, String token, LocalDateTime expiraEm) {
        this.email = email;
        this.token = token;
        this.criadoEm = LocalDateTime.now();
        this.expiraEm = expiraEm;
    }
    
    // Getters e Setters
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
    
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }
    
    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }
    
    public LocalDateTime getExpiraEm() {
        return expiraEm;
    }
    
    public void setExpiraEm(LocalDateTime expiraEm) {
        this.expiraEm = expiraEm;
    }
    
    public boolean isUsado() {
        return usado;
    }
    
    public void setUsado(boolean usado) {
        this.usado = usado;
    }
    
    public boolean isExpirado() {
        return LocalDateTime.now().isAfter(expiraEm);
    }
    
    public boolean isValido() {
        return !usado && !isExpirado();
    }
}
