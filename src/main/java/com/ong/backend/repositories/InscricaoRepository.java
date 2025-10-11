package com.ong.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ong.backend.entities.Curso;
import com.ong.backend.entities.Inscricao;
import com.ong.backend.entities.Usuario;

@Repository
public interface InscricaoRepository extends JpaRepository <Inscricao, Long>{

	boolean existsByIdUsuarioAndIdCurso(Usuario usuario, Curso curso);
}
