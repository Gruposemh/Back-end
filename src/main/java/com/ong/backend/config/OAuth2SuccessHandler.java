package com.ong.backend.config;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
            System.out.println("✅ Novo usuário criado via OAuth2: " + email + " - Role: USUARIO");
        } else {
            // Usuário já existe - manter role original e atualizar último login
            usuario.setUltimoLogin(LocalDateTime.now());
            // Não alterar o role - manter o que já existe (pode ser ADMIN)
            // Garantir que email está verificado
            if (!usuario.isEmailVerificado()) {
                usuario.setEmailVerificado(true);
            }
            // Atualizar tipo de autenticação se necessário
            if (usuario.getTipoAutenticacao() == null) {
                usuario.setTipoAutenticacao(TipoAutenticacao.SOCIAL);
            }
            usuarioRepository.save(usuario);
            System.out.println("✅ Usuário existente logado via OAuth2: " + email + " - Role: " + usuario.getRole());
        }
        
        // Gerar tokens
        String jwtToken = tokenService.gerarToken(usuario);
        
        // Criar refresh token
        String deviceInfo = request.getHeader("User-Agent");
        String ipAddress = request.getRemoteAddr();
        var refreshToken = refreshTokenService.createRefreshToken(usuario, deviceInfo, ipAddress);
        
        // Verificar se é requisição mobile (via User-Agent ou parâmetro)
        String userAgent = request.getHeader("User-Agent");
        String isMobile = request.getParameter("mobile");
        
        // Log para debug
        System.out.println("=== OAuth2 Success Handler ===");
        System.out.println("User-Agent: " + userAgent);
        System.out.println("Parâmetro mobile: " + isMobile);
        System.out.println("Request URL: " + request.getRequestURL());
        System.out.println("Query String: " + request.getQueryString());
        
        boolean isMobileRequest = (isMobile != null && isMobile.equals("true")) || 
                                  (userAgent != null && (
                                      userAgent.toLowerCase().contains("expo") ||
                                      userAgent.toLowerCase().contains("android") ||
                                      userAgent.toLowerCase().contains("iphone")
                                  ));
        
        System.out.println("É mobile? " + isMobileRequest);
        
        if (isMobileRequest) {
            // Para mobile: redirecionar com tokens na URL (deep link)
            String nomeEncoded = URLEncoder.encode(usuario.getNome(), StandardCharsets.UTF_8);
            
            String redirectUrl = String.format(
                "voluntariosprobem://oauth2/callback?token=%s&refreshToken=%s&email=%s&role=%s&id=%d&nome=%s",
                jwtToken,
                refreshToken.getToken(),
                usuario.getEmail(),
                usuario.getRole().name(),
                usuario.getId(),
                nomeEncoded
            );
            
            System.out.println("🔵 Redirecionando para MOBILE: " + redirectUrl);
            response.sendRedirect(redirectUrl);
        } else {
            // Para web: usar TANTO cookie QUANTO localStorage (solução híbrida)
            String nomeEncoded = URLEncoder.encode(usuario.getNome(), StandardCharsets.UTF_8);
            
            // Tentar definir cookie (pode não funcionar entre domínios)
            int maxAge = 60 * 60 * 24; // 1 dia
            response.setHeader("Set-Cookie", String.format(
                "jwt=%s; Path=/; Max-Age=%d; HttpOnly; Secure; SameSite=None",
                jwtToken, maxAge
            ));
            
            // Redirecionar com tokens na URL como fallback
            String redirectUrl = String.format(
                "https://front-tcc2.vercel.app/oauth2/callback?token=%s&refreshToken=%s&email=%s&role=%s&id=%d&nome=%s",
                jwtToken,
                refreshToken.getToken(),
                usuario.getEmail(),
                usuario.getRole().name(),
                usuario.getId(),
                nomeEncoded
            );
            
            System.out.println("🌐 Redirecionando para WEB com cookie E tokens na URL: " + redirectUrl);
            response.sendRedirect(redirectUrl);
        }
    }
}
