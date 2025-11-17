package com.ong.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ong.backend.entities.Atividade;

@Repository
public interface AtividadeRepository extends JpaRepository <Atividade, Long> {

	List<Atividade> findAllByNomeContainingIgnoreCase(String nome);

}

