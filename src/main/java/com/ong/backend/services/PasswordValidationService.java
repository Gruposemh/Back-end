package com.ong.backend.services;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

@Service
public class PasswordValidationService {
    
    private static final int MIN_LENGTH = 8;
    private static final int MAX_LENGTH = 128;
    
    // Padrões para validação
    private static final Pattern UPPERCASE_PATTERN = Pattern.compile("[A-Z]");
    private static final Pattern LOWERCASE_PATTERN = Pattern.compile("[a-z]");
    private static final Pattern DIGIT_PATTERN = Pattern.compile("[0-9]");
    private static final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile("[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]");
    
    public PasswordValidationResult validatePassword(String password) {
        List<String> errors = new ArrayList<>();
        
        if (password == null || password.trim().isEmpty()) {
            errors.add("Senha é obrigatória");
            return new PasswordValidationResult(false, errors);
        }
        
        // Verificar comprimento mínimo
        if (password.length() < MIN_LENGTH) {
            errors.add("Senha deve ter pelo menos " + MIN_LENGTH + " caracteres");
        }
        
        // Verificar comprimento máximo
        if (password.length() > MAX_LENGTH) {
            errors.add("Senha deve ter no máximo " + MAX_LENGTH + " caracteres");
        }
        
        // Verificar se contém letra maiúscula
        if (!UPPERCASE_PATTERN.matcher(password).find()) {
            errors.add("Senha deve conter pelo menos uma letra maiúscula");
        }
        
        // Verificar se contém letra minúscula
        if (!LOWERCASE_PATTERN.matcher(password).find()) {
            errors.add("Senha deve conter pelo menos uma letra minúscula");
        }
        
        // Verificar se contém dígito
        if (!DIGIT_PATTERN.matcher(password).find()) {
            errors.add("Senha deve conter pelo menos um número");
        }
        
        // Verificar se contém caractere especial
        if (!SPECIAL_CHAR_PATTERN.matcher(password).find()) {
            errors.add("Senha deve conter pelo menos um caractere especial (!@#$%^&*()_+-=[]{}|;':\",./<>?)");
        }
        
        // Verificar senhas comuns
        if (isCommonPassword(password)) {
            errors.add("Esta senha é muito comum. Escolha uma senha mais segura");
        }
        
        // Verificar sequências
        if (hasSequentialChars(password)) {
            errors.add("Senha não deve conter sequências de caracteres (ex: 123, abc)");
        }
        
        // Verificar repetições
        if (hasRepeatedChars(password)) {
            errors.add("Senha não deve conter caracteres repetidos em sequência (ex: aaa, 111)");
        }
        
        return new PasswordValidationResult(errors.isEmpty(), errors);
    }
    
    private boolean isCommonPassword(String password) {
        String[] commonPasswords = {
            "password", "123456", "123456789", "qwerty", "abc123", "password123",
            "admin", "letmein", "welcome", "monkey", "1234567890", "password1",
            "qwerty123", "dragon", "master", "hello", "freedom", "whatever",
            "qazwsx", "trustno1", "jordan23", "harley", "password1", "jordan",
            "superman", "michael", "andrew", "jessica", "pepper", "1234",
            "baseball", "dallas", "jennifer", "joshua", "maggie", "hunter",
            "sunshine", "iloveyou", "2000", "charlie", "robert", "thomas",
            "hockey", "ranger", "daniel", "asshole", "fuckyou", "12345",
            "buster", "shadow", "michael", "jennifer", "jordan", "superman",
            "harley", "ranger", "buster", "hunter", "fuck", "jordan23"
        };
        
        String lowerPassword = password.toLowerCase();
        for (String common : commonPasswords) {
            if (lowerPassword.contains(common)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean hasSequentialChars(String password) {
        String lowerPassword = password.toLowerCase();
        
        // Verificar sequências numéricas
        for (int i = 0; i < lowerPassword.length() - 2; i++) {
            char c1 = lowerPassword.charAt(i);
            char c2 = lowerPassword.charAt(i + 1);
            char c3 = lowerPassword.charAt(i + 2);
            
            if (Character.isDigit(c1) && Character.isDigit(c2) && Character.isDigit(c3)) {
                if (c2 == c1 + 1 && c3 == c2 + 1) {
                    return true;
                }
            }
        }
        
        // Verificar sequências alfabéticas
        for (int i = 0; i < lowerPassword.length() - 2; i++) {
            char c1 = lowerPassword.charAt(i);
            char c2 = lowerPassword.charAt(i + 1);
            char c3 = lowerPassword.charAt(i + 2);
            
            if (Character.isLetter(c1) && Character.isLetter(c2) && Character.isLetter(c3)) {
                if (c2 == c1 + 1 && c3 == c2 + 1) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    private boolean hasRepeatedChars(String password) {
        for (int i = 0; i < password.length() - 2; i++) {
            char c = password.charAt(i);
            if (password.charAt(i + 1) == c && password.charAt(i + 2) == c) {
                return true;
            }
        }
        return false;
    }
    
    public static class PasswordValidationResult {
        private final boolean valid;
        private final List<String> errors;
        
        public PasswordValidationResult(boolean valid, List<String> errors) {
            this.valid = valid;
            this.errors = errors;
        }
        
        public boolean isValid() {
            return valid;
        }
        
        public List<String> getErrors() {
            return errors;
        }
        
        public String getErrorMessage() {
            return String.join("; ", errors);
        }
    }
}
