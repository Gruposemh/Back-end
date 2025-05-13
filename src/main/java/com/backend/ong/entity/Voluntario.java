package com.backend.ong.entity;

import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_doacao")
public class Voluntario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long idUsuario;
    private String tipoAjuda;
    private LocalDateTime dataVoluntario;

    @ManyToOne
	@JoinColumn(name = "usuario_id")
	private Usuario usuario;

    public Voluntario() {
    }

    public Voluntario(Long id, Long idUsuario, String tipoAjuda, LocalDateTime dataVoluntario) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.tipoAjuda = tipoAjuda;
        this.dataVoluntario = dataVoluntario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getTipoAjuda() {
        return tipoAjuda;
    }

    public void setTipoAjuda(String tipoAjuda) {
        this.tipoAjuda = tipoAjuda;
    }

    public LocalDateTime getDataVoluntario() {
        return dataVoluntario;
    }

    public void setDataVoluntario(LocalDateTime dataVoluntario) {
        this.dataVoluntario = dataVoluntario;
    }

}
