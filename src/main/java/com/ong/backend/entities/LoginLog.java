package com.ong.backend.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_login_logs")
public class LoginLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    
    @Column(nullable = false)
    private String ipAddress;
    
    @Column(nullable = false)
    private String userAgent;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoginStatus status;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoginType type;
    
    @Column(nullable = false)
    private LocalDateTime timestamp;
    
    private String failureReason;
    
    public LoginLog() {
    }
    
    public LoginLog(Usuario usuario, String ipAddress, String userAgent, LoginStatus status, LoginType type) {
        this.usuario = usuario;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.status = status;
        this.type = type;
        this.timestamp = LocalDateTime.now();
    }
    
    // Getters e Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Usuario getUsuario() {
        return usuario;
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public String getIpAddress() {
        return ipAddress;
    }
    
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
    
    public String getUserAgent() {
        return userAgent;
    }
    
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
    
    public LoginStatus getStatus() {
        return status;
    }
    
    public void setStatus(LoginStatus status) {
        this.status = status;
    }
    
    public LoginType getType() {
        return type;
    }
    
    public void setType(LoginType type) {
        this.type = type;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    public String getFailureReason() {
        return failureReason;
    }
    
    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }
    
    public enum LoginStatus {
        SUCCESS, FAILED, BLOCKED
    }
    
    public enum LoginType {
        TRADICIONAL, SOCIAL, OTP, PASSWORD_RESET
    }
}
