package com.ong.backend.dto.auth;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthResponseDTO {

    private boolean success;
    private String message;
    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";
    private Long expiresIn;
    private UserInfoDTO user;

    public AuthResponseDTO() {}

    public AuthResponseDTO(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public AuthResponseDTO(boolean success, String message, String accessToken, String refreshToken, Long expiresIn, UserInfoDTO user) {
        this.success = success;
        this.message = message;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
        this.user = user;
    }

    // Static factory methods
    public static AuthResponseDTO success(String message) {
        return new AuthResponseDTO(true, message);
    }

    public static AuthResponseDTO error(String message) {
        return new AuthResponseDTO(false, message);
    }

    public static AuthResponseDTO loginSuccess(String accessToken, String refreshToken, Long expiresIn, UserInfoDTO user) {
        return new AuthResponseDTO(true, "Login realizado com sucesso", accessToken, refreshToken, expiresIn, user);
    }

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public UserInfoDTO getUser() {
        return user;
    }

    public void setUser(UserInfoDTO user) {
        this.user = user;
    }

    // Inner class for user info
    public static class UserInfoDTO {
        private Long id;
        private String nome;
        private String email;
        private String role;
        private boolean emailVerificado;

        public UserInfoDTO() {}

        public UserInfoDTO(Long id, String nome, String email, String role, boolean emailVerificado) {
            this.id = id;
            this.nome = nome;
            this.email = email;
            this.role = role;
            this.emailVerificado = emailVerificado;
        }

        // Getters and Setters
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public boolean isEmailVerificado() {
            return emailVerificado;
        }

        public void setEmailVerificado(boolean emailVerificado) {
            this.emailVerificado = emailVerificado;
        }
    }
}
