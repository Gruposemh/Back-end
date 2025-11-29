package com.ong.backend.dto;

public class EditarPerfilDTO {
    private String nome;
    private String imagemPerfil;
    private String telefone; // Apenas para voluntários
    private String endereco; // Apenas para voluntários
    
    public EditarPerfilDTO() {
    }
    
    public EditarPerfilDTO(String nome, String imagemPerfil, String telefone, String endereco) {
        this.nome = nome;
        this.imagemPerfil = imagemPerfil;
        this.telefone = telefone;
        this.endereco = endereco;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getImagemPerfil() {
        return imagemPerfil;
    }
    
    public void setImagemPerfil(String imagemPerfil) {
        this.imagemPerfil = imagemPerfil;
    }
    
    public String getTelefone() {
        return telefone;
    }
    
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
    
    public String getEndereco() {
        return endereco;
    }
    
    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
}
