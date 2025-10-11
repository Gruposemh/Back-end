package com.ong.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ong.backend.entities.Curso;

@Repository
public interface CursoRepository extends JpaRepository <Curso, Long> {

	List<Curso> findAllByTituloContainingIgnoreCase(String titulo);

}
