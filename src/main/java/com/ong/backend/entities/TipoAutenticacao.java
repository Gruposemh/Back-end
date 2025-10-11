package com.ong.backend.entities;

public enum TipoAutenticacao {
    TRADICIONAL,    // Login com email/senha
    SOCIAL,         // Login via Google/Facebook
    PASSWORDLESS    // Login via OTP por email
}
