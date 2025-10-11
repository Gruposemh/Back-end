package com.ong.backend.services;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ong.backend.entities.OTPToken;
import com.ong.backend.repositories.OTPTokenRepository;

@Service
public class OTPService {
    
    private static final Logger logger = LoggerFactory.getLogger(OTPService.class);

    @Autowired
    private OTPTokenRepository otpTokenRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private RateLimitService rateLimitService;

    @Autowired(required = false)
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${app.otp.expiration-minutes:5}")
    private int expirationMinutes;

    private static final int OTP_LENGTH = 6;
    private static final SecureRandom random = new SecureRandom();
    private static final String REDIS_OTP_PREFIX = "otp:";

    @Transactional
    public void gerarEEnviarOTP(String email) {
        // Verificar rate limiting
        if (rateLimitService.isOTPRateLimited(email)) {
            throw new RuntimeException("Muitas tentativas de OTP. Tente novamente mais tarde.");
        }

        // Invalidar tokens ativos anteriores no banco
        otpTokenRepository.deleteByEmail(email);

        // Gerar novo token
        String token = gerarTokenNumerico();
        LocalDateTime expiraEm = LocalDateTime.now().plusMinutes(expirationMinutes);

        // Salvar no banco de dados
        OTPToken otpToken = new OTPToken(email, token, expiraEm);
        otpTokenRepository.save(otpToken);

        // Salvar no Redis para acesso rápido (se disponível)
        if (redisTemplate != null) {
            try {
                String redisKey = REDIS_OTP_PREFIX + email;
                redisTemplate.opsForValue().set(redisKey, token, expirationMinutes, TimeUnit.MINUTES);
                logger.info("OTP salvo no Redis para: {}", email);
            } catch (Exception e) {
                logger.warn("Redis não disponível, usando apenas banco de dados: {}", e.getMessage());
            }
        } else {
            logger.info("Redis não configurado, usando apenas banco de dados");
        }

        // Registrar tentativa
        rateLimitService.recordOTPAttempt(email);

        // Enviar por email
        emailService.enviarEmailOTP(email, token);
    }

    public boolean validarOTP(String email, String token) {
        // Primeiro verificar no Redis (mais rápido) se disponível
        if (redisTemplate != null) {
            try {
                String redisKey = REDIS_OTP_PREFIX + email;
                String redisToken = (String) redisTemplate.opsForValue().get(redisKey);
                
                if (redisToken != null && !redisToken.equals(token)) {
                    // Token inválido no Redis
                    return false;
                }
            } catch (Exception e) {
                logger.warn("Erro ao acessar Redis, usando banco de dados: {}", e.getMessage());
            }
        }
        
        // Verificar no banco
        Optional<OTPToken> tokenOTPOpt = otpTokenRepository.findByEmailAndOtpCodeAndUsedFalse(email, token);
            
            if (tokenOTPOpt.isEmpty()) {
                return false;
            }

            OTPToken otpToken = tokenOTPOpt.get();
            
            if (!otpToken.canAttempt()) {
                return false;
            }

            // Incrementar tentativas
            otpToken.incrementAttempts();
            
            if (!otpToken.isValid()) {
                otpTokenRepository.save(otpToken);
                return false;
            }

        // Marcar como usado
        otpToken.setUsed(true);
        otpTokenRepository.save(otpToken);

        // Limpar do Redis após uso (se disponível)
        if (redisTemplate != null) {
            try {
                String redisKey = REDIS_OTP_PREFIX + email;
                redisTemplate.delete(redisKey);
            } catch (Exception e) {
                logger.warn("Erro ao limpar Redis: {}", e.getMessage());
            }
        }
        
        // Limpar tentativas de rate limiting em caso de sucesso
        rateLimitService.clearOTPAttempts(email);

        return true;
    }

    @Transactional
    public void limparTokensExpirados() {
        otpTokenRepository.deleteExpiredTokens(LocalDateTime.now());
    }

    private String gerarTokenNumerico() {
        StringBuilder token = new StringBuilder();
        for (int i = 0; i < OTP_LENGTH; i++) {
            token.append(random.nextInt(10));
        }
        return token.toString();
    }
}
