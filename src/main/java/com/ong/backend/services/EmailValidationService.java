package com.ong.backend.services;

import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

@Service
public class EmailValidationService {
    
    // Padrão mais robusto para validação de email
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$"
    );
    
    // Lista de domínios temporários conhecidos
    private static final String[] TEMPORARY_EMAIL_DOMAINS = {
        "10minutemail.com", "tempmail.org", "guerrillamail.com", "mailinator.com",
        "throwaway.email", "temp-mail.org", "yopmail.com", "getnada.com",
        "maildrop.cc", "sharklasers.com", "guerrillamailblock.com"
    };
    
    public EmailValidationResult validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return new EmailValidationResult(false, "Email é obrigatório");
        }
        
        email = email.trim().toLowerCase();
        
        // Verificar comprimento
        if (email.length() > 254) {
            return new EmailValidationResult(false, "Email muito longo (máximo 254 caracteres)");
        }
        
        // Verificar formato básico
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            return new EmailValidationResult(false, "Formato de email inválido");
        }
        
        // Verificar se não é email temporário
        if (isTemporaryEmail(email)) {
            return new EmailValidationResult(false, "Emails temporários não são permitidos");
        }
        
        // Verificar se tem domínio válido
        if (!hasValidDomain(email)) {
            return new EmailValidationResult(false, "Domínio de email inválido");
        }
        
        return new EmailValidationResult(true, "Email válido");
    }
    
    private boolean isTemporaryEmail(String email) {
        String domain = email.substring(email.lastIndexOf("@") + 1);
        for (String tempDomain : TEMPORARY_EMAIL_DOMAINS) {
            if (domain.equals(tempDomain)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean hasValidDomain(String email) {
        String domain = email.substring(email.lastIndexOf("@") + 1);
        
        // Verificar se tem pelo menos um ponto
        if (!domain.contains(".")) {
            return false;
        }
        
        // Verificar se não termina com ponto
        if (domain.endsWith(".")) {
            return false;
        }
        
        // Verificar se não começa com ponto
        if (domain.startsWith(".")) {
            return false;
        }
        
        // Verificar se tem extensão válida
        String[] parts = domain.split("\\.");
        if (parts.length < 2) {
            return false;
        }
        
        String extension = parts[parts.length - 1];
        return extension.length() >= 2 && extension.length() <= 6;
    }
    
    public static class EmailValidationResult {
        private final boolean valid;
        private final String message;
        
        public EmailValidationResult(boolean valid, String message) {
            this.valid = valid;
            this.message = message;
        }
        
        public boolean isValid() {
            return valid;
        }
        
        public String getMessage() {
            return message;
        }
    }
}
