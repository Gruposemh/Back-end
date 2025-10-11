package com.ong.backend.repositories;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ong.backend.entities.TokenBlacklist;

@Repository
public interface TokenBlacklistRepository extends JpaRepository<TokenBlacklist, Long> {
    
    Optional<TokenBlacklist> findByTokenHash(String tokenHash);
    
    boolean existsByTokenHash(String tokenHash);
    
    @Modifying
    @Query("DELETE FROM TokenBlacklist t WHERE t.expiresAt < :agora")
    void limparTokensExpirados(LocalDateTime agora);
    
    @Modifying
    @Query("DELETE FROM TokenBlacklist t WHERE t.email = :email")
    void limparTokensPorEmail(String email);
}
