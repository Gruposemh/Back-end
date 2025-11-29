package com.ong.backend.dto;

public class CancelarVoluntarioDTO {
    private String codigo;
    
    public CancelarVoluntarioDTO() {
    }
    
    public CancelarVoluntarioDTO(String codigo) {
        this.codigo = codigo;
    }
    
    public String getCodigo() {
        return codigo;
    }
    
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}
