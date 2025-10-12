package com.ong.backend.config;

import java.io.IOException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ong.backend.entities.Usuario;
import com.ong.backend.repositories.UsuarioRepository;
import com.ong.backend.services.TokenService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(SecurityFilter.class);

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        try {
            String token = recuperarToken(request);
            
            if (StringUtils.hasText(token)) {
                if (tokenService.isTokenValid(token)) {
                    String email = tokenService.getSubject(token);
                    
                    if (StringUtils.hasText(email)) {
                        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
                        
                        if (usuarioOpt.isPresent()) {
                            Usuario usuario = usuarioOpt.get();
                            
                            // Verificar se o usuário está ativo e email verificado
                            if (usuario.isEmailVerificado()) {
                                UsernamePasswordAuthenticationToken authentication = 
                                    new UsernamePasswordAuthenticationToken(
                                        usuario, 
                                        null, 
                                        usuario.getAuthorities()
                                    );
                                
                                // Adicionar detalhes da requisição
                                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                                SecurityContextHolder.getContext().setAuthentication(authentication);
                                
                                logger.debug("Usuário autenticado: {}", email);
                            } else {
                                logger.debug("Usuário com email não verificado: {}", email);
                            }
                        } else {
                            logger.debug("Usuário não encontrado para email: {}", email);
                        }
                    }
                } else {
                    logger.debug("Token inválido ou expirado");
                }
            }
        } catch (Exception e) {
            logger.error("Erro na validação do token: {}", e.getMessage(), e);
            // Limpar contexto de segurança em caso de erro
            SecurityContextHolder.clearContext();
        }
        
        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        // Primeiro, tentar ler do header Authorization
        String authHeader = request.getHeader("Authorization");
        
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7).trim();
            if (StringUtils.hasText(token)) {
                return token;
            }
        }
        
        // Se não houver no header, tentar ler do cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt".equals(cookie.getName())) {
                    String token = cookie.getValue();
                    if (StringUtils.hasText(token)) {
                        return token;
                    }
                }
            }
        }
        
        return null;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        
        // Não filtrar rotas públicas
        return path.startsWith("/auth/") || 
               path.startsWith("/oauth2/") || 
               path.startsWith("/login/oauth2/") ||
               path.startsWith("/google-auth/") ||
               path.startsWith("/test/") ||
               path.equals("/error");
    }
}

