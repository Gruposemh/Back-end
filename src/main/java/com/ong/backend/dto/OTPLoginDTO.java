package com.ong.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class OTPLoginDTO {
    
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ter formato válido")
    private String email;
    
    @NotBlank(message = "Token OTP é obrigatório")
    @Pattern(regexp = "\\d{6}", message = "Token deve conter exatamente 6 dígitos")
    private String token;

    public OTPLoginDTO() {
    }

    public OTPLoginDTO(String email, String token) {
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
