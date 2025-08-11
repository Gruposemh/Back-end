package com.ong.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ong.backend.entities.Doacao;

@Repository
public interface DoacaoRepository extends JpaRepository <Doacao, Long>{

	// Listar doações por usuário
	List<Doacao> findByUsuarioId(Long idUsuario);
	
	// Contar doações por usuário
	Long countByUsuarioId(Long usuarioId);
}
