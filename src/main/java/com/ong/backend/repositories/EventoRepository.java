package com.ong.backend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ong.backend.entities.Evento;

@Repository
public interface EventoRepository extends JpaRepository <Evento, Long>{

	Optional<Evento> findByNome(String nome);
}
