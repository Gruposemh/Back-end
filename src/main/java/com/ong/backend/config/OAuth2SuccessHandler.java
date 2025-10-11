package com.ong.backend.config;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ong.backend.entities.StatusRole;
import com.ong.backend.entities.TipoAutenticacao;
import com.ong.backend.entities.Usuario;
import com.ong.backend.repositories.UsuarioRepository;
import com.ong.backend.services.RefreshTokenService;
import com.ong.backend.services.TokenService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;

@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        
        // Extrair informações do usuário
        String email = oauth2User.getAttribute("email");
        String nome = oauth2User.getAttribute("name");
        
        // Buscar ou criar usuário
        Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);
        
        if (usuario == null) {
            // Criar novo usuário
            usuario = new Usuario();
            usuario.setNome(nome);
            usuario.setEmail(email);
            usuario.setEmailVerificado(true);
            usuario.setRole(StatusRole.USUARIO);
            usuario.setTipoAutenticacao(TipoAutenticacao.SOCIAL);
            usuario.setCriadoEm(LocalDateTime.now());
            usuario.setUltimoLogin(LocalDateTime.now());
            
            usuarioRepository.save(usuario);
        } else {
            // Atualizar último login e tipo de autenticação
            usuario.setUltimoLogin(LocalDateTime.now());
            if (usuario.getTipoAutenticacao() != TipoAutenticacao.SOCIAL) {
                usuario.setTipoAutenticacao(TipoAutenticacao.SOCIAL);
            }
            usuarioRepository.save(usuario);
        }
        
        // Gerar tokens
        String jwtToken = tokenService.gerarToken(usuario);
        
        // Criar refresh token
        String deviceInfo = request.getHeader("User-Agent");
        String ipAddress = request.getRemoteAddr();
        var refreshToken = refreshTokenService.createRefreshToken(usuario, deviceInfo, ipAddress);
        
        // Preparar resposta JSON
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("success", true);
        responseData.put("message", "Login com Google realizado com sucesso");
        responseData.put("token", jwtToken);
        responseData.put("refreshToken", refreshToken.getToken());
        responseData.put("email", usuario.getEmail());
        responseData.put("role", usuario.getRole().name());
        responseData.put("nome", usuario.getNome());
        
        // Enviar resposta JSON
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(objectMapper.writeValueAsString(responseData));
    }
}
