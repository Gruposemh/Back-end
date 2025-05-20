package com.backend.ong.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.ong.entity.Curso;

@Repository
public interface CursoRepository extends JpaRepository <Curso, Long>{

}
