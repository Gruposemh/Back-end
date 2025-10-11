package com.ong.backend.dto;

public class SocialLoginDTO {
    private String provider; // "google" ou "facebook"
    private String token;    // Token do provedor
    private String providerId; // ID do usuário no provedor (para Facebook)
    private String email;    // Email (para Facebook)
    private String nome;     // Nome (para Facebook)

    public SocialLoginDTO() {
    }

    public SocialLoginDTO(String provider, String token) {
        this.provider = provider;
        this.token = token;
    }

    // Getters e Setters
    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
