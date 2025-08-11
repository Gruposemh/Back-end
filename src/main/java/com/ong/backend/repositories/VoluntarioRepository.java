package com.ong.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ong.backend.entities.Voluntario;

@Repository
public interface VoluntarioRepository extends JpaRepository <Voluntario, Long>{

}
