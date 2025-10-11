package com.ong.backend.services;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Service;

@Service
public class RateLimitService {
    
    private final ConcurrentMap<String, AttemptInfo> otpAttempts = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, AttemptInfo> loginAttempts = new ConcurrentHashMap<>();
    
    private static final int MAX_OTP_ATTEMPTS = 3; // 3 tentativas por hora
    private static final int MAX_LOGIN_ATTEMPTS = 5; // 5 tentativas por hora
    private static final int RATE_LIMIT_WINDOW_HOURS = 1;
    
    public boolean isOTPRateLimited(String email) {
        return isRateLimited(email, otpAttempts, MAX_OTP_ATTEMPTS);
    }
    
    public boolean isLoginRateLimited(String email) {
        return isRateLimited(email, loginAttempts, MAX_LOGIN_ATTEMPTS);
    }
    
    public void recordOTPAttempt(String email) {
        recordAttempt(email, otpAttempts);
    }
    
    public void recordLoginAttempt(String email) {
        recordAttempt(email, loginAttempts);
    }
    
    public void clearOTPAttempts(String email) {
        otpAttempts.remove(email);
    }
    
    public void clearLoginAttempts(String email) {
        loginAttempts.remove(email);
    }
    
    private boolean isRateLimited(String key, ConcurrentMap<String, AttemptInfo> attempts, int maxAttempts) {
        AttemptInfo info = attempts.get(key);
        
        if (info == null) {
            return false;
        }
        
        // Limpar tentativas antigas
        if (info.firstAttempt.isBefore(LocalDateTime.now().minusHours(RATE_LIMIT_WINDOW_HOURS))) {
            attempts.remove(key);
            return false;
        }
        
        return info.count >= maxAttempts;
    }
    
    private void recordAttempt(String key, ConcurrentMap<String, AttemptInfo> attempts) {
        LocalDateTime now = LocalDateTime.now();
        
        attempts.compute(key, (k, info) -> {
            if (info == null) {
                return new AttemptInfo(now, 1);
            }
            
            // Reset se passou da janela de tempo
            if (info.firstAttempt.isBefore(now.minusHours(RATE_LIMIT_WINDOW_HOURS))) {
                return new AttemptInfo(now, 1);
            }
            
            return new AttemptInfo(info.firstAttempt, info.count + 1);
        });
    }
    
    private static class AttemptInfo {
        final LocalDateTime firstAttempt;
        final int count;
        
        AttemptInfo(LocalDateTime firstAttempt, int count) {
            this.firstAttempt = firstAttempt;
            this.count = count;
        }
    }
}
