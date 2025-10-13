package com.ong.backend.config;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.ong.backend.entities.StatusRole;
import com.ong.backend.entities.TipoAutenticacao;
import com.ong.backend.entities.Usuario;
import com.ong.backend.repositories.UsuarioRepository;
import com.ong.backend.services.RefreshTokenService;
import com.ong.backend.services.TokenService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RefreshTokenService refreshTokenService;

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
        
        // Criar cookie HTTP-only com o JWT token
        Cookie cookie = new Cookie("jwt", jwtToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // Mudar para true em produção com HTTPS
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24); // 1 dia
        response.addCookie(cookie);
        
        // Redirecionar para o frontend (sem tokens na URL)
        String redirectUrl = "https://front-tcc-nine.vercel.app/";
        
        response.sendRedirect(redirectUrl);
    }
}
