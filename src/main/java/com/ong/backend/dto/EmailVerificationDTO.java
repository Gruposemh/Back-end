package com.ong.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class EmailVerificationDTO {
    
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Formato de email inválido")
    private String email;
    
    @NotBlank(message = "Token é obrigatório")
    @Size(min = 6, max = 6, message = "Token deve ter exatamente 6 dígitos")
    private String token;

    public EmailVerificationDTO() {
    }

    public EmailVerificationDTO(String email, String token) {
        this.email = email;
        this.token = token;
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
}
