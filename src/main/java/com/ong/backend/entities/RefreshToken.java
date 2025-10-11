package com.ong.backend.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_refresh_tokens")
public class RefreshToken {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String token;
    
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    
    @Column(nullable = false)
    private LocalDateTime expiresAt;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @Column(nullable = false)
    private boolean revoked = false;
    
    @Column
    private String deviceInfo;
    
    @Column
    private String ipAddress;
    
    // Constructors
    public RefreshToken() {}
    
    public RefreshToken(String token, Usuario usuario, LocalDateTime expiresAt, String deviceInfo, String ipAddress) {
        this.token = token;
        this.usuario = usuario;
        this.expiresAt = expiresAt;
        this.createdAt = LocalDateTime.now();
        this.deviceInfo = deviceInfo;
        this.ipAddress = ipAddress;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public Usuario getUsuario() {
        return usuario;
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }
    
    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public boolean isRevoked() {
        return revoked;
    }
    
    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }
    
    public String getDeviceInfo() {
        return deviceInfo;
    }
    
    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }
    
    public String getIpAddress() {
        return ipAddress;
    }
    
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
    
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }
    
    public boolean isValid() {
        return !revoked && !isExpired();
    }
}
