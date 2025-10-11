package com.ong.backend.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ong.backend.entities.LoginLog;
import com.ong.backend.entities.Usuario;

@Repository
public interface LoginLogRepository extends JpaRepository<LoginLog, Long> {
    
    List<LoginLog> findByUsuarioOrderByTimestampDesc(Usuario usuario);
    
    Page<LoginLog> findByUsuarioOrderByTimestampDesc(Usuario usuario, Pageable pageable);
    
    List<LoginLog> findByIpAddressOrderByTimestampDesc(String ipAddress);
    
    @Query("SELECT l FROM LoginLog l WHERE l.usuario = :usuario AND l.timestamp >= :since ORDER BY l.timestamp DESC")
    List<LoginLog> findRecentLoginsByUsuario(@Param("usuario") Usuario usuario, @Param("since") LocalDateTime since);
    
    @Query("SELECT COUNT(l) FROM LoginLog l WHERE l.usuario = :usuario AND l.status = 'FAILED' AND l.timestamp >= :since")
    long countFailedLoginsSince(@Param("usuario") Usuario usuario, @Param("since") LocalDateTime since);
    
    @Query("SELECT COUNT(l) FROM LoginLog l WHERE l.ipAddress = :ipAddress AND l.status = 'FAILED' AND l.timestamp >= :since")
    long countFailedLoginsByIpSince(@Param("ipAddress") String ipAddress, @Param("since") LocalDateTime since);
    
    @Query("SELECT l FROM LoginLog l WHERE l.usuario = :usuario AND l.status = 'SUCCESS' ORDER BY l.timestamp DESC")
    List<LoginLog> findLastSuccessfulLogins(@Param("usuario") Usuario usuario, Pageable pageable);
}
