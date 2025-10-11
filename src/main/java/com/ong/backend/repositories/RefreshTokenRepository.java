package com.ong.backend.repositories;

import com.ong.backend.entities.RefreshToken;
import com.ong.backend.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    
    Optional<RefreshToken> findByToken(String token);
    
    List<RefreshToken> findByUsuario(Usuario usuario);
    
    List<RefreshToken> findByUsuarioAndRevokedFalse(Usuario usuario);
    
    @Modifying
    @Transactional
    @Query("UPDATE RefreshToken rt SET rt.revoked = true WHERE rt.usuario = :usuario")
    void revokeAllByUsuario(Usuario usuario);
    
    @Modifying
    @Transactional
    @Query("UPDATE RefreshToken rt SET rt.revoked = true WHERE rt.token = :token")
    void revokeByToken(String token);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM RefreshToken rt WHERE rt.expiresAt < :now")
    void deleteExpiredTokens(LocalDateTime now);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM RefreshToken rt WHERE rt.usuario = :usuario AND rt.revoked = true")
    void deleteRevokedTokensByUsuario(Usuario usuario);
    
    @Query("SELECT COUNT(rt) FROM RefreshToken rt WHERE rt.usuario = :usuario AND rt.revoked = false")
    long countActiveTokensByUsuario(Usuario usuario);
}
