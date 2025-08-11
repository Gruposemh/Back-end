package com.ong.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ong.backend.dto.UsuarioDTO;
import com.ong.backend.entities.StatusRole;
import com.ong.backend.entities.Usuario;
import com.ong.backend.repositories.UsuarioRepository;
import com.ong.backend.services.TokenService;

@RestController
@RequestMapping(value = "/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder encoder;

    @PostMapping(value = "/register")
    public ResponseEntity<?> register(@RequestBody UsuarioDTO dto) {
        try {
            if (usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
                return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Usuário já existe com este email"));
            }

            if (dto.getRole() == null) {
                dto.setRole(StatusRole.USUARIO);
            }

            Usuario usuario = new Usuario(
            	dto.getNome(),
                dto.getEmail(), // Email para login
                encoder.encode(dto.getSenha()),
                dto.getRole()
            );

            usuarioRepository.save(usuario);

            return ResponseEntity.ok(new SuccessResponse("Usuário cadastrado com sucesso"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Erro interno do servidor"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UsuarioDTO dto) {
        try {
            UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getSenha());

            Authentication auth = authenticationManager.authenticate(authToken);

            Usuario usuario = (Usuario) auth.getPrincipal();
            String token = tokenService.gerarToken(usuario); // subject = email

            return ResponseEntity.ok(new LoginResponse(token, usuario.getEmail(), usuario.getRole().name()));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("Credenciais inválidas"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Erro interno do servidor"));
        }
    }

    // Classes auxiliares internas

    public static class ErrorResponse {
        private String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    public static class SuccessResponse {
        private String message;

        public SuccessResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    public static class LoginResponse {
        private String token;
        private String email;
        private String role;

        public LoginResponse(String token, String email, String role) {
            this.token = token;
            this.email = email;
            this.role = role;
        }

        public String getToken() {
            return token;
        }

        public String getEmail() {
            return email;
        }

        public String getRole() {
            return role;
        }
    }
}

