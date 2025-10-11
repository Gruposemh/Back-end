package com.ong.backend.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class SetPasswordRequestDTO {

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ter um formato válido")
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 8, max = 100, message = "Senha deve ter entre 8 e 100 caracteres")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&].*$", 
             message = "Senha deve conter pelo menos: 1 letra minúscula, 1 maiúscula, 1 número e 1 caractere especial")
    private String senha;

    @NotBlank(message = "Confirmação de senha é obrigatória")
    private String confirmarSenha;

    public SetPasswordRequestDTO() {}

    public SetPasswordRequestDTO(String email, String senha, String confirmarSenha) {
        this.email = email;
        this.senha = senha;
        this.confirmarSenha = confirmarSenha;
    }

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getConfirmarSenha() {
        return confirmarSenha;
    }

    public void setConfirmarSenha(String confirmarSenha) {
        this.confirmarSenha = confirmarSenha;
    }

    public boolean senhasConferem() {
        return senha != null && senha.equals(confirmarSenha);
    }
}
