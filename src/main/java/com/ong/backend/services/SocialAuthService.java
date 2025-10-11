package com.ong.backend.services;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.ong.backend.dto.SocialLoginDTO;
import com.ong.backend.entities.StatusRole;
import com.ong.backend.entities.Usuario;
import com.ong.backend.repositories.UsuarioRepository;

@Service
public class SocialAuthService {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EmailService emailService;

    @Transactional
    public Usuario processarLoginSocial(SocialLoginDTO socialLoginDTO) {
        // Validação de campos obrigatórios
        if (socialLoginDTO.getProvider() == null || socialLoginDTO.getProvider().trim().isEmpty()) {
            throw new RuntimeException("Provider é obrigatório");
        }
        
        if (socialLoginDTO.getToken() == null || socialLoginDTO.getToken().trim().isEmpty()) {
            throw new RuntimeException("Token é obrigatório");
        }
        
        String providerId;
        String email;
        String nome;
        String providerName = socialLoginDTO.getProvider().toUpperCase();

        // Validar token baseado no provedor
        switch (providerName) {
            case "GOOGLE":
                GoogleIdToken.Payload payload = validarTokenGoogle(socialLoginDTO.getToken());
                if (payload == null) {
                    throw new RuntimeException("Token Google inválido");
                }
                providerId = payload.getSubject();
                email = payload.getEmail();
                nome = (String) payload.get("name");
                
                // Validação adicional dos dados do Google
                if (email == null || email.trim().isEmpty()) {
                    throw new RuntimeException("Email não encontrado no token Google");
                }
                if (nome == null || nome.trim().isEmpty()) {
                    throw new RuntimeException("Nome não encontrado no token Google");
                }
                break;
                
            case "FACEBOOK":
                // Para Facebook, validar se os campos obrigatórios foram fornecidos
                if (socialLoginDTO.getProviderId() == null || socialLoginDTO.getProviderId().trim().isEmpty()) {
                    throw new RuntimeException("Provider ID é obrigatório para Facebook");
                }
                if (socialLoginDTO.getEmail() == null || socialLoginDTO.getEmail().trim().isEmpty()) {
                    throw new RuntimeException("Email é obrigatório para Facebook");
                }
                if (socialLoginDTO.getNome() == null || socialLoginDTO.getNome().trim().isEmpty()) {
                    throw new RuntimeException("Nome é obrigatório para Facebook");
                }
                
                // TODO: Implementar validação real do token Facebook
                providerId = socialLoginDTO.getProviderId();
                email = socialLoginDTO.getEmail();
                nome = socialLoginDTO.getNome();
                break;
                
            default:
                throw new RuntimeException("Provedor não suportado: " + providerName);
        }

        // Verificar se usuário já existe por providerId
        Optional<Usuario> usuarioExistentePorProvider = 
            usuarioRepository.findByProviderIdAndProviderName(providerId, providerName);
        
        if (usuarioExistentePorProvider.isPresent()) {
            Usuario usuario = usuarioExistentePorProvider.get();
            usuario.setUltimoLogin(LocalDateTime.now());
            return usuarioRepository.save(usuario);
        }

        // Verificar se já existe usuário com este email
        Optional<Usuario> usuarioExistentePorEmail = usuarioRepository.findByEmail(email);
        
        if (usuarioExistentePorEmail.isPresent()) {
            Usuario usuario = usuarioExistentePorEmail.get();
            
            // Se já tem providerName, é conta social duplicada
            if (usuario.getProviderName() != null) {
                throw new RuntimeException("Email já está associado a outra conta social");
            }
            
            // Vincular conta tradicional com social
            usuario.setProviderId(providerId);
            usuario.setProviderName(providerName);
            usuario.setEmailVerificado(true);
            usuario.setUltimoLogin(LocalDateTime.now());
            
            return usuarioRepository.save(usuario);
        }

        // Criar novo usuário social
        Usuario novoUsuario = new Usuario(nome, email, StatusRole.USUARIO, providerId, providerName);
        novoUsuario.setUltimoLogin(LocalDateTime.now());
        
        Usuario usuarioSalvo = usuarioRepository.save(novoUsuario);
        
        // Enviar email de boas-vindas
        emailService.enviarEmailBoasVindas(email, nome);
        
        return usuarioSalvo;
    }

    private GoogleIdToken.Payload validarTokenGoogle(String tokenString) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(), 
                GsonFactory.getDefaultInstance())
                .setAudience(java.util.Collections.singletonList(googleClientId))
                .build();

            GoogleIdToken idToken = verifier.verify(tokenString);
            if (idToken != null) {
                return idToken.getPayload();
            }
            return null;
        } catch (Exception e) {
            System.err.println("Erro ao validar token Google: " + e.getMessage());
            return null;
        }
    }
}
