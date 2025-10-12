package com.ong.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ong.backend.entities.Tarefas;

@Repository
public interface TarefasRepository extends JpaRepository <Tarefas, Long>{

	List<Tarefas> findByUsuarioId(Long usuarioId);
}
