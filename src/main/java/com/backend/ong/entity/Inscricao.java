package com.backend.ong.entity;

import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_inscricao")
public class Inscricao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long idUsuario;
    private Long idCurso;
    private StatusDoPagamento status;
    private LocalDateTime dataInscricao;

    @ManyToOne
	@JoinColumn(name = "curso_id")
	private Curso curso;

    @OneToOne(mappedBy = "inscricao", cascade = CascadeType.ALL)
	private Pagamento pagamento;

    @ManyToOne
	@JoinColumn(name = "usuario_id")
	private Usuario usuario;

    public Inscricao() {
    }

    public Inscricao(Long id, Long idUsuario, Long idCurso, StatusDoPagamento status, LocalDateTime dataInscricao) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idCurso = idCurso;
        this.status = status;
        this.dataInscricao = dataInscricao;
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

    public Long getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(Long idCurso) {
        this.idCurso = idCurso;
    }

    public StatusDoPagamento getStatus() {
        return status;
    }

    public void setStatus(StatusDoPagamento status) {
        this.status = status;
    }

    public LocalDateTime getDataInscricao() {
        return dataInscricao;
    }

    public void setDataInscricao(LocalDateTime dataInscricao) {
        this.dataInscricao = dataInscricao;
    }

    

}
