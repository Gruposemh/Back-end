package com.ong.backend.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ong.backend.entities.TokenType;
import com.ong.backend.entities.Usuario;
import com.ong.backend.entities.VerificationToken;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    Optional<VerificationToken> findByTokenAndTokenType(String token, TokenType tokenType);

    Optional<VerificationToken> findByUsuarioAndTokenTypeAndUsedFalse(Usuario usuario, TokenType tokenType);

    List<VerificationToken> findByUsuarioAndTokenType(Usuario usuario, TokenType tokenType);

    @Query("SELECT vt FROM VerificationToken vt WHERE vt.usuario.email = :email AND vt.tokenType = :tokenType AND vt.used = false ORDER BY vt.createdAt DESC")
    Optional<VerificationToken> findLatestByEmailAndTokenType(@Param("email") String email, @Param("tokenType") TokenType tokenType);

    @Modifying
    @Query("DELETE FROM VerificationToken vt WHERE vt.expiryDate < :now")
    void deleteExpiredTokens(@Param("now") LocalDateTime now);

    @Modifying
    @Query("DELETE FROM VerificationToken vt WHERE vt.usuario = :usuario AND vt.tokenType = :tokenType")
    void deleteByUsuarioAndTokenType(@Param("usuario") Usuario usuario, @Param("tokenType") TokenType tokenType);

    @Query("SELECT COUNT(vt) FROM VerificationToken vt WHERE vt.usuario.email = :email AND vt.tokenType = :tokenType AND vt.createdAt > :since")
    long countByEmailAndTokenTypeAndCreatedAtAfter(@Param("email") String email, @Param("tokenType") TokenType tokenType, @Param("since") LocalDateTime since);

    boolean existsByTokenAndTokenType(String token, TokenType tokenType);
}
