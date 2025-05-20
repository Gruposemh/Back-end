package com.backend.ong.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.backend.ong.entity.Pagamento;

@Repository
public interface PagamentoRepository extends JpaRepository <Pagamento, Long>{

}
