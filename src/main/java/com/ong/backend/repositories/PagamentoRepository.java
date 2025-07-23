package com.ong.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ong.backend.entities.Inscricao;
import com.ong.backend.entities.Pagamento;
import com.ong.backend.entities.Usuario;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Long>{
	
	boolean existsByInscricao(Inscricao inscricao);
	List<Pagamento> findByUsuario(Usuario usuario);

}
