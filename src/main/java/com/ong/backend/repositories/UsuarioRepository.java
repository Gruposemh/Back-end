package com.ong.backend.repositories;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ong.backend.entities.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository <Usuario, Long>{
	
	Optional<Usuario> findByNome(String nome);
	Optional<Usuario> findByEmail(String email);
	Optional<Usuario> findByProviderIdAndProviderName(String providerId, String providerName);
	boolean existsByEmailAndProviderNameIsNotNull(String email);
	
	@Modifying
	@Query("UPDATE Usuario u SET u.emailVerificationToken = null, u.emailVerificationTokenExpiryDate = null WHERE u.emailVerificationTokenExpiryDate < :agora")
	void limparTokensVerificacaoExpirados(LocalDateTime agora);
	
	@Modifying
	@Query("UPDATE Usuario u SET u.passwordResetToken = null, u.passwordResetTokenExpiryDate = null WHERE u.passwordResetTokenExpiryDate < :agora")
	void limparTokensResetExpirados(LocalDateTime agora);
	
	@Modifying
	@Query("UPDATE Usuario u SET u.emailVerificationToken = null, u.emailVerificationTokenExpiryDate = null, u.passwordResetToken = null, u.passwordResetTokenExpiryDate = null WHERE u.emailVerificationTokenExpiryDate < :agora OR u.passwordResetTokenExpiryDate < :agora")
	void limparTokensExpirados(LocalDateTime agora);
}
