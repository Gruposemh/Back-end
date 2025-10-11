package com.ong.backend.repositories;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ong.backend.entities.OTPToken;

@Repository
public interface OTPTokenRepository extends JpaRepository<OTPToken, Long> {

    Optional<OTPToken> findByEmailAndOtpCodeAndUsedFalse(String email, String otpCode);

    @Query("SELECT ot FROM OTPToken ot WHERE ot.email = :email AND ot.used = false ORDER BY ot.createdAt DESC")
    Optional<OTPToken> findLatestByEmail(@Param("email") String email);

    @Modifying
    @Query("DELETE FROM OTPToken ot WHERE ot.expiryDate < :now")
    void deleteExpiredTokens(@Param("now") LocalDateTime now);

    @Modifying
    @Query("DELETE FROM OTPToken ot WHERE ot.email = :email")
    void deleteByEmail(@Param("email") String email);

    @Query("SELECT COUNT(ot) FROM OTPToken ot WHERE ot.email = :email AND ot.createdAt > :since")
    long countByEmailAndCreatedAtAfter(@Param("email") String email, @Param("since") LocalDateTime since);

    boolean existsByEmailAndOtpCodeAndUsedFalse(String email, String otpCode);
}
