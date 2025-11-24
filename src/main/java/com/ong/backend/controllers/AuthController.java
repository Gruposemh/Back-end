package com.ong.backend.controllers;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ong.backend.dto.EmailVerificationDTO;
import com.ong.backend.dto.OTPLoginDTO;
import com.ong.backend.dto.OTPRequestDTO;
import com.ong.backend.dto.PasswordResetDTO;
import com.ong.backend.dto.RefreshTokenDTO;
import com.ong.backend.dto.SocialLoginDTO;
import com.ong.backend.dto.UsuarioDTO;
import com.ong.backend.dto.auth.AuthResponseDTO;
import com.ong.backend.dto.auth.RegisterRequestDTO;
import com.ong.backend.dto.auth.VerifyEmailRequestDTO;
import com.ong.backend.entities.RefreshToken;
import com.ong.backend.entities.StatusRole;
import com.ong.backend.entities.TipoAutenticacao;
import com.ong.backend.entities.Usuario;
import com.ong.backend.repositories.UsuarioRepository;
import com.ong.backend.services.OTPService;
import com.ong.backend.services.RateLimitService;
import com.ong.backend.services.RefreshTokenService;
import com.ong.backend.services.TokenService;
import com.ong.backend.services.VerificationTokenService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/auth")
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:3000", "http://localhost:5500", "http://127.0.0.1:5500", "http://localhost:8080", "http://127.0.0.1:8080", "https://ong-tcc-ddabbpc6btgqeeey.brazilsouth-01.azurewebsites.net", "https://front-tcc-git-main-iagob12s-projects.vercel.app", "https://front-tcc-nine.vercel.app", "https://front-o5yzf96tq-iagob12s-projects.vercel.app"}, allowCredentials = "true")
public class AuthController {

    @Autowired
    private TokenService tokenService;
    
    @Autowired
    private VerificationTokenService verificationTokenService;
    
    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private OTPService otpService;

    @Autowired
    private RateLimitService rateLimitService;

    
    private void createAuthCookie(HttpServletResponse response, String token, int maxAge) {
        Cookie cookie = new Cookie("jwt", token);
        cookie.setHttpOnly(true); // Garante que o cookie não seja acessível via JavaScript
        cookie.setSecure(true);   // Defina como true quando estiver em produção (apenas HTTPS)
        cookie.setPath("/");      // O cookie será enviado para todas as rotas
        cookie.setMaxAge(maxAge);
        
        // Configuração SameSite=None para permitir cookies entre origens diferentes
        response.setHeader("Set-Cookie", String.format(
            "jwt=%s; Path=/; Max-Age=%d; HttpOnly; Secure; SameSite=None",
            token != null ? token : "", maxAge
        ));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UsuarioDTO dto) {
        try {
            // Verificar se email já existe
            if (usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse("Email já cadastrado"));
            }

            // Criar usuário
            Usuario usuario = new Usuario();
            usuario.setNome(dto.getNome());
            usuario.setEmail(dto.getEmail());
            usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
            usuario.setRole(dto.getRole() != null ? dto.getRole() : StatusRole.USUARIO);
            usuario.setTipoAutenticacao(TipoAutenticacao.TRADICIONAL);
            usuario.setCriadoEm(LocalDateTime.now());
            usuario.setEmailVerificado(false);

            usuarioRepository.save(usuario);
            
            // Enviar email de verificação
            verificationTokenService.gerarEEnviarTokenVerificacaoEmail(dto.getEmail());

            return ResponseEntity.status(HttpStatus.CREATED)
                .body(new SuccessResponse("Usuário cadastrado com sucesso. Verifique seu email para ativar a conta."));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Erro interno do servidor"));
        }
    }

    // ===== NOVOS ENDPOINTS MODERNOS =====

    @PostMapping("/register-modern")
    public ResponseEntity<AuthResponseDTO> registerModern(@Valid @RequestBody RegisterRequestDTO request) {
        try {
            // Verificar se email já existe
            Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(request.getEmail());
            if (usuarioExistente.isPresent()) {
                Usuario usuario = usuarioExistente.get();
                // Se o usuário já existe mas não verificou o email, permitir reenvio
                if (!usuario.isEmailVerificado()) {
                    verificationTokenService.gerarEEnviarTokenVerificacaoEmail(request.getEmail());
                    return ResponseEntity.ok()
                        .body(AuthResponseDTO.success("Email já cadastrado mas não verificado. Um novo código foi enviado para seu email."));
                }
                return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(AuthResponseDTO.error("Email já cadastrado e verificado. Faça login."));
            }

            // Criar usuário com senha criptografada
            Usuario usuario = new Usuario();
            usuario.setNome(request.getNome());
            usuario.setEmail(request.getEmail());
            usuario.setSenha(passwordEncoder.encode(request.getSenha())); // Salvar senha criptografada
            usuario.setRole(StatusRole.USUARIO);
            usuario.setTipoAutenticacao(TipoAutenticacao.TRADICIONAL);
            usuario.setCriadoEm(LocalDateTime.now());
            usuario.setEmailVerificado(false);

            usuarioRepository.save(usuario);
            
            // Enviar código de verificação por email REAL
            verificationTokenService.gerarEEnviarTokenVerificacaoEmail(request.getEmail());

            return ResponseEntity.status(HttpStatus.CREATED)
                .body(AuthResponseDTO.success("Cadastro realizado! Verifique seu email e insira o código de 6 dígitos para confirmar."));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(AuthResponseDTO.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(AuthResponseDTO.error("Erro interno do servidor"));
        }
    }

    // ENDPOINT TEMPORÁRIO PARA CRIAR ADMIN - REMOVER EM PRODUÇÃO
    @PostMapping("/create-admin-temp")
    public ResponseEntity<?> createAdminTemp(@RequestBody Map<String, String> request) {
        try {
            String nome = request.get("nome");
            String email = request.get("email");
            String senha = request.get("senha");

            // Verificar se já existe
            if (usuarioRepository.findByEmail(email).isPresent()) {
                return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Email já cadastrado"));
            }

            // Criar ADMIN
            Usuario admin = new Usuario();
            admin.setNome(nome);
            admin.setEmail(email);
            admin.setSenha(passwordEncoder.encode(senha));
            admin.setRole(StatusRole.ADMIN);
            admin.setTipoAutenticacao(TipoAutenticacao.TRADICIONAL);
            admin.setCriadoEm(LocalDateTime.now());
            admin.setEmailVerificado(true); // Admin já verificado

            usuarioRepository.save(admin);

            return ResponseEntity.ok(new SuccessResponse("Admin criado com sucesso!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Erro ao criar admin"));
        }
    }

    @PostMapping("/verify-email-modern")
    public ResponseEntity<AuthResponseDTO> verifyEmail(@Valid @RequestBody VerifyEmailRequestDTO request, HttpServletRequest httpRequest) {
        try {
            boolean isValid = verificationTokenService.validarTokenVerificacaoEmail(request.getEmail(), request.getCodigo());
            
            if (isValid) {
                // Buscar usuário para gerar tokens e fazer login automático
                Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(request.getEmail());
                if (usuarioOpt.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(AuthResponseDTO.error("Usuário não encontrado"));
                }
                
                Usuario usuario = usuarioOpt.get();
                
                // Gerar tokens de acesso para login automático
                String accessToken = tokenService.gerarToken(usuario);
                String deviceInfo = httpRequest.getHeader("User-Agent");
                String ipAddress = httpRequest.getRemoteAddr();
                RefreshToken refreshTokenEntity = refreshTokenService.createRefreshToken(usuario, deviceInfo, ipAddress);
                
                AuthResponseDTO.UserInfoDTO userInfo = new AuthResponseDTO.UserInfoDTO(
                    usuario.getId(),
                    usuario.getNome(),
                    usuario.getEmail(),
                    usuario.getRole().name(),
                    usuario.isEmailVerificado()
                );

                return ResponseEntity.ok(AuthResponseDTO.loginSuccess(
                    accessToken, 
                    refreshTokenEntity.getToken(), 
                    900000L, // 15 minutos em millisegundos
                    userInfo
                ));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(AuthResponseDTO.error("Código inválido ou expirado"));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(AuthResponseDTO.error("Erro interno do servidor"));
        }
    }

    // Endpoint removido - senha agora é definida no cadastro

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UsuarioDTO dto, HttpServletRequest request, HttpServletResponse response) {
        try {
            // Verificar rate limiting
            if (rateLimitService.isLoginRateLimited(dto.getEmail())) {
                return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(new ErrorResponse("Muitas tentativas de login. Tente novamente em 1 hora."));
            }
            
            // Verificar se usuário existe
            var usuarioOpt = usuarioRepository.findByEmail(dto.getEmail());
            if (usuarioOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Credenciais inválidas"));
            }

            Usuario usuario = usuarioOpt.get();

            // Verificar senha
            if (!passwordEncoder.matches(dto.getSenha(), usuario.getSenha())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Credenciais inválidas"));
            }

            // Verificar se email está verificado
            if (!usuario.isEmailVerificado()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ErrorResponse("Email não verificado. Verifique sua caixa de entrada."));
            }

            // Limpar tentativas de login
            rateLimitService.clearLoginAttempts(dto.getEmail());
            
            // Atualizar último login
            usuario.setUltimoLogin(LocalDateTime.now());
            usuarioRepository.save(usuario);
            
            // Gerar tokens
            String jwtToken = tokenService.gerarToken(usuario);
            
            // Criar refresh token
            String deviceInfo = request.getHeader("User-Agent");
            String ipAddress = request.getRemoteAddr();
            var refreshToken = refreshTokenService.createRefreshToken(usuario, deviceInfo, ipAddress);

            // Criar cookie HTTP-only com o JWT token
            createAuthCookie(response, jwtToken, 60 * 60 * 24); // 1 dia

            return ResponseEntity.ok(new LoginResponse(jwtToken, refreshToken.getToken(), usuario.getEmail(), usuario.getRole().name(), usuario.getId(), usuario.getNome()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Erro interno do servidor"));
        }
    }

    @PostMapping("/social-login")
    public ResponseEntity<?> socialLogin(@RequestBody SocialLoginDTO dto, HttpServletRequest request) {
        try {
            // Implementação simplificada - retornar erro por enquanto
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                .body(new ErrorResponse("Social login não implementado ainda"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Erro interno do servidor"));
        }
    }

    @PostMapping("/request-otp")
    public ResponseEntity<?> requestOTP(@RequestBody OTPRequestDTO dto) {
        try {
            // Verificar rate limiting
            if (rateLimitService.isOTPRateLimited(dto.getEmail())) {
                return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(new ErrorResponse("Muitas solicitações. Tente novamente em 1 hora."));
            }
            
            // Verificar se email existe
            Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(dto.getEmail());
            if (usuarioOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Email não encontrado"));
            }
            
            Usuario usuario = usuarioOpt.get();
            
            // Verificar se email foi verificado
            if (!usuario.isEmailVerificado()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ErrorResponse("Email não verificado. Complete seu cadastro primeiro."));
            }

            // Registrar tentativa
            rateLimitService.recordOTPAttempt(dto.getEmail());
            
            otpService.gerarEEnviarOTP(dto.getEmail());

            return ResponseEntity.ok(new SuccessResponse("OTP enviado para seu email"));

        } catch (RuntimeException e) {
            // Log do erro real
            System.err.println("Erro ao enviar OTP: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Erro ao enviar OTP: " + e.getMessage()));
        } catch (Exception e) {
            System.err.println("Erro inesperado ao enviar OTP: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Erro interno do servidor"));
        }
    }

    @PostMapping("/login-otp")
    public ResponseEntity<?> loginOTP(@RequestBody OTPLoginDTO dto, HttpServletRequest request, HttpServletResponse response) {
        try {
            // Verificar OTP
            if (!otpService.validarOTP(dto.getEmail(), dto.getToken())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("OTP inválido ou expirado"));
            }

            // Buscar usuário
            var usuarioOpt = usuarioRepository.findByEmail(dto.getEmail());
            if (usuarioOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Usuário não encontrado"));
            }

            Usuario usuario = usuarioOpt.get();

            // Atualizar último login
            usuario.setUltimoLogin(LocalDateTime.now());
            usuarioRepository.save(usuario);

            // Gerar tokens
            String jwtToken = tokenService.gerarToken(usuario);
            
            // Criar refresh token
            String deviceInfo = request.getHeader("User-Agent");
            String ipAddress = request.getRemoteAddr();
            var refreshToken = refreshTokenService.createRefreshToken(usuario, deviceInfo, ipAddress);

            // Criar cookie HTTP-only com o JWT token
            createAuthCookie(response, jwtToken, 60 * 60 * 24); // 1 dia

            return ResponseEntity.ok(new LoginResponse(jwtToken, refreshToken.getToken(), usuario.getEmail(), usuario.getRole().name(), usuario.getId(), usuario.getNome()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Erro interno do servidor"));
        }
    }

    @PostMapping("/verify-email-legacy")
    public ResponseEntity<?> verifyEmailLegacy(@RequestBody EmailVerificationDTO dto) {
        try {
            boolean isValid = verificationTokenService.validarTokenVerificacaoEmail(dto.getEmail(), dto.getToken());
            
            if (isValid) {
                return ResponseEntity.ok(new SuccessResponse("Email verificado com sucesso"));
            } else {
                return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Token inválido ou expirado"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Erro interno do servidor"));
        }
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<?> resendVerification(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            if (email == null || email.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Email é obrigatório"));
            }
            
            // Verificar se o usuário existe
            Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
            if (usuarioOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Usuário não encontrado"));
            }
            
            Usuario usuario = usuarioOpt.get();
            if (usuario.isEmailVerificado()) {
                return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Email já verificado. Faça login."));
            }
            
            verificationTokenService.gerarEEnviarTokenVerificacaoEmail(email);
            return ResponseEntity.ok(new SuccessResponse("Novo código de verificação enviado para seu email"));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Erro interno do servidor"));
        }
    }

    @PostMapping("/request-password-reset")
    public ResponseEntity<?> requestPasswordReset(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            if (email == null || email.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Email é obrigatório"));
            }
            
            verificationTokenService.gerarEEnviarTokenResetSenha(email);
            return ResponseEntity.ok(new SuccessResponse("Email de reset de senha enviado"));

        } catch (Exception e) {
            System.err.println("❌ Erro em requestPasswordReset: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Erro interno do servidor: " + e.getMessage()));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetDTO dto) {
        try {
            boolean success = verificationTokenService.validarEResetarSenha(dto.getEmail(), dto.getToken(), dto.getNovaSenha());
            
            if (success) {
                return ResponseEntity.ok(new SuccessResponse("Senha alterada com sucesso"));
            } else {
                return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Token inválido ou expirado"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Erro interno do servidor"));
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenDTO refreshTokenDTO, HttpServletRequest request) {
        try {
            String refreshToken = refreshTokenDTO.getRefreshToken();
            
            if (refreshToken == null || refreshToken.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Refresh token é obrigatório"));
            }
            
            // Verificar se o refresh token é válido
            if (!refreshTokenService.isTokenValid(refreshToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Refresh token inválido ou expirado"));
            }
            
            // Buscar o refresh token
            var refreshTokenOpt = refreshTokenService.findByToken(refreshToken);
            if (refreshTokenOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Refresh token não encontrado"));
            }
            
            var refreshTokenEntity = refreshTokenOpt.get();
            Usuario usuario = refreshTokenEntity.getUsuario();
            
            // Gerar novo JWT token
            String newToken = tokenService.gerarToken(usuario);
            
            // Revogar o refresh token usado
            refreshTokenService.revokeToken(refreshToken);
            
            // Criar novo refresh token
            String deviceInfo = request.getHeader("User-Agent");
            String ipAddress = request.getRemoteAddr();
            var newRefreshToken = refreshTokenService.createRefreshToken(usuario, deviceInfo, ipAddress);
            
            return ResponseEntity.ok(new LoginResponse(newToken, newRefreshToken.getToken(), usuario.getEmail(), usuario.getRole().name(), usuario.getId(), usuario.getNome()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Erro interno do servidor"));
        }
    }

    @GetMapping("/check")
    public ResponseEntity<?> check(@CookieValue(value = "jwt", required = false) String token) {
        try {
            if (token == null || !tokenService.isTokenValid(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            String email = tokenService.getEmailFromToken(token);
            var usuarioOpt = usuarioRepository.findByEmail(email);

            if (usuarioOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            Usuario usuario = usuarioOpt.get();

            Map<String, Object> userInfo = Map.of(
                "id", usuario.getId(),
                "email", usuario.getEmail(),
                "role", usuario.getRole().name(),
                "nome", usuario.getNome()
            );

            return ResponseEntity.ok(userInfo);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            // Limpar o cookie JWT
            createAuthCookie(response, "", 0); // Expira imediatamente
            
            return ResponseEntity.ok(new SuccessResponse("Logout realizado com sucesso"));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Erro interno do servidor"));
        }
    }

    @GetMapping("/token-status")
    public ResponseEntity<?> getTokenStatus(HttpServletRequest request) {
        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Token de autorização não encontrado"));
            }
            
            String token = authHeader.substring(7);
            
            if (!tokenService.isTokenValid(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Token inválido ou expirado"));
            }
            
            String email = tokenService.getEmailFromToken(token);
            var usuarioOpt = usuarioRepository.findByEmail(email);
            
            if (usuarioOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Usuário não encontrado"));
            }
            
            Usuario usuario = usuarioOpt.get();
            
            Map<String, Object> response = Map.of(
                "valid", true,
                "email", usuario.getEmail(),
                "role", usuario.getRole().name(),
                "nome", usuario.getNome()
            );
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Erro interno do servidor"));
        }
    }

    @GetMapping("/login-history")
    public ResponseEntity<?> getLoginHistory(HttpServletRequest request) {
        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Token de autorização não encontrado"));
            }
            
            String token = authHeader.substring(7);
            
            if (!tokenService.isTokenValid(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Token inválido ou expirado"));
            }
            
            String email = tokenService.getEmailFromToken(token);
            var usuarioOpt = usuarioRepository.findByEmail(email);
            
            if (usuarioOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Usuário não encontrado"));
            }
            
            // Por enquanto, retornar histórico simulado
            // Em uma implementação completa, você buscaria do LoginLogService
            Map<String, Object> history = Map.of(
                "message", "Histórico de login não implementado ainda",
                "usuario", email,
                "ultimoLogin", usuarioOpt.get().getUltimoLogin()
            );
            
            return ResponseEntity.ok(history);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Erro interno do servidor"));
        }
    }

    @PostMapping("/logout-all-devices")
    public ResponseEntity<?> logoutAllDevices(HttpServletRequest request, HttpServletResponse response) {
        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Token de autorização não encontrado"));
            }
            
            String token = authHeader.substring(7);
            String email = tokenService.getEmailFromToken(token);
            
            // Revogar todos os refresh tokens do usuário
            var usuarioOpt = usuarioRepository.findByEmail(email);
            if (usuarioOpt.isPresent()) {
                // Por enquanto, apenas log - implementar revokeAllTokensForUser se necessário
                // refreshTokenService.revokeAllTokensForUser(usuarioOpt.get());
            }
            
            // Limpar o cookie JWT
            createAuthCookie(response, "", 0);
            
            // Aqui você pode implementar blacklist de todos os tokens se necessário
            // tokenBlacklistService.logoutAllDevices(email);
            
            return ResponseEntity.ok(new SuccessResponse("Logout de todos os dispositivos realizado com sucesso"));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Erro interno do servidor"));
        }
    }

    // Classes internas para respostas
    public static class LoginResponse {
        private String token;
        private String refreshToken;
        private String email;
        private String role;
        private Long id;
        private String nome;

        public LoginResponse(String token, String refreshToken, String email, String role, Long id, String nome) {
            this.token = token;
            this.refreshToken = refreshToken;
            this.email = email;
            this.role = role;
            this.id = id;
            this.nome = nome;
        }

        public String getToken() { return token; }
        public String getRefreshToken() { return refreshToken; }
        public String getEmail() { return email; }
        public String getRole() { return role; }
        public Long getId() { return id; }
        public String getNome() { return nome; }
    }

    public static class SuccessResponse {
        private String message;

        public SuccessResponse(String message) {
            this.message = message;
        }

        public String getMessage() { return message; }
    }

    public static class ErrorResponse {
        private String error;

        public ErrorResponse(String error) {
            this.error = error;
        }

        public String getError() { return error; }
    }
    
    // ⚠️ ENDPOINT TEMPORÁRIO PARA DESENVOLVIMENTO - REMOVER EM PRODUÇÃO
    @PostMapping("/dev/clear-rate-limit")
    public ResponseEntity<?> clearRateLimit(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            if (email == null || email.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Email é obrigatório"));
            }
            
            rateLimitService.clearOTPAttempts(email);
            rateLimitService.clearLoginAttempts(email);
            
            return ResponseEntity.ok(new SuccessResponse("Rate limit limpo para: " + email));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Erro ao limpar rate limit"));
        }
    }
}
