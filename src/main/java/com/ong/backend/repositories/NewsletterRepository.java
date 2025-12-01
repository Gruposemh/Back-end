package com.ong.backend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ong.backend.entities.Newsletter;

@Repository
public interface NewsletterRepository extends JpaRepository<Newsletter, Long> {
    Optional<Newsletter> findByEmail(String email);
    List<Newsletter> findByAtivoTrue();
    List<Newsletter> findByEmailConfirmadoTrue();
    boolean existsByEmail(String email);
}
