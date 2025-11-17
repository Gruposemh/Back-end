package com.ong.backend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ong.backend.entities.Atividade;
import com.ong.backend.entities.Inscricao;
import com.ong.backend.entities.Usuario;

@Repository
public interface InscricaoRepository extends JpaRepository <Inscricao, Long>{

	boolean existsByIdUsuarioAndIdAtividade(Usuario usuario, Atividade atividade);
	int countByIdAtividade(Atividade atividade);
	Optional<Inscricao> findByIdUsuarioAndIdAtividade(Usuario usuario, Atividade atividade);
}
