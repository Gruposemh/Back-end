package com.backend.ong.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.backend.ong.entity.Doacao;

@Repository
public interface DoacaoDTO extends JpaRepository <Doacao, Long>{

}
