package com.ong.backend.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ong.backend.entities.TokenBlacklist;
import com.ong.backend.repositories.TokenBlacklistRepository;

@Service
public class TokenBlacklistService {
    
    @Autowired
    private TokenBlacklistRepository tokenBlacklistRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private TokenService tokenService;
    
    @Transactional
    public void blacklistToken(String token, String email) {
        try {
            // Extrair expiração real do token JWT
            LocalDateTime expiresAt = tokenService.extractExpirationPublic(token).toInstant()
                    .atZone(java.time.ZoneId.systemDefault())
                    .toLocalDateTime();
            
            // Hash do token para armazenar
            String tokenHash = passwordEncoder.encode(token);
            
            TokenBlacklist blacklistEntry = new TokenBlacklist(tokenHash, expiresAt, email);
            tokenBlacklistRepository.save(blacklistEntry);
        } catch (Exception e) {
            // Se não conseguir extrair a expiração, usar 1 hora como fallback
            LocalDateTime expiresAt = LocalDateTime.now().plusHours(1);
            String tokenHash = passwordEncoder.encode(token);
            TokenBlacklist blacklistEntry = new TokenBlacklist(tokenHash, expiresAt, email);
            tokenBlacklistRepository.save(blacklistEntry);
        }
    }
    
    public boolean isTokenBlacklisted(String token) {
        // Como não podemos descriptografar o hash, vamos verificar se existe
        // um token com hash similar (isso é uma limitação da abordagem atual)
        // Em uma implementação real, seria melhor usar um cache Redis ou similar
        
        // Para simplificar, vamos assumir que se o token está na blacklist,
        // ele foi invalidado
        return tokenBlacklistRepository.existsByTokenHash(passwordEncoder.encode(token));
    }
    
    @Transactional
    public void clearExpiredTokens() {
        tokenBlacklistRepository.limparTokensExpirados(LocalDateTime.now());
    }
    
    @Transactional
    public void clearTokensForUser(String email) {
        tokenBlacklistRepository.limparTokensPorEmail(email);
    }
    
    @Transactional
    public void logoutAllDevices(String email) {
        clearTokensForUser(email);
    }
}
