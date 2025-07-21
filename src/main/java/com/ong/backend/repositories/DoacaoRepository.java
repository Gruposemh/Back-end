package com.ong.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ong.backend.entities.Doacao;

@Repository
public interface DoacaoRepository extends JpaRepository <Doacao, Long>{

}
