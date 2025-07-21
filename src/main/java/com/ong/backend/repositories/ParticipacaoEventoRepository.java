package com.ong.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ong.backend.entities.ParticipacaoEvento;

@Repository
public interface ParticipacaoEventoRepository extends JpaRepository <ParticipacaoEvento, Long>{

}
