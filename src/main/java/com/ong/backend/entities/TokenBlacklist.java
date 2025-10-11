package com.ong.backend.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_token_blacklist")
public class TokenBlacklist {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String tokenHash;
    
    @Column(nullable = false)
    private LocalDateTime expiresAt;
    
    @Column(nullable = false)
    private LocalDateTime blacklistedAt;
    
    @Column(nullable = false)
    private String email;
    
    public TokenBlacklist() {
    }
    
    public TokenBlacklist(String tokenHash, LocalDateTime expiresAt, String email) {
        this.tokenHash = tokenHash;
        this.expiresAt = expiresAt;
        this.email = email;
        this.blacklistedAt = LocalDateTime.now();
    }
    
    // Getters e Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTokenHash() {
        return tokenHash;
    }
    
    public void setTokenHash(String tokenHash) {
        this.tokenHash = tokenHash;
    }
    
    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }
    
    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
    
    public LocalDateTime getBlacklistedAt() {
        return blacklistedAt;
    }
    
    public void setBlacklistedAt(LocalDateTime blacklistedAt) {
        this.blacklistedAt = blacklistedAt;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }
}
