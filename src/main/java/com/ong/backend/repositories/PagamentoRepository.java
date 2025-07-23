package com.ong.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ong.backend.entities.Inscricao;
import com.ong.backend.entities.Pagamento;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Long>{
	
	boolean existsByInscricao(Inscricao inscricao);

}
