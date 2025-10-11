package com.ong.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TokenCleanupService {

    @Autowired
    private VerificationTokenService verificationTokenService;

    @Autowired
    private OTPService otpService;

    @Autowired
    private TokenBlacklistService tokenBlacklistService;
    
    @Autowired
    private RefreshTokenService refreshTokenService;

    // Executa a cada 30 minutos
    @Scheduled(fixedRate = 1800000)
    @Transactional
    public void limparTokensExpirados() {
        try {
            // Limpar tokens de verificação de email e reset de senha
            verificationTokenService.limparTokensExpirados();
            
            // Limpar tokens OTP expirados
            otpService.limparTokensExpirados();
            
            // Limpar tokens blacklistados expirados
            tokenBlacklistService.clearExpiredTokens();
            
            // Limpar refresh tokens expirados
            refreshTokenService.cleanExpiredTokens();
            
            System.out.println("Limpeza de tokens expirados executada com sucesso");
        } catch (Exception e) {
            System.err.println("Erro ao limpar tokens expirados: " + e.getMessage());
        }
    }
}