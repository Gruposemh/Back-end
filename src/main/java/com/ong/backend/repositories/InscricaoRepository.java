package com.ong.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ong.backend.entities.Inscricao;

@Repository
public interface InscricaoRepository extends JpaRepository <Inscricao, Long>{

}
