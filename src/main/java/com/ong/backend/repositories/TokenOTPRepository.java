package com.ong.backend.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ong.backend.entities.TokenOTP;

@Repository
public interface TokenOTPRepository extends JpaRepository<TokenOTP, Long> {
    
    Optional<TokenOTP> findByEmailAndTokenAndUsadoFalse(String email, String token);
    
    List<TokenOTP> findByEmailAndUsadoFalse(String email);
    
    @Modifying
    @Query("UPDATE TokenOTP t SET t.usado = true WHERE t.email = :email AND t.usado = false")
    void invalidarTokensAtivos(@Param("email") String email);
    
    @Modifying
    @Query("DELETE FROM TokenOTP t WHERE t.expiraEm < :agora")
    void limparTokensExpirados(@Param("agora") LocalDateTime agora);
}
