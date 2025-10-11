package com.ong.backend.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class VerifyEmailRequestDTO {

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ter um formato válido")
    private String email;

    @NotBlank(message = "Código de verificação é obrigatório")
    @Pattern(regexp = "\\d{6}", message = "Código deve conter exatamente 6 dígitos")
    private String codigo;

    public VerifyEmailRequestDTO() {}

    public VerifyEmailRequestDTO(String email, String codigo) {
        this.email = email;
        this.codigo = codigo;
    }

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}
