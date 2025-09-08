package com.ong.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ong.backend.entities.Notificacao;

@Repository
public interface NotificacaoRepository extends JpaRepository <Notificacao, Long>{

}