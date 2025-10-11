package com.ong.backend.services;

import com.ong.backend.entities.RefreshToken;
import com.ong.backend.entities.Usuario;
import com.ong.backend.repositories.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RefreshTokenService {
    
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    
    @Autowired
    private TokenService tokenService;
    
    @Value("${jwt.refresh-expiration:604800000}") // 7 dias por padrão
    private Long refreshExpiration;
    
    private static final int TOKEN_LENGTH = 64;
    private static final int MAX_ACTIVE_TOKENS_PER_USER = 5; // Máximo de 5 tokens ativos por usuário
    private static final SecureRandom random = new SecureRandom();
    
    @Transactional
    public RefreshToken createRefreshToken(Usuario usuario, String deviceInfo, String ipAddress) {
        // Revogar tokens antigos se o usuário tiver muitos tokens ativos
        long activeTokensCount = refreshTokenRepository.countActiveTokensByUsuario(usuario);
        if (activeTokensCount >= MAX_ACTIVE_TOKENS_PER_USER) {
            revokeOldestTokens(usuario, (int) (activeTokensCount - MAX_ACTIVE_TOKENS_PER_USER + 1));
        }
        
        String token = generateSecureToken();
        LocalDateTime expiresAt = LocalDateTime.now().plusSeconds(refreshExpiration / 1000);
        
        RefreshToken refreshToken = new RefreshToken(token, usuario, expiresAt, deviceInfo, ipAddress);
        return refreshTokenRepository.save(refreshToken);
    }
    
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }
    
    public boolean isTokenValid(String token) {
        Optional<RefreshToken> refreshTokenOpt = findByToken(token);
        if (refreshTokenOpt.isEmpty()) {
            return false;
        }
        
        RefreshToken refreshToken = refreshTokenOpt.get();
        return refreshToken.isValid();
    }
    
    @Transactional
    public void revokeToken(String token) {
        refreshTokenRepository.revokeByToken(token);
    }
    
    @Transactional
    public void revokeAllUserTokens(Usuario usuario) {
        refreshTokenRepository.revokeAllByUsuario(usuario);
    }
    
    @Transactional
    public void revokeOldestTokens(Usuario usuario, int count) {
        List<RefreshToken> activeTokens = refreshTokenRepository.findByUsuarioAndRevokedFalse(usuario);
        activeTokens.sort((t1, t2) -> t1.getCreatedAt().compareTo(t2.getCreatedAt()));
        
        for (int i = 0; i < Math.min(count, activeTokens.size()); i++) {
            activeTokens.get(i).setRevoked(true);
        }
        
        refreshTokenRepository.saveAll(activeTokens);
    }
    
    @Transactional
    public void cleanExpiredTokens() {
        refreshTokenRepository.deleteExpiredTokens(LocalDateTime.now());
    }
    
    @Transactional
    public void cleanRevokedTokens(Usuario usuario) {
        refreshTokenRepository.deleteRevokedTokensByUsuario(usuario);
    }
    
    public List<RefreshToken> getUserActiveTokens(Usuario usuario) {
        return refreshTokenRepository.findByUsuarioAndRevokedFalse(usuario);
    }
    
    private String generateSecureToken() {
        StringBuilder token = new StringBuilder();
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        
        for (int i = 0; i < TOKEN_LENGTH; i++) {
            token.append(chars.charAt(random.nextInt(chars.length())));
        }
        
        return token.toString();
    }
}
