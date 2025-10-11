package com.ong.backend.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username:noreply@ong.com}")
    private String fromEmail;

    @Value("${app.email.enabled:true}")
    private boolean emailEnabled;

    @Value("${app.email.mock-mode:false}")
    private boolean mockMode;

    public void enviarEmailOTP(String destinatario, String token) {
        if (!emailEnabled) {
            logger.warn("Email desabilitado. Email OTP não enviado para: {}", destinatario);
            return;
        }

        if (mockMode) {
            logger.info("=== MODO MOCK - EMAIL OTP ===");
            logger.info("Para: {}", destinatario);
            logger.info("CÓDIGO OTP: {}", token);
            logger.info("=============================");
            return;
        }

        try {
            logger.info("Enviando email OTP para: {}", destinatario);
            
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(destinatario);
            message.setSubject("Código de Login - ONG");
            message.setText(construirMensagemOTP(token));
            
            mailSender.send(message);
            logger.info("Email OTP enviado com sucesso para: {}", destinatario);
            
        } catch (Exception e) {
            logger.error("Failed to send OTP email to {}: {}", destinatario, e.getMessage(), e);
            
            // Em desenvolvimento, mostrar o código no log mesmo com erro
            logger.error("=== CÓDIGO OTP (ERRO DE EMAIL) ===");
            logger.error("Email: {}", destinatario);
            logger.error("CÓDIGO: {}", token);
            logger.error("===================================");
            
            throw new RuntimeException("Erro ao enviar email OTP: " + e.getMessage());
        }
    }

    public void enviarEmailBoasVindas(String destinatario, String nome) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(destinatario);
            message.setSubject("Bem-vindo à ONG!");
            message.setText(construirMensagemBoasVindas(nome));
            
            mailSender.send(message);
        } catch (Exception e) {
            // Log do erro, mas não falha o processo de registro
            System.err.println("Erro ao enviar email de boas-vindas: " + e.getMessage());
        }
    }

    public void enviarEmailVerificacao(String destinatario, String nome, String token) {
        if (!emailEnabled) {
            logger.warn("Email desabilitado. Email de verificação não enviado para: {}", destinatario);
            return;
        }

        if (mockMode) {
            logger.info("=== MODO MOCK - EMAIL DE VERIFICAÇÃO ===");
            logger.info("Para: {}", destinatario);
            logger.info("Nome: {}", nome);
            logger.info("CÓDIGO DE VERIFICAÇÃO: {}", token);
            logger.info("========================================");
            return;
        }

        try {
            logger.info("Enviando email de verificação para: {}", destinatario);
            
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(destinatario);
            message.setSubject("Verificação de Email - ONG");
            message.setText(construirMensagemVerificacaoEmail(nome, token));
            
            mailSender.send(message);
            logger.info("Email de verificação enviado com sucesso para: {}", destinatario);
            
        } catch (Exception e) {
            logger.error("Failed to send email to {}: {}", destinatario, e.getMessage(), e);
            
            // Em desenvolvimento, mostrar o código no log mesmo com erro
            logger.error("=== CÓDIGO DE VERIFICAÇÃO (ERRO DE EMAIL) ===");
            logger.error("Email: {}", destinatario);
            logger.error("CÓDIGO: {}", token);
            logger.error("==========================================");
            
            throw new RuntimeException("Erro ao enviar email de verificação: " + e.getMessage());
        }
    }

    public void enviarEmailResetSenha(String destinatario, String nome, String token) {
        if (!emailEnabled) {
            logger.warn("Email desabilitado. Email de reset de senha não enviado para: {}", destinatario);
            return;
        }

        if (mockMode) {
            logger.info("=== MODO MOCK - EMAIL DE RESET DE SENHA ===");
            logger.info("Para: {}", destinatario);
            logger.info("Nome: {}", nome);
            logger.info("CÓDIGO DE RECUPERAÇÃO: {}", token);
            logger.info("==========================================");
            return;
        }

        try {
            logger.info("Enviando email de reset de senha para: {}", destinatario);
            
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(destinatario);
            message.setSubject("Código de Recuperação de Senha - ONG");
            message.setText(construirMensagemResetSenha(nome, token));
            
            mailSender.send(message);
            logger.info("Email de reset de senha enviado com sucesso para: {}", destinatario);
            
        } catch (Exception e) {
            logger.error("Failed to send password reset email to {}: {}", destinatario, e.getMessage(), e);
            
            // Em desenvolvimento, mostrar o código no log mesmo com erro
            logger.error("=== CÓDIGO DE RECUPERAÇÃO (ERRO DE EMAIL) ===");
            logger.error("Email: {}", destinatario);
            logger.error("CÓDIGO: {}", token);
            logger.error("=========================================");
            
            throw new RuntimeException("Erro ao enviar email de reset de senha: " + e.getMessage());
        }
    }

    private String construirMensagemOTP(String token) {
        return String.format(
            "Olá!\n\n" +
            "Você solicitou um código para fazer login sem senha.\n\n" +
            "Seu código de login é: %s\n\n" +
            "Este código é válido por 5 minutos.\n\n" +
            "Para fazer login:\n" +
            "1. Acesse a página de login\n" +
            "2. Digite este código de 6 dígitos\n" +
            "3. Você será logado automaticamente\n\n" +
            "Se você não solicitou este código, ignore este email.\n" +
            "Seu acesso permanece seguro.\n\n" +
            "Atenciosamente,\n" +
            "Equipe ONG",
            token
        );
    }

    private String construirMensagemBoasVindas(String nome) {
        return String.format(
            "Olá %s!\n\n" +
            "Bem-vindo à nossa ONG!\n\n" +
            "Sua conta foi criada com sucesso. Agora você pode acessar todos os nossos serviços.\n\n" +
            "Atenciosamente,\n" +
            "Equipe ONG",
            nome
        );
    }

    private String construirMensagemVerificacaoEmail(String nome, String token) {
        return String.format(
            "Olá %s!\n\n" +
            "Para completar seu cadastro, por favor verifique seu email.\n\n" +
            "Seu código de verificação é: %s\n\n" +
            "Este código é válido por 15 minutos.\n\n" +
            "Se você não criou uma conta em nossa ONG, ignore este email.\n\n" +
            "Atenciosamente,\n" +
            "Equipe ONG",
            nome, token
        );
    }

    private String construirMensagemResetSenha(String nome, String token) {
        return String.format(
            "Olá %s!\n\n" +
            "Você solicitou um código de recuperação de senha para sua conta na ONG.\n\n" +
            "Seu código de recuperação é: %s\n\n" +
            "Este código é válido por 30 minutos.\n\n" +
            "Para redefinir sua senha:\n" +
            "1. Acesse a página de recuperação de senha\n" +
            "2. Digite este código de 6 dígitos\n" +
            "3. Defina sua nova senha\n\n" +
            "Se você não solicitou esta recuperação, ignore este email.\n" +
            "Seu acesso permanecerá seguro.\n\n" +
            "Atenciosamente,\n" +
            "Equipe ONG",
            nome, token
        );
    }
}
