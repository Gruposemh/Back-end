package com.ong.backend.services;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ong.backend.entities.TokenType;
import com.ong.backend.entities.Usuario;
import com.ong.backend.entities.VerificationToken;
import com.ong.backend.repositories.UsuarioRepository;
import com.ong.backend.repositories.VerificationTokenRepository;

@Service
public class VerificationTokenService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private RateLimitService rateLimitService;

    @Value("${app.verification-token.expiration-minutes:15}")
    private int expirationMinutes;

    @Value("${app.password-reset-token.expiration-minutes:30}")
    private int passwordResetExpirationMinutes;

    private static final int TOKEN_LENGTH = 6;
    private static final SecureRandom random = new SecureRandom();

    @Transactional
    public void gerarEEnviarTokenVerificacaoEmail(String email) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        if (usuarioOpt.isEmpty()) {
            throw new RuntimeException("Usuário não encontrado");
        }

        Usuario usuario = usuarioOpt.get();
        
        if (usuario.isEmailVerificado()) {
            throw new RuntimeException("Email já verificado");
        }

        // Verificar rate limiting (usando OTP como proxy para email)
        if (rateLimitService.isOTPRateLimited(email)) {
            throw new RuntimeException("Muitas tentativas. Tente novamente mais tarde.");
        }

        // Gerar novo token
        String token = gerarTokenNumerico();
        LocalDateTime expiraEm = LocalDateTime.now().plusMinutes(expirationMinutes);

        // Remover tokens antigos do mesmo tipo
        verificationTokenRepository.deleteByUsuarioAndTokenType(usuario, TokenType.EMAIL_VERIFICATION);

        // Criar novo token
        VerificationToken verificationToken = new VerificationToken(token, usuario, TokenType.EMAIL_VERIFICATION, expiraEm);
        verificationTokenRepository.save(verificationToken);

        // Registrar tentativa de rate limiting
        rateLimitService.recordOTPAttempt(email);

        // Enviar por email
        emailService.enviarEmailVerificacao(email, usuario.getNome(), token);
    }

    @Transactional
    public boolean validarTokenVerificacaoEmail(String email, String token) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        if (usuarioOpt.isEmpty()) {
            return false;
        }

        Usuario usuario = usuarioOpt.get();
        
        // Buscar token de verificação
        Optional<VerificationToken> tokenOpt = verificationTokenRepository
                .findLatestByEmailAndTokenType(email, TokenType.EMAIL_VERIFICATION);
        
        if (tokenOpt.isEmpty()) {
            return false;
        }

        VerificationToken verificationToken = tokenOpt.get();
        
        if (!verificationToken.isValid() || !verificationToken.getToken().equals(token)) {
            return false;
        }

        // Marcar email como verificado e token como usado
        usuario.setEmailVerificado(true);
        usuarioRepository.save(usuario);
        
        verificationToken.setUsed(true);
        verificationTokenRepository.save(verificationToken);

        return true;
    }

    @Transactional
    public void gerarEEnviarTokenResetSenha(String email) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        if (usuarioOpt.isEmpty()) {
            throw new RuntimeException("Usuário não encontrado");
        }

        Usuario usuario = usuarioOpt.get();

        // Verificar rate limiting para reset de senha
        if (rateLimitService.isOTPRateLimited(email)) {
            throw new RuntimeException("Muitas tentativas de recuperação. Tente novamente mais tarde.");
        }

        // Gerar novo token
        String token = gerarTokenNumerico();
        LocalDateTime expiraEm = LocalDateTime.now().plusMinutes(passwordResetExpirationMinutes);

        // Remover tokens antigos do mesmo tipo
        verificationTokenRepository.deleteByUsuarioAndTokenType(usuario, TokenType.PASSWORD_RESET);

        // Criar novo token
        VerificationToken verificationToken = new VerificationToken(token, usuario, TokenType.PASSWORD_RESET, expiraEm);
        verificationTokenRepository.save(verificationToken);

        // Registrar tentativa de rate limiting
        rateLimitService.recordOTPAttempt(email);

        // Enviar por email
        emailService.enviarEmailResetSenha(email, usuario.getNome(), token);
    }

    @Transactional
    public boolean validarEResetarSenha(String email, String token, String novaSenha) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        if (usuarioOpt.isEmpty()) {
            return false;
        }

        Usuario usuario = usuarioOpt.get();
        
        // Buscar token de reset de senha
        Optional<VerificationToken> tokenOpt = verificationTokenRepository
                .findLatestByEmailAndTokenType(email, TokenType.PASSWORD_RESET);
        
        if (tokenOpt.isEmpty()) {
            return false;
        }

        VerificationToken verificationToken = tokenOpt.get();
        
        if (!verificationToken.isValid() || !verificationToken.getToken().equals(token)) {
            return false;
        }

        // Atualizar senha e marcar token como usado
        usuario.setSenha(passwordEncoder.encode(novaSenha));
        usuarioRepository.save(usuario);
        
        verificationToken.setUsed(true);
        verificationTokenRepository.save(verificationToken);

        return true;
    }

    @Transactional
    public void limparTokensExpirados() {
        LocalDateTime agora = LocalDateTime.now();
        usuarioRepository.limparTokensExpirados(agora);
    }

    private String gerarTokenNumerico() {
        StringBuilder token = new StringBuilder();
        for (int i = 0; i < TOKEN_LENGTH; i++) {
            token.append(random.nextInt(10));
        }
        return token.toString();
    }
}
