package com.backend.ong.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Voluntario extends JpaRepository <Voluntario, Long>{

}
