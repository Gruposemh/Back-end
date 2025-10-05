package com.ong.backend.entities;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "tb_comentario")
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String comentario;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario idUsuario;

    @ManyToOne
    @JoinColumn(name = "id_blog")
    @JsonBackReference
    private Blog idBlog;

    private LocalDateTime dataComentario;

    public Comentario() {}

    public Comentario(Long id, Usuario idUsuario, Blog idBlog, LocalDateTime dataComentario, String comentario) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idBlog = idBlog;
        this.dataComentario = dataComentario;
        this.comentario = comentario;
    }
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Usuario getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(Usuario idUsuario) {
		this.idUsuario = idUsuario;
	}
	public Blog getIdBlog() {
		return idBlog;
	}
	public void setIdBlog(Blog idBlog) {
		this.idBlog = idBlog;
	}
	public LocalDateTime getDataComentario() {
		return dataComentario;
	}
	public void setDataComentario(LocalDateTime dataComentario) {
		this.dataComentario = dataComentario;
	}
	public String getComentario() {
		return comentario;
	}
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
}