package com.ong.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ong.backend.entities.NotificacaoUsuario;

@Repository
public interface NotificacaoUsuarioRepository extends JpaRepository <NotificacaoUsuario , Long>{
	List<NotificacaoUsuario> findByUsuarioId(Long usuarioId);
}