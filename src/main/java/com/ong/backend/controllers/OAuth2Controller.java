package com.ong.backend.controllers;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ong.backend.entities.Usuario;
import com.ong.backend.repositories.UsuarioRepository;
import com.ong.backend.services.RefreshTokenService;
import com.ong.backend.services.TokenService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/oauth2")
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:3000", "http://localhost:5500", "http://127.0.0.1:5500", "http://localhost:8080", "http://127.0.0.1:8080"})
public class OAuth2Controller {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @GetMapping("/success")
    public ResponseEntity<?> oauth2Success(HttpServletRequest request) {
        try {
            // Obter autenticação do SecurityContext
            var authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
            
            if (authentication == null || !(authentication.getPrincipal() instanceof OAuth2User)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Usuário OAuth2 não encontrado"));
            }
            
            OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();

            // Extrair informações do usuário OAuth2
            String email = oauth2User.getAttribute("email");
            String nome = oauth2User.getAttribute("name");

            if (email == null || nome == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Email ou nome não encontrado no perfil OAuth2"));
            }

            // Buscar ou criar usuário
            Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);
            
            if (usuario == null) {
                // Criar novo usuário
                usuario = new Usuario();
                usuario.setNome(nome);
                usuario.setEmail(email);
                usuario.setEmailVerificado(true); // OAuth2 já verifica o email
                usuario.setRole(com.ong.backend.entities.StatusRole.USUARIO);
                usuario.setTipoAutenticacao(com.ong.backend.entities.TipoAutenticacao.SOCIAL);
                usuario.setCriadoEm(LocalDateTime.now());
                usuario.setUltimoLogin(LocalDateTime.now());
                
                usuarioRepository.save(usuario);
            } else {
                // Atualizar último login e tipo de autenticação
                usuario.setUltimoLogin(LocalDateTime.now());
                if (usuario.getTipoAutenticacao() != com.ong.backend.entities.TipoAutenticacao.SOCIAL) {
                    usuario.setTipoAutenticacao(com.ong.backend.entities.TipoAutenticacao.SOCIAL);
                }
                usuarioRepository.save(usuario);
            }

            // Gerar tokens
            String jwtToken = tokenService.gerarToken(usuario);
            
            // Criar refresh token
            String deviceInfo = request.getHeader("User-Agent");
            String ipAddress = request.getRemoteAddr();
            var refreshToken = refreshTokenService.createRefreshToken(usuario, deviceInfo, ipAddress);

            // Retornar resposta com tokens
            Map<String, Object> response = Map.of(
                "success", true,
                "message", "Login com Google realizado com sucesso",
                "token", jwtToken,
                "refreshToken", refreshToken.getToken(),
                "email", usuario.getEmail(),
                "role", usuario.getRole().name(),
                "nome", usuario.getNome()
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Erro interno do servidor: " + e.getMessage()));
        }
    }

    @GetMapping("/failure")
    public ResponseEntity<?> oauth2Failure() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(Map.of("error", "Falha na autenticação OAuth2"));
    }

    @GetMapping("/login/google")
    public ResponseEntity<?> initiateGoogleLogin() {
        // Redirecionar para o endpoint OAuth2 do Spring Security
        return ResponseEntity.status(HttpStatus.FOUND)
            .header("Location", "/oauth2/authorization/google")
            .body(Map.of("message", "Redirecionando para Google OAuth2"));
    }

    /**
     * Endpoint específico para mobile - retorna tokens em JSON
     * Usado após o fluxo OAuth2 ser concluído
     */
    @GetMapping("/mobile/success")
    public ResponseEntity<?> mobileOAuth2Success(HttpServletRequest request) {
        try {
            var authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
            
            if (authentication == null || !(authentication.getPrincipal() instanceof OAuth2User)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Usuário OAuth2 não encontrado"));
            }
            
            OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
            String email = oauth2User.getAttribute("email");
            String nome = oauth2User.getAttribute("name");

            if (email == null || nome == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Email ou nome não encontrado"));
            }

            Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);
            
            if (usuario == null) {
                usuario = new Usuario();
                usuario.setNome(nome);
                usuario.setEmail(email);
                usuario.setEmailVerificado(true);
                usuario.setRole(com.ong.backend.entities.StatusRole.USUARIO);
                usuario.setTipoAutenticacao(com.ong.backend.entities.TipoAutenticacao.SOCIAL);
                usuario.setCriadoEm(LocalDateTime.now());
                usuario.setUltimoLogin(LocalDateTime.now());
                usuarioRepository.save(usuario);
            } else {
                usuario.setUltimoLogin(LocalDateTime.now());
                if (usuario.getTipoAutenticacao() != com.ong.backend.entities.TipoAutenticacao.SOCIAL) {
                    usuario.setTipoAutenticacao(com.ong.backend.entities.TipoAutenticacao.SOCIAL);
                }
                usuarioRepository.save(usuario);
            }

            String jwtToken = tokenService.gerarToken(usuario);
            String deviceInfo = request.getHeader("User-Agent");
            String ipAddress = request.getRemoteAddr();
            var refreshToken = refreshTokenService.createRefreshToken(usuario, deviceInfo, ipAddress);

            return ResponseEntity.ok(Map.of(
                "success", true,
                "token", jwtToken,
                "refreshToken", refreshToken.getToken(),
                "email", usuario.getEmail(),
                "role", usuario.getRole().name(),
                "id", usuario.getId(),
                "nome", usuario.getNome()
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Erro: " + e.getMessage()));
        }
    }
}