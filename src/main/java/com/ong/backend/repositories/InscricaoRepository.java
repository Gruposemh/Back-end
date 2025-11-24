package com.ong.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ong.backend.entities.Curso;
import com.ong.backend.entities.Inscricao;
import com.ong.backend.entities.Usuario;

@Repository
public interface InscricaoRepository extends JpaRepository <Inscricao, Long>{

	boolean existsByIdUsuarioAndIdCurso(Usuario usuario, Curso curso);
	int countByIdCurso(Curso curso);
	List<Inscricao> findByIdCursoId(Long cursoId);
	List<Inscricao> findByIdUsuario_Id(Long usuarioId);
	List<Inscricao> findByIdUsuario(Usuario usuario);

}
