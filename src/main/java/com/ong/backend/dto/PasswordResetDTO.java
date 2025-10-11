package com.ong.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PasswordResetDTO {
    
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Formato de email inválido")
    private String email;
    
    @NotBlank(message = "Token é obrigatório")
    @Size(min = 6, max = 6, message = "Token deve ter exatamente 6 dígitos")
    private String token;
    
    @NotBlank(message = "Nova senha é obrigatória")
    @Size(min = 6, message = "Senha deve ter pelo menos 6 caracteres")
    private String novaSenha;

    public PasswordResetDTO() {
    }

    public PasswordResetDTO(String email, String token, String novaSenha) {
        this.email = email;
        this.token = token;
        this.novaSenha = novaSenha;
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

    public String getNovaSenha() {
        return novaSenha;
    }

    public void setNovaSenha(String novaSenha) {
        this.novaSenha = novaSenha;
    }
}
