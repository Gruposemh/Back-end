package com.ong.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ong.backend.entities.StatusVoluntario;
import com.ong.backend.entities.Voluntario;

@Repository
public interface VoluntarioRepository extends JpaRepository <Voluntario, Long>{

	List<Voluntario> findByStatus(StatusVoluntario status);
}
