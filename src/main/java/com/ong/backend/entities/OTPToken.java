package com.ong.backend.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_otp_tokens")
public class OTPToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String otpCode;

    @Column(nullable = false)
    private LocalDateTime expiryDate;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private boolean used = false;

    private int attempts = 0;

    public OTPToken() {
        this.createdAt = LocalDateTime.now();
    }

    public OTPToken(String email, String otpCode, LocalDateTime expiryDate) {
        this.email = email;
        this.otpCode = otpCode;
        this.expiryDate = expiryDate;
        this.createdAt = LocalDateTime.now();
        this.used = false;
        this.attempts = 0;
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

    public String getOtpCode() {
        return otpCode;
    }

    public void setOtpCode(String otpCode) {
        this.otpCode = otpCode;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public void incrementAttempts() {
        this.attempts++;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiryDate);
    }

    public boolean isValid() {
        return !isUsed() && !isExpired() && attempts < 3;
    }

    public boolean canAttempt() {
        return !isUsed() && !isExpired() && attempts < 3;
    }
}
