package com.ong.backend.services;

import java.time.LocalDateTime;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.ong.backend.entities.StatusRole;
import com.ong.backend.entities.TipoAutenticacao;
import com.ong.backend.entities.Usuario;
import com.ong.backend.repositories.UsuarioRepository;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private static final Logger logger = LoggerFactory.getLogger(CustomOAuth2UserService.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        try {
            return processOAuth2User(userRequest, oAuth2User);
        } catch (Exception ex) {
            logger.error("Erro ao processar usuário OAuth2: {}", ex.getMessage(), ex);
            throw new OAuth2AuthenticationException(ex.getMessage());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        String email = oAuth2User.getAttribute("email");
        String nome = oAuth2User.getAttribute("name");
        String googleId = oAuth2User.getAttribute("sub");

        if (email == null || email.isEmpty()) {
            throw new OAuth2AuthenticationException("Email não encontrado no provedor OAuth2");
        }

        logger.info("Processando login OAuth2 para email: {}", email);

        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        Usuario usuario;

        if (usuarioOpt.isPresent()) {
            // Usuário já existe - atualizar informações
            usuario = usuarioOpt.get();
            logger.info("Usuário OAuth2 existente encontrado: {}", email);
            
            // Atualizar informações se necessário
            if (usuario.getTipoAutenticacao() != TipoAutenticacao.SOCIAL) {
                usuario.setTipoAutenticacao(TipoAutenticacao.SOCIAL);
            }
            
            // Atualizar nome se mudou
            if (nome != null && !nome.equals(usuario.getNome())) {
                usuario.setNome(nome);
            }
            
            // Garantir que email está verificado
            if (!usuario.isEmailVerificado()) {
                usuario.setEmailVerificado(true);
            }
            
            usuario.setUltimoLogin(LocalDateTime.now());
            
        } else {
            // Criar novo usuário
            logger.info("Novo usuário OAuth2 criado: {}", email);
            usuario = new Usuario();
            usuario.setNome(nome != null ? nome : "Usuário Google");
            usuario.setEmail(email);
            usuario.setRole(StatusRole.USUARIO);
            usuario.setTipoAutenticacao(TipoAutenticacao.SOCIAL);
            usuario.setEmailVerificado(true); // Email do Google já é verificado
            usuario.setCriadoEm(LocalDateTime.now());
            usuario.setUltimoLogin(LocalDateTime.now());
            // Não precisa de senha para login OAuth2
        }

        usuarioRepository.save(usuario);
        logger.info("Usuário OAuth2 salvo com sucesso: {}", email);

        return oAuth2User;
    }
}
