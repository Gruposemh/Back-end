package com.ong.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ong.backend.dto.UsuarioDTO;
import com.ong.backend.entities.StatusRole;
import com.ong.backend.entities.Usuario;
import com.ong.backend.repositories.UsuarioRepository;
import com.ong.backend.services.TokenService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true") // habilita envio de cookies
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder encoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UsuarioDTO dto) {
        if (usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Usuário já existe com este email"));
        }

        if (dto.getRole() == null) {
            dto.setRole(StatusRole.USUARIO);
        }

        Usuario usuario = new Usuario(
                dto.getNome(),
                dto.getEmail(),
                encoder.encode(dto.getSenha()),
                dto.getRole()
        );

        usuarioRepository.save(usuario);

        return ResponseEntity.ok(new SuccessResponse("Usuário cadastrado com sucesso"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UsuarioDTO dto, HttpServletResponse response) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getSenha())
            );

            Usuario usuario = (Usuario) auth.getPrincipal();
            String token = tokenService.gerarToken(usuario);

            Cookie cookie = new Cookie("jwt", token);
            cookie.setHttpOnly(true);
            cookie.setSecure(false);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24); // 1 dia
            response.addCookie(cookie);

            return ResponseEntity.ok(new LoginResponse(usuario.getEmail(), usuario.getRole().name()));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Credenciais inválidas"));
        }
    }

    @GetMapping("/check")
    public ResponseEntity<?> check(@CookieValue(value = "jwt", required = false) String token) {
        if (token == null || !tokenService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ResponseEntity.ok("Logout realizado");
    }

    public static class ErrorResponse {
        private String message;
        public ErrorResponse(String message) { this.message = message; }
        public String getMessage() { return message; }
    }

    public static class SuccessResponse {
        private String message;
        public SuccessResponse(String message) { this.message = message; }
        public String getMessage() { return message; }
    }

    public static class LoginResponse {
        private String email;
        private String role;
        public LoginResponse(String email, String role) {
            this.email = email;
            this.role = role;
        }
        public String getEmail() { return email; }
        public String getRole() { return role; }
    }
}