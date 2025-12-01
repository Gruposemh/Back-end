package com.ong.backend.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ong.backend.entities.Usuario;

@RestController
@RequestMapping("/debug")
public class DebugController {

    @GetMapping("/auth-info")
    public ResponseEntity<?> getAuthInfo(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        Map<String, Object> info = new HashMap<>();
        
        // Informações do header
        info.put("authHeader", authHeader != null ? "Present (Bearer " + authHeader.substring(7, Math.min(20, authHeader.length())) + "...)" : "Missing");
        
        // Informações do contexto de segurança
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        if (auth != null) {
            info.put("authenticated", auth.isAuthenticated());
            info.put("principal", auth.getPrincipal().getClass().getSimpleName());
            
            if (auth.getPrincipal() instanceof Usuario) {
                Usuario usuario = (Usuario) auth.getPrincipal();
                info.put("email", usuario.getEmail());
                info.put("role", usuario.getRole());
                info.put("emailVerificado", usuario.isEmailVerificado());
            }
            
            info.put("authorities", auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));
        } else {
            info.put("authenticated", false);
            info.put("message", "No authentication found in SecurityContext");
        }
        
        return ResponseEntity.ok(info);
    }
}
