package com.ong.backend.controllers;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.ong.backend.entities.StatusRole;
import com.ong.backend.entities.TipoAutenticacao;
import com.ong.backend.entities.Usuario;
import com.ong.backend.repositories.UsuarioRepository;
import com.ong.backend.services.EmailService;
import com.ong.backend.services.TokenService;

@RestController
@RequestMapping("/google-auth")
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:5500", "http://localhost:5500", "http://127.0.0.1:3000"})
public class GoogleAuthController {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/verify")
    public ResponseEntity<?> verifyGoogleToken(@RequestBody Map<String, String> request) {
        try {
            String idToken = request.get("credential");
            
            if (idToken == null || idToken.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Token Google é obrigatório"));
            }

            System.out.println("=== Google Auth Direct Verify ===");
            System.out.println("Token recebido: " + idToken.substring(0, 50) + "...");

            // Verificar token Google
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(), 
                GsonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(googleClientId))
                .build();

            GoogleIdToken googleIdToken = verifier.verify(idToken);
            
            if (googleIdToken == null) {
                System.err.println("Token Google inválido");
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Token Google inválido"));
            }

            GoogleIdToken.Payload payload = googleIdToken.getPayload();
            
            String email = payload.getEmail();
            String nome = (String) payload.get("name");
            String providerId = payload.getSubject();
            String providerName = "GOOGLE";

            System.out.println("Email: " + email);
            System.out.println("Nome: " + nome);
            System.out.println("Provider ID: " + providerId);

            if (email == null || nome == null || providerId == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Informações incompletas do Google"));
            }

            // Verificar se usuário já existe
            Usuario usuario = usuarioRepository.findByProviderIdAndProviderName(providerId, providerName)
                .orElse(null);

            if (usuario != null) {
                // Usuário existente
                usuario.setUltimoLogin(LocalDateTime.now());
                usuario = usuarioRepository.save(usuario);
                System.out.println("Usuário existente logado: " + email);
            } else {
                // Verificar se existe usuário com mesmo email
                usuario = usuarioRepository.findByEmail(email).orElse(null);
                
                if (usuario != null) {
                    // Vincular conta existente
                    if (usuario.getProviderName() != null) {
                        return ResponseEntity.badRequest()
                            .body(Map.of("error", "Email já está associado a outra conta social"));
                    }
                    
                    usuario.setProviderId(providerId);
                    usuario.setProviderName(providerName);
                    usuario.setTipoAutenticacao(TipoAutenticacao.SOCIAL);
                    usuario.setEmailVerificado(true);
                    usuario.setUltimoLogin(LocalDateTime.now());
                    usuario = usuarioRepository.save(usuario);
                    System.out.println("Conta existente vinculada ao Google: " + email);
                } else {
                    // Criar novo usuário
                    usuario = new Usuario(nome, email, StatusRole.USUARIO, providerId, providerName);
                    usuario.setUltimoLogin(LocalDateTime.now());
                    usuario = usuarioRepository.save(usuario);
                    
                    // Enviar email de boas-vindas
                    emailService.enviarEmailBoasVindas(email, nome);
                    System.out.println("Novo usuário criado: " + email);
                }
            }

            // Gerar token JWT
            String jwtToken = tokenService.gerarToken(usuario);

            return ResponseEntity.ok(Map.of(
                "success", true,
                "token", jwtToken,
                "email", usuario.getEmail(),
                "nome", usuario.getNome(),
                "role", usuario.getRole().name(),
                "message", "Login Google realizado com sucesso!"
            ));

        } catch (Exception e) {
            System.err.println("Erro no Google Auth: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Erro interno: " + e.getMessage()));
        }
    }
}
