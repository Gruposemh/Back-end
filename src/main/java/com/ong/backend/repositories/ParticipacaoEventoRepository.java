package com.ong.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ong.backend.entities.Evento;
import com.ong.backend.entities.ParticipacaoEvento;
import com.ong.backend.entities.Usuario;

@Repository
public interface ParticipacaoEventoRepository extends JpaRepository <ParticipacaoEvento, Long>{

	boolean existsByUsuarioAndEvento(Usuario usuario, Evento evento);
	List<ParticipacaoEvento> findByUsuario(Usuario usuario);
}
