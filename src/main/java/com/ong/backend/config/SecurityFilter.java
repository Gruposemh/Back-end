package com.ong.backend.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import com.ong.backend.services.TokenService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ong.backend.entities.Usuario;
import com.ong.backend.repositories.UsuarioRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        try {
            String token = recuperarToken(request);
            
            if (token != null && tokenService.isTokenValid(token)) {
            	String email = tokenService.getSubject(token);
            	Usuario usuario = usuarioRepository.findByEmail(email)
            	    .orElse(null);
                
                if (usuario != null) {
                    UsernamePasswordAuthenticationToken authentication = 
                        new UsernamePasswordAuthenticationToken(
                            usuario, 
                            null, 
                            usuario.getAuthorities()
                        );
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception e) {
            // Log da exceção se necessário
            System.err.println("Erro na validação do token: " + e.getMessage());
        }
        
        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            return token.trim().isEmpty() ? null : token;
        }
        
        return null;
    }
}

