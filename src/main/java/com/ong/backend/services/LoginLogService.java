package com.ong.backend.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ong.backend.entities.LoginLog;
import com.ong.backend.entities.Usuario;
import com.ong.backend.repositories.LoginLogRepository;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class LoginLogService {
    
    @Autowired
    private LoginLogRepository loginLogRepository;
    
    @Transactional
    public void logLogin(Usuario usuario, HttpServletRequest request, LoginLog.LoginStatus status, 
                        LoginLog.LoginType type, String failureReason) {
        String ipAddress = getClientIpAddress(request);
        String userAgent = request.getHeader("User-Agent");
        
        LoginLog loginLog = new LoginLog(usuario, ipAddress, userAgent, status, type);
        if (failureReason != null) {
            loginLog.setFailureReason(failureReason);
        }
        
        loginLogRepository.save(loginLog);
    }
    
    @Transactional
    public void logSuccessfulLogin(Usuario usuario, HttpServletRequest request, LoginLog.LoginType type) {
        logLogin(usuario, request, LoginLog.LoginStatus.SUCCESS, type, null);
    }
    
    @Transactional
    public void logFailedLogin(Usuario usuario, HttpServletRequest request, LoginLog.LoginType type, String reason) {
        logLogin(usuario, request, LoginLog.LoginStatus.FAILED, type, reason);
    }
    
    public List<LoginLog> getLoginHistory(Usuario usuario) {
        return loginLogRepository.findByUsuarioOrderByTimestampDesc(usuario);
    }
    
    public Page<LoginLog> getLoginHistory(Usuario usuario, Pageable pageable) {
        return loginLogRepository.findByUsuarioOrderByTimestampDesc(usuario, pageable);
    }
    
    public List<LoginLog> getRecentLogins(Usuario usuario, int hours) {
        LocalDateTime since = LocalDateTime.now().minusHours(hours);
        return loginLogRepository.findRecentLoginsByUsuario(usuario, since);
    }
    
    public long getFailedLoginCount(Usuario usuario, int hours) {
        LocalDateTime since = LocalDateTime.now().minusHours(hours);
        return loginLogRepository.countFailedLoginsSince(usuario, since);
    }
    
    public long getFailedLoginCountByIp(String ipAddress, int hours) {
        LocalDateTime since = LocalDateTime.now().minusHours(hours);
        return loginLogRepository.countFailedLoginsByIpSince(ipAddress, since);
    }
    
    public List<LoginLog> getLastSuccessfulLogins(Usuario usuario, Pageable pageable) {
        return loginLogRepository.findLastSuccessfulLogins(usuario, pageable);
    }
    
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }
}
