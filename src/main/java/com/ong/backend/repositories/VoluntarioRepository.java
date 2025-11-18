package com.ong.backend.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ong.backend.entities.StatusVoluntario;
import com.ong.backend.entities.Voluntario;

public interface VoluntarioRepository extends JpaRepository<Voluntario, Long> {
    List<Voluntario> findByStatus(StatusVoluntario status);
    List<Voluntario> findByStatusAndDataCancelamentoBefore(StatusVoluntario status, LocalDateTime data);
    boolean existsByIdUsuario_IdAndStatus(Long id, StatusVoluntario status);
    Optional<Voluntario> findByIdUsuarioId(Long idUsuario);
}

