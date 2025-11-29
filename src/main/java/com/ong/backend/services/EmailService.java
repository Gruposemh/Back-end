package com.ong.backend.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.ong.backend.entities.Evento;
import com.ong.backend.entities.Curso;

import jakarta.mail.internet.MimeMessage;
import java.time.format.DateTimeFormatter;

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
            
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(fromEmail);
            helper.setTo(destinatario);
            helper.setSubject("🔐 Verificação de Email - Voluntários Pro Bem");
            helper.setText(construirMensagemVerificacaoEmail(nome, token), true);
            
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
        return String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
            </head>
            <body style="margin: 0; padding: 0; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f4f4f4;">
                <table width="100%%" cellpadding="0" cellspacing="0" style="background-color: #f4f4f4; padding: 20px 0;">
                    <tr>
                        <td align="center">
                            <table width="600" cellpadding="0" cellspacing="0" style="background-color: #ffffff; border-radius: 10px; overflow: hidden; box-shadow: 0 4px 6px rgba(0,0,0,0.1);">
                                <!-- Header -->
                                <tr>
                                    <td style="background: linear-gradient(135deg, #B20000 0%%, #8B0000 100%%); padding: 40px 30px; text-align: center;">
                                        <h1 style="color: #ffffff; margin: 0; font-size: 28px; font-weight: bold;">🔐 Verificação de Email</h1>
                                    </td>
                                </tr>
                                
                                <!-- Body -->
                                <tr>
                                    <td style="padding: 40px 30px;">
                                        <p style="color: #333; font-size: 16px; line-height: 1.6; margin: 0 0 20px 0;">
                                            Olá <strong>%s</strong>,
                                        </p>
                                        
                                        <p style="color: #333; font-size: 16px; line-height: 1.6; margin: 0 0 30px 0;">
                                            Bem-vindo à <strong>Voluntários Pro Bem</strong>! Para completar seu cadastro, por favor verifique seu email usando o código abaixo:
                                        </p>
                                        
                                        <!-- Code Box -->
                                        <table width="100%%" cellpadding="0" cellspacing="0" style="margin-bottom: 30px;">
                                            <tr>
                                                <td align="center">
                                                    <table cellpadding="0" cellspacing="0" style="background: linear-gradient(135deg, #f8f9fa 0%%, #e9ecef 100%%); border: 2px dashed #B20000; border-radius: 10px; padding: 25px;">
                                                        <tr>
                                                            <td align="center">
                                                                <p style="color: #6c757d; font-size: 14px; margin: 0 0 10px 0; text-transform: uppercase; letter-spacing: 1px;">Seu Código de Verificação</p>
                                                                <p style="color: #B20000; font-size: 36px; font-weight: bold; margin: 0; letter-spacing: 8px; font-family: 'Courier New', monospace;">%s</p>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr>
                                        </table>
                                        
                                        <!-- Instructions -->
                                        <table width="100%%" cellpadding="0" cellspacing="0" style="background-color: #FFF3CD; border-radius: 8px; margin-bottom: 30px;">
                                            <tr>
                                                <td style="padding: 20px;">
                                                    <p style="color: #856404; font-size: 14px; line-height: 1.6; margin: 0 0 10px 0;">
                                                        <strong>⏰ Atenção:</strong> Este código é válido por <strong>15 minutos</strong>.
                                                    </p>
                                                    <p style="color: #856404; font-size: 14px; line-height: 1.6; margin: 0;">
                                                        Digite este código na tela de verificação para ativar sua conta.
                                                    </p>
                                                </td>
                                            </tr>
                                        </table>
                                        
                                        <p style="color: #6c757d; font-size: 14px; line-height: 1.6; margin: 0; text-align: center;">
                                            Se você não criou uma conta em nossa ONG, ignore este email com segurança.
                                        </p>
                                    </td>
                                </tr>
                                
                                <!-- Footer -->
                                <tr>
                                    <td style="background-color: #f8f9fa; padding: 30px; text-align: center; border-top: 1px solid #e9ecef;">
                                        <p style="color: #6c757d; font-size: 14px; margin: 0 0 10px 0;">
                                            <strong>Voluntários Pro Bem</strong>
                                        </p>
                                        <p style="color: #6c757d; font-size: 12px; margin: 0;">
                                            Fazendo o bem, fazendo a diferença.
                                        </p>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </body>
            </html>
            """,
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

    // ==================== NOVOS MÉTODOS COM HTML ====================

    public void enviarEmailInscricaoEvento(String destinatario, String nomeUsuario, Evento evento) {
        if (!emailEnabled) {
            logger.warn("Email desabilitado. Email de inscrição em evento não enviado para: {}", destinatario);
            return;
        }

        if (mockMode) {
            logger.info("=== MODO MOCK - EMAIL DE INSCRIÇÃO EM EVENTO ===");
            logger.info("Para: {}", destinatario);
            logger.info("Usuário: {}", nomeUsuario);
            logger.info("Evento: {}", evento.getNome());
            logger.info("===============================================");
            return;
        }

        try {
            logger.info("Enviando email de inscrição em evento para: {}", destinatario);
            
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(fromEmail);
            helper.setTo(destinatario);
            helper.setSubject("✅ Confirmação de Presença no Evento - " + evento.getNome());
            helper.setText(construirEmailInscricaoEvento(nomeUsuario, evento), true);
            
            mailSender.send(message);
            logger.info("Email de inscrição em evento enviado com sucesso para: {}", destinatario);
            
        } catch (Exception e) {
            logger.error("Erro ao enviar email de inscrição em evento para {}: {}", destinatario, e.getMessage(), e);
        }
    }

    public void enviarEmailCancelamentoEvento(String destinatario, String nomeUsuario, Evento evento) {
        enviarEmailCancelamentoEvento(destinatario, nomeUsuario, evento, false);
    }

    public void enviarEmailCancelamentoEvento(String destinatario, String nomeUsuario, Evento evento, boolean removidoPorAdmin) {
        if (!emailEnabled) {
            logger.warn("Email desabilitado. Email de cancelamento de evento não enviado para: {}", destinatario);
            return;
        }

        if (mockMode) {
            logger.info("=== MODO MOCK - EMAIL DE CANCELAMENTO DE EVENTO ===");
            logger.info("Para: {}", destinatario);
            logger.info("Usuário: {}", nomeUsuario);
            logger.info("Evento: {}", evento.getNome());
            logger.info("==================================================");
            return;
        }

        try {
            logger.info("Enviando email de cancelamento de evento para: {}", destinatario);
            
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(fromEmail);
            helper.setTo(destinatario);
            
            if (removidoPorAdmin) {
                helper.setSubject("Remoção de Inscrição no Evento - " + evento.getNome());
                helper.setText(construirEmailRemocaoAdminEvento(nomeUsuario, evento), true);
            } else {
                helper.setSubject("Cancelamento de Presença no Evento - " + evento.getNome());
                helper.setText(construirEmailCancelamentoEvento(nomeUsuario, evento), true);
            }
            
            mailSender.send(message);
            logger.info("Email de cancelamento de evento enviado com sucesso para: {}", destinatario);
            
        } catch (Exception e) {
            logger.error("Erro ao enviar email de cancelamento de evento para {}: {}", destinatario, e.getMessage(), e);
        }
    }

    public void enviarEmailInscricaoAtividade(String destinatario, String nomeUsuario, Curso atividade) {
        if (!emailEnabled) {
            logger.warn("Email desabilitado. Email de inscrição em atividade não enviado para: {}", destinatario);
            return;
        }

        if (mockMode) {
            logger.info("=== MODO MOCK - EMAIL DE INSCRIÇÃO EM ATIVIDADE ===");
            logger.info("Para: {}", destinatario);
            logger.info("Usuário: {}", nomeUsuario);
            logger.info("Atividade: {}", atividade.getTitulo());
            logger.info("==================================================");
            return;
        }

        try {
            logger.info("Enviando email de inscrição em atividade para: {}", destinatario);
            
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(fromEmail);
            helper.setTo(destinatario);
            helper.setSubject("✅ Inscrição Confirmada na Atividade - " + atividade.getTitulo());
            helper.setText(construirEmailInscricaoAtividade(nomeUsuario, atividade), true);
            
            mailSender.send(message);
            logger.info("Email de inscrição em atividade enviado com sucesso para: {}", destinatario);
            
        } catch (Exception e) {
            logger.error("Erro ao enviar email de inscrição em atividade para {}: {}", destinatario, e.getMessage(), e);
        }
    }

    public void enviarEmailCancelamentoAtividade(String destinatario, String nomeUsuario, Curso atividade) {
        enviarEmailCancelamentoAtividade(destinatario, nomeUsuario, atividade, false);
    }

    public void enviarEmailCancelamentoAtividade(String destinatario, String nomeUsuario, Curso atividade, boolean removidoPorAdmin) {
        if (!emailEnabled) {
            logger.warn("Email desabilitado. Email de cancelamento de atividade não enviado para: {}", destinatario);
            return;
        }

        if (mockMode) {
            logger.info("=== MODO MOCK - EMAIL DE CANCELAMENTO DE ATIVIDADE ===");
            logger.info("Para: {}", destinatario);
            logger.info("Usuário: {}", nomeUsuario);
            logger.info("Atividade: {}", atividade.getTitulo());
            logger.info("=====================================================");
            return;
        }

        try {
            logger.info("Enviando email de cancelamento de atividade para: {}", destinatario);
            
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(fromEmail);
            helper.setTo(destinatario);
            
            if (removidoPorAdmin) {
                helper.setSubject("Remoção de Inscrição na Atividade - " + atividade.getTitulo());
                helper.setText(construirEmailRemocaoAdminAtividade(nomeUsuario, atividade), true);
            } else {
                helper.setSubject("Cancelamento de Inscrição na Atividade - " + atividade.getTitulo());
                helper.setText(construirEmailCancelamentoAtividade(nomeUsuario, atividade), true);
            }
            
            mailSender.send(message);
            logger.info("Email de cancelamento de atividade enviado com sucesso para: {}", destinatario);
            
        } catch (Exception e) {
            logger.error("Erro ao enviar email de cancelamento de atividade para {}: {}", destinatario, e.getMessage(), e);
        }
    }


    // ==================== TEMPLATES HTML ====================

    private String construirEmailInscricaoEvento(String nomeUsuario, Evento evento) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");
        String dataFormatada = evento.getData() != null ? evento.getData().format(formatter) : "A definir";
        
        return String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
            </head>
            <body style="margin: 0; padding: 0; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f4f4f4;">
                <table width="100%%" cellpadding="0" cellspacing="0" style="background-color: #f4f4f4; padding: 20px 0;">
                    <tr>
                        <td align="center">
                            <table width="600" cellpadding="0" cellspacing="0" style="background-color: #ffffff; border-radius: 10px; overflow: hidden; box-shadow: 0 4px 6px rgba(0,0,0,0.1);">
                                <!-- Header -->
                                <tr>
                                    <td style="background: linear-gradient(135deg, #B20000 0%%, #8B0000 100%%); padding: 40px 30px; text-align: center;">
                                        <h1 style="color: #ffffff; margin: 0; font-size: 28px; font-weight: bold;">✅ Presença Confirmada!</h1>
                                    </td>
                                </tr>
                                
                                <!-- Body -->
                                <tr>
                                    <td style="padding: 40px 30px;">
                                        <p style="color: #333; font-size: 16px; line-height: 1.6; margin: 0 0 20px 0;">
                                            Olá <strong>%s</strong>,
                                        </p>
                                        
                                        <p style="color: #333; font-size: 16px; line-height: 1.6; margin: 0 0 30px 0;">
                                            Sua presença foi confirmada com sucesso no evento! Estamos muito felizes em contar com você.
                                        </p>
                                        
                                        <!-- Event Card -->
                                        <table width="100%%" cellpadding="0" cellspacing="0" style="background-color: #f8f9fa; border-left: 4px solid #B20000; border-radius: 8px; margin-bottom: 30px;">
                                            <tr>
                                                <td style="padding: 25px;">
                                                    <h2 style="color: #B20000; margin: 0 0 15px 0; font-size: 22px;">%s</h2>
                                                    <p style="color: #555; font-size: 15px; line-height: 1.6; margin: 0 0 15px 0;">%s</p>
                                                    
                                                    <table width="100%%" cellpadding="0" cellspacing="0">
                                                        <tr>
                                                            <td style="padding: 8px 0;">
                                                                <span style="color: #B20000; font-weight: bold;">📅 Data:</span>
                                                                <span style="color: #333; margin-left: 10px;">%s</span>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td style="padding: 8px 0;">
                                                                <span style="color: #B20000; font-weight: bold;">📍 Local:</span>
                                                                <span style="color: #333; margin-left: 10px;">%s</span>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr>
                                        </table>
                                        
                                        <!-- Important Info -->
                                        <table width="100%%" cellpadding="0" cellspacing="0" style="background-color: #FFF3CD; border-radius: 8px; margin-bottom: 30px;">
                                            <tr>
                                                <td style="padding: 20px;">
                                                    <p style="color: #856404; font-size: 14px; line-height: 1.6; margin: 0;">
                                                        <strong>⚠️ Importante:</strong> Por favor, chegue com 15 minutos de antecedência. 
                                                        Em caso de imprevistos, entre em contato conosco o quanto antes.
                                                    </p>
                                                </td>
                                            </tr>
                                        </table>
                                        
                                        <p style="color: #333; font-size: 16px; line-height: 1.6; margin: 0;">
                                            Contamos com sua participação! Juntos fazemos a diferença. ❤️
                                        </p>
                                    </td>
                                </tr>
                                
                                <!-- Footer -->
                                <tr>
                                    <td style="background-color: #f8f9fa; padding: 30px; text-align: center; border-top: 1px solid #e9ecef;">
                                        <p style="color: #6c757d; font-size: 14px; margin: 0 0 10px 0;">
                                            <strong>Voluntários Pro Bem</strong>
                                        </p>
                                        <p style="color: #6c757d; font-size: 12px; margin: 0;">
                                            Fazendo o bem, fazendo a diferença.
                                        </p>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </body>
            </html>
            """,
            nomeUsuario,
            evento.getNome(),
            evento.getDescricao() != null ? evento.getDescricao() : "Descrição não disponível",
            dataFormatada,
            evento.getLocal() != null ? evento.getLocal() : "A definir"
        );
    }

    private String construirEmailCancelamentoEvento(String nomeUsuario, Evento evento) {
        return String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
            </head>
            <body style="margin: 0; padding: 0; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f4f4f4;">
                <table width="100%%" cellpadding="0" cellspacing="0" style="background-color: #f4f4f4; padding: 20px 0;">
                    <tr>
                        <td align="center">
                            <table width="600" cellpadding="0" cellspacing="0" style="background-color: #ffffff; border-radius: 10px; overflow: hidden; box-shadow: 0 4px 6px rgba(0,0,0,0.1);">
                                <!-- Header -->
                                <tr>
                                    <td style="background: linear-gradient(135deg, #6c757d 0%%, #495057 100%%); padding: 40px 30px; text-align: center;">
                                        <h1 style="color: #ffffff; margin: 0; font-size: 28px; font-weight: bold;">❌ Presença Cancelada</h1>
                                    </td>
                                </tr>
                                
                                <!-- Body -->
                                <tr>
                                    <td style="padding: 40px 30px;">
                                        <p style="color: #333; font-size: 16px; line-height: 1.6; margin: 0 0 20px 0;">
                                            Olá <strong>%s</strong>,
                                        </p>
                                        
                                        <p style="color: #333; font-size: 16px; line-height: 1.6; margin: 0 0 30px 0;">
                                            Sua presença no evento foi cancelada conforme solicitado.
                                        </p>
                                        
                                        <!-- Event Card -->
                                        <table width="100%%" cellpadding="0" cellspacing="0" style="background-color: #f8f9fa; border-left: 4px solid #6c757d; border-radius: 8px; margin-bottom: 30px;">
                                            <tr>
                                                <td style="padding: 25px;">
                                                    <h2 style="color: #495057; margin: 0 0 15px 0; font-size: 22px;">%s</h2>
                                                    <p style="color: #6c757d; font-size: 14px; margin: 0;">
                                                        Sua inscrição foi removida com sucesso.
                                                    </p>
                                                </td>
                                            </tr>
                                        </table>
                                        
                                        <p style="color: #333; font-size: 16px; line-height: 1.6; margin: 0 0 15px 0;">
                                            Sentiremos sua falta, mas compreendemos que imprevistos acontecem.
                                        </p>
                                        
                                        <p style="color: #333; font-size: 16px; line-height: 1.6; margin: 0;">
                                            Você pode se inscrever novamente em outros eventos quando desejar. Esperamos contar com você em breve! 💙
                                        </p>
                                    </td>
                                </tr>
                                
                                <!-- Footer -->
                                <tr>
                                    <td style="background-color: #f8f9fa; padding: 30px; text-align: center; border-top: 1px solid #e9ecef;">
                                        <p style="color: #6c757d; font-size: 14px; margin: 0 0 10px 0;">
                                            <strong>Voluntários Pro Bem</strong>
                                        </p>
                                        <p style="color: #6c757d; font-size: 12px; margin: 0;">
                                            Fazendo o bem, fazendo a diferença.
                                        </p>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </body>
            </html>
            """,
            nomeUsuario,
            evento.getNome()
        );
    }

    private String construirEmailInscricaoAtividade(String nomeUsuario, Curso atividade) {
        return String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
            </head>
            <body style="margin: 0; padding: 0; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f4f4f4;">
                <table width="100%%" cellpadding="0" cellspacing="0" style="background-color: #f4f4f4; padding: 20px 0;">
                    <tr>
                        <td align="center">
                            <table width="600" cellpadding="0" cellspacing="0" style="background-color: #ffffff; border-radius: 10px; overflow: hidden; box-shadow: 0 4px 6px rgba(0,0,0,0.1);">
                                <!-- Header -->
                                <tr>
                                    <td style="background: linear-gradient(135deg, #B20000 0%%, #8B0000 100%%); padding: 40px 30px; text-align: center;">
                                        <h1 style="color: #ffffff; margin: 0; font-size: 28px; font-weight: bold;">✅ Inscrição Confirmada!</h1>
                                    </td>
                                </tr>
                                
                                <!-- Body -->
                                <tr>
                                    <td style="padding: 40px 30px;">
                                        <p style="color: #333; font-size: 16px; line-height: 1.6; margin: 0 0 20px 0;">
                                            Olá <strong>%s</strong>,
                                        </p>
                                        
                                        <p style="color: #333; font-size: 16px; line-height: 1.6; margin: 0 0 30px 0;">
                                            Sua inscrição na atividade foi confirmada com sucesso! Estamos animados para ter você conosco.
                                        </p>
                                        
                                        <!-- Activity Card -->
                                        <table width="100%%" cellpadding="0" cellspacing="0" style="background-color: #f8f9fa; border-left: 4px solid #B20000; border-radius: 8px; margin-bottom: 30px;">
                                            <tr>
                                                <td style="padding: 25px;">
                                                    <h2 style="color: #B20000; margin: 0 0 15px 0; font-size: 22px;">%s</h2>
                                                    <p style="color: #555; font-size: 15px; line-height: 1.6; margin: 0 0 15px 0;">%s</p>
                                                    
                                                    <table width="100%%" cellpadding="0" cellspacing="0">
                                                        <tr>
                                                            <td style="padding: 8px 0;">
                                                                <span style="color: #B20000; font-weight: bold;">📅 Dias:</span>
                                                                <span style="color: #333; margin-left: 10px;">%s</span>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td style="padding: 8px 0;">
                                                                <span style="color: #B20000; font-weight: bold;">🕐 Horário:</span>
                                                                <span style="color: #333; margin-left: 10px;">%s</span>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr>
                                        </table>
                                        
                                        <!-- Important Info -->
                                        <table width="100%%" cellpadding="0" cellspacing="0" style="background-color: #D1ECF1; border-radius: 8px; margin-bottom: 30px;">
                                            <tr>
                                                <td style="padding: 20px;">
                                                    <p style="color: #0C5460; font-size: 14px; line-height: 1.6; margin: 0;">
                                                        <strong>💡 Dica:</strong> Verifique sua agenda regularmente para não perder nenhuma atividade. 
                                                        Sua participação é muito importante para nós!
                                                    </p>
                                                </td>
                                            </tr>
                                        </table>
                                        
                                        <p style="color: #333; font-size: 16px; line-height: 1.6; margin: 0;">
                                            Obrigado por fazer parte da nossa comunidade! Juntos fazemos a diferença. ❤️
                                        </p>
                                    </td>
                                </tr>
                                
                                <!-- Footer -->
                                <tr>
                                    <td style="background-color: #f8f9fa; padding: 30px; text-align: center; border-top: 1px solid #e9ecef;">
                                        <p style="color: #6c757d; font-size: 14px; margin: 0 0 10px 0;">
                                            <strong>Voluntários Pro Bem</strong>
                                        </p>
                                        <p style="color: #6c757d; font-size: 12px; margin: 0;">
                                            Fazendo o bem, fazendo a diferença.
                                        </p>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </body>
            </html>
            """,
            nomeUsuario,
            atividade.getTitulo(),
            atividade.getDescricao() != null ? atividade.getDescricao() : "Descrição não disponível",
            atividade.getDias() != null ? atividade.getDias() : "A definir",
            atividade.getHorario() != null ? atividade.getHorario().toString() : "A definir"
        );
    }

    private String construirEmailCancelamentoAtividade(String nomeUsuario, Curso atividade) {
        return String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
            </head>
            <body style="margin: 0; padding: 0; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f4f4f4;">
                <table width="100%%" cellpadding="0" cellspacing="0" style="background-color: #f4f4f4; padding: 20px 0;">
                    <tr>
                        <td align="center">
                            <table width="600" cellpadding="0" cellspacing="0" style="background-color: #ffffff; border-radius: 10px; overflow: hidden; box-shadow: 0 4px 6px rgba(0,0,0,0.1);">
                                <!-- Header -->
                                <tr>
                                    <td style="background: linear-gradient(135deg, #6c757d 0%%, #495057 100%%); padding: 40px 30px; text-align: center;">
                                        <h1 style="color: #ffffff; margin: 0; font-size: 28px; font-weight: bold;">❌ Inscrição Cancelada</h1>
                                    </td>
                                </tr>
                                
                                <!-- Body -->
                                <tr>
                                    <td style="padding: 40px 30px;">
                                        <p style="color: #333; font-size: 16px; line-height: 1.6; margin: 0 0 20px 0;">
                                            Olá <strong>%s</strong>,
                                        </p>
                                        
                                        <p style="color: #333; font-size: 16px; line-height: 1.6; margin: 0 0 30px 0;">
                                            Sua inscrição na atividade foi cancelada conforme solicitado.
                                        </p>
                                        
                                        <!-- Activity Card -->
                                        <table width="100%%" cellpadding="0" cellspacing="0" style="background-color: #f8f9fa; border-left: 4px solid #6c757d; border-radius: 8px; margin-bottom: 30px;">
                                            <tr>
                                                <td style="padding: 25px;">
                                                    <h2 style="color: #495057; margin: 0 0 15px 0; font-size: 22px;">%s</h2>
                                                    <p style="color: #6c757d; font-size: 14px; margin: 0;">
                                                        Sua inscrição foi removida com sucesso.
                                                    </p>
                                                </td>
                                            </tr>
                                        </table>
                                        
                                        <p style="color: #333; font-size: 16px; line-height: 1.6; margin: 0 0 15px 0;">
                                            Sentiremos sua falta, mas compreendemos que imprevistos acontecem.
                                        </p>
                                        
                                        <p style="color: #333; font-size: 16px; line-height: 1.6; margin: 0;">
                                            Você pode se inscrever novamente em outras atividades quando desejar. Esperamos contar com você em breve! 💙
                                        </p>
                                    </td>
                                </tr>
                                
                                <!-- Footer -->
                                <tr>
                                    <td style="background-color: #f8f9fa; padding: 30px; text-align: center; border-top: 1px solid #e9ecef;">
                                        <p style="color: #6c757d; font-size: 14px; margin: 0 0 10px 0;">
                                            <strong>Voluntários Pro Bem</strong>
                                        </p>
                                        <p style="color: #6c757d; font-size: 12px; margin: 0;">
                                            Fazendo o bem, fazendo a diferença.
                                        </p>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </body>
            </html>
            """,
            nomeUsuario,
            atividade.getTitulo()
        );
    }

    private String construirEmailRemocaoAdminEvento(String nomeUsuario, Evento evento) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");
        String dataFormatada = evento.getData() != null ? evento.getData().format(formatter) : "A definir";
        
        return String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
            </head>
            <body style="margin: 0; padding: 0; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f4f4f4;">
                <table width="100%%" cellpadding="0" cellspacing="0" style="background-color: #f4f4f4; padding: 20px 0;">
                    <tr>
                        <td align="center">
                            <table width="600" cellpadding="0" cellspacing="0" style="background-color: #ffffff; border-radius: 12px; overflow: hidden; box-shadow: 0 8px 16px rgba(0,0,0,0.1);">
                                <!-- Header -->
                                <tr>
                                    <td style="background: linear-gradient(135deg, #dc3545 0%%, #c82333 100%%); padding: 45px 35px; text-align: center;">
                                        <div style="width: 70px; height: 70px; background-color: rgba(255,255,255,0.2); border-radius: 50%%; margin: 0 auto 20px; display: flex; align-items: center; justify-content: center;">
                                            <svg width="40" height="40" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                                                <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm1 15h-2v-2h2v2zm0-4h-2V7h2v6z" fill="white"/>
                                            </svg>
                                        </div>
                                        <h1 style="color: #ffffff; margin: 0; font-size: 26px; font-weight: 600; letter-spacing: -0.5px;">Remoção de Inscrição</h1>
                                    </td>
                                </tr>
                                
                                <!-- Body -->
                                <tr>
                                    <td style="padding: 45px 35px;">
                                        <p style="color: #2c3e50; font-size: 17px; line-height: 1.7; margin: 0 0 25px 0;">
                                            Olá <strong style="color: #B20000;">%s</strong>,
                                        </p>
                                        
                                        <p style="color: #2c3e50; font-size: 17px; line-height: 1.7; margin: 0 0 30px 0;">
                                            Informamos que sua inscrição no evento abaixo foi <strong>removida por um administrador</strong> da ONG.
                                        </p>
                                        
                                        <!-- Event Card -->
                                        <table width="100%%" cellpadding="0" cellspacing="0" style="background: linear-gradient(135deg, #f8f9fa 0%%, #e9ecef 100%%); border-left: 5px solid #dc3545; border-radius: 10px; margin-bottom: 30px; box-shadow: 0 2px 8px rgba(0,0,0,0.05);">
                                            <tr>
                                                <td style="padding: 28px;">
                                                    <h2 style="color: #dc3545; margin: 0 0 18px 0; font-size: 22px; font-weight: 600;">%s</h2>
                                                    <p style="color: #6c757d; font-size: 15px; line-height: 1.6; margin: 0 0 18px 0;">%s</p>
                                                    
                                                    <table width="100%%" cellpadding="0" cellspacing="0" style="border-top: 1px solid #dee2e6; padding-top: 15px;">
                                                        <tr>
                                                            <td style="padding: 10px 0;">
                                                                <span style="color: #dc3545; font-weight: 600; font-size: 14px;">📅 Data:</span>
                                                                <span style="color: #495057; margin-left: 12px; font-size: 15px;">%s</span>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td style="padding: 10px 0;">
                                                                <span style="color: #dc3545; font-weight: 600; font-size: 14px;">📍 Local:</span>
                                                                <span style="color: #495057; margin-left: 12px; font-size: 15px;">%s</span>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr>
                                        </table>
                                        
                                        <!-- Info Box -->
                                        <table width="100%%" cellpadding="0" cellspacing="0" style="background-color: #fff3cd; border-left: 4px solid #ffc107; border-radius: 8px; margin-bottom: 30px;">
                                            <tr>
                                                <td style="padding: 22px;">
                                                    <p style="color: #856404; font-size: 15px; line-height: 1.7; margin: 0;">
                                                        <strong>ℹ️ Motivo:</strong> Esta remoção pode ter ocorrido por ajustes na organização do evento, 
                                                        limite de vagas ou outros motivos administrativos.
                                                    </p>
                                                </td>
                                            </tr>
                                        </table>
                                        
                                        <p style="color: #2c3e50; font-size: 16px; line-height: 1.7; margin: 0 0 15px 0;">
                                            Se você tiver dúvidas sobre esta remoção, entre em contato conosco. Você pode se inscrever em outros eventos disponíveis.
                                        </p>
                                        
                                        <p style="color: #2c3e50; font-size: 16px; line-height: 1.7; margin: 0;">
                                            Agradecemos sua compreensão e esperamos contar com você em futuras oportunidades.
                                        </p>
                                    </td>
                                </tr>
                                
                                <!-- Footer -->
                                <tr>
                                    <td style="background: linear-gradient(135deg, #f8f9fa 0%%, #e9ecef 100%%); padding: 35px; text-align: center; border-top: 2px solid #dee2e6;">
                                        <p style="color: #495057; font-size: 16px; font-weight: 600; margin: 0 0 8px 0;">
                                            Voluntários Pro Bem
                                        </p>
                                        <p style="color: #6c757d; font-size: 13px; margin: 0; letter-spacing: 0.3px;">
                                            Fazendo o bem, fazendo a diferença
                                        </p>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </body>
            </html>
            """,
            nomeUsuario,
            evento.getNome(),
            evento.getDescricao() != null ? evento.getDescricao() : "Descrição não disponível",
            dataFormatada,
            evento.getLocal() != null ? evento.getLocal() : "A definir"
        );
    }

    private String construirEmailRemocaoAdminAtividade(String nomeUsuario, Curso atividade) {
        return String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
            </head>
            <body style="margin: 0; padding: 0; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f4f4f4;">
                <table width="100%%" cellpadding="0" cellspacing="0" style="background-color: #f4f4f4; padding: 20px 0;">
                    <tr>
                        <td align="center">
                            <table width="600" cellpadding="0" cellspacing="0" style="background-color: #ffffff; border-radius: 12px; overflow: hidden; box-shadow: 0 8px 16px rgba(0,0,0,0.1);">
                                <!-- Header -->
                                <tr>
                                    <td style="background: linear-gradient(135deg, #dc3545 0%%, #c82333 100%%); padding: 45px 35px; text-align: center;">
                                        <div style="width: 70px; height: 70px; background-color: rgba(255,255,255,0.2); border-radius: 50%%; margin: 0 auto 20px; display: flex; align-items: center; justify-content: center;">
                                            <svg width="40" height="40" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                                                <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm1 15h-2v-2h2v2zm0-4h-2V7h2v6z" fill="white"/>
                                            </svg>
                                        </div>
                                        <h1 style="color: #ffffff; margin: 0; font-size: 26px; font-weight: 600; letter-spacing: -0.5px;">Remoção de Inscrição</h1>
                                    </td>
                                </tr>
                                
                                <!-- Body -->
                                <tr>
                                    <td style="padding: 45px 35px;">
                                        <p style="color: #2c3e50; font-size: 17px; line-height: 1.7; margin: 0 0 25px 0;">
                                            Olá <strong style="color: #B20000;">%s</strong>,
                                        </p>
                                        
                                        <p style="color: #2c3e50; font-size: 17px; line-height: 1.7; margin: 0 0 30px 0;">
                                            Informamos que sua inscrição na atividade abaixo foi <strong>removida por um administrador</strong> da ONG.
                                        </p>
                                        
                                        <!-- Activity Card -->
                                        <table width="100%%" cellpadding="0" cellspacing="0" style="background: linear-gradient(135deg, #f8f9fa 0%%, #e9ecef 100%%); border-left: 5px solid #dc3545; border-radius: 10px; margin-bottom: 30px; box-shadow: 0 2px 8px rgba(0,0,0,0.05);">
                                            <tr>
                                                <td style="padding: 28px;">
                                                    <h2 style="color: #dc3545; margin: 0 0 18px 0; font-size: 22px; font-weight: 600;">%s</h2>
                                                    <p style="color: #6c757d; font-size: 15px; line-height: 1.6; margin: 0 0 18px 0;">%s</p>
                                                    
                                                    <table width="100%%" cellpadding="0" cellspacing="0" style="border-top: 1px solid #dee2e6; padding-top: 15px;">
                                                        <tr>
                                                            <td style="padding: 10px 0;">
                                                                <span style="color: #dc3545; font-weight: 600; font-size: 14px;">📅 Dias:</span>
                                                                <span style="color: #495057; margin-left: 12px; font-size: 15px;">%s</span>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td style="padding: 10px 0;">
                                                                <span style="color: #dc3545; font-weight: 600; font-size: 14px;">🕐 Horário:</span>
                                                                <span style="color: #495057; margin-left: 12px; font-size: 15px;">%s</span>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr>
                                        </table>
                                        
                                        <!-- Info Box -->
                                        <table width="100%%" cellpadding="0" cellspacing="0" style="background-color: #fff3cd; border-left: 4px solid #ffc107; border-radius: 8px; margin-bottom: 30px;">
                                            <tr>
                                                <td style="padding: 22px;">
                                                    <p style="color: #856404; font-size: 15px; line-height: 1.7; margin: 0;">
                                                        <strong>ℹ️ Motivo:</strong> Esta remoção pode ter ocorrido por ajustes na organização da atividade, 
                                                        limite de vagas ou outros motivos administrativos.
                                                    </p>
                                                </td>
                                            </tr>
                                        </table>
                                        
                                        <p style="color: #2c3e50; font-size: 16px; line-height: 1.7; margin: 0 0 15px 0;">
                                            Se você tiver dúvidas sobre esta remoção, entre em contato conosco. Você pode se inscrever em outras atividades disponíveis.
                                        </p>
                                        
                                        <p style="color: #2c3e50; font-size: 16px; line-height: 1.7; margin: 0;">
                                            Agradecemos sua compreensão e esperamos contar com você em futuras oportunidades.
                                        </p>
                                    </td>
                                </tr>
                                
                                <!-- Footer -->
                                <tr>
                                    <td style="background: linear-gradient(135deg, #f8f9fa 0%%, #e9ecef 100%%); padding: 35px; text-align: center; border-top: 2px solid #dee2e6;">
                                        <p style="color: #495057; font-size: 16px; font-weight: 600; margin: 0 0 8px 0;">
                                            Voluntários Pro Bem
                                        </p>
                                        <p style="color: #6c757d; font-size: 13px; margin: 0; letter-spacing: 0.3px;">
                                            Fazendo o bem, fazendo a diferença
                                        </p>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </body>
            </html>
            """,
            nomeUsuario,
            atividade.getTitulo(),
            atividade.getDescricao() != null ? atividade.getDescricao() : "Descrição não disponível",
            atividade.getDias() != null ? atividade.getDias() : "A definir",
            atividade.getHorario() != null ? atividade.getHorario().toString() : "A definir"
        );
    }

    // ==================== EMAILS DE VOLUNTARIADO ====================
    
    public void enviarEmailPedidoVoluntario(String destinatario, String nome) {
        if (!emailEnabled) {
            logger.warn("Email desabilitado. Email de pedido de voluntário não enviado para: {}", destinatario);
            return;
        }

        if (mockMode) {
            logger.info("=== MODO MOCK - EMAIL DE PEDIDO DE VOLUNTÁRIO ===");
            logger.info("Para: {}", destinatario);
            logger.info("Nome: {}", nome);
            logger.info("================================================");
            return;
        }

        try {
            logger.info("Enviando email de pedido de voluntário para: {}", destinatario);
            
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(fromEmail);
            helper.setTo(destinatario);
            helper.setSubject("Pedido de Voluntariado Recebido - Voluntários Pro Bem");
            helper.setText(construirEmailPedidoVoluntario(nome), true);
            
            mailSender.send(message);
            logger.info("Email de pedido de voluntário enviado com sucesso para: {}", destinatario);
            
        } catch (Exception e) {
            logger.error("Erro ao enviar email de pedido de voluntário para {}: {}", destinatario, e.getMessage(), e);
        }
    }
    
    public void enviarEmailVoluntarioAprovado(String destinatario, String nome) {
        if (!emailEnabled) {
            logger.warn("Email desabilitado. Email de aprovação de voluntário não enviado para: {}", destinatario);
            return;
        }

        if (mockMode) {
            logger.info("=== MODO MOCK - EMAIL DE APROVAÇÃO DE VOLUNTÁRIO ===");
            logger.info("Para: {}", destinatario);
            logger.info("Nome: {}", nome);
            logger.info("===================================================");
            return;
        }

        try {
            logger.info("Enviando email de aprovação de voluntário para: {}", destinatario);
            
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(fromEmail);
            helper.setTo(destinatario);
            helper.setSubject("🎉 Você foi aprovado como Voluntário! - Voluntários Pro Bem");
            helper.setText(construirEmailVoluntarioAprovado(nome), true);
            
            mailSender.send(message);
            logger.info("Email de aprovação de voluntário enviado com sucesso para: {}", destinatario);
            
        } catch (Exception e) {
            logger.error("Erro ao enviar email de aprovação de voluntário para {}: {}", destinatario, e.getMessage(), e);
        }
    }
    
    public void enviarEmailCodigoCancelamento(String destinatario, String nome, String codigo) {
        if (!emailEnabled) {
            logger.warn("Email desabilitado. Email de código de cancelamento não enviado para: {}", destinatario);
            return;
        }

        if (mockMode) {
            logger.info("=== MODO MOCK - EMAIL DE CÓDIGO DE CANCELAMENTO ===");
            logger.info("Para: {}", destinatario);
            logger.info("Nome: {}", nome);
            logger.info("Código: {}", codigo);
            logger.info("===================================================");
            return;
        }

        try {
            logger.info("Enviando email de código de cancelamento para: {}", destinatario);
            
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(fromEmail);
            helper.setTo(destinatario);
            helper.setSubject("Código de Cancelamento de Voluntariado - Voluntários Pro Bem");
            helper.setText(construirEmailCodigoCancelamento(nome, codigo), true);
            
            mailSender.send(message);
            logger.info("Email de código de cancelamento enviado com sucesso para: {}", destinatario);
            
        } catch (Exception e) {
            logger.error("Erro ao enviar email de código de cancelamento para {}: {}", destinatario, e.getMessage(), e);
        }
    }
    
    public void enviarEmailCancelamentoConfirmado(String destinatario, String nome) {
        if (!emailEnabled) {
            logger.warn("Email desabilitado. Email de cancelamento confirmado não enviado para: {}", destinatario);
            return;
        }

        if (mockMode) {
            logger.info("=== MODO MOCK - EMAIL DE CANCELAMENTO CONFIRMADO ===");
            logger.info("Para: {}", destinatario);
            logger.info("Nome: {}", nome);
            logger.info("====================================================");
            return;
        }

        try {
            logger.info("Enviando email de cancelamento confirmado para: {}", destinatario);
            
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(fromEmail);
            helper.setTo(destinatario);
            helper.setSubject("Cancelamento de Voluntariado Confirmado - Voluntários Pro Bem");
            helper.setText(construirEmailCancelamentoConfirmado(nome), true);
            
            mailSender.send(message);
            logger.info("Email de cancelamento confirmado enviado com sucesso para: {}", destinatario);
            
        } catch (Exception e) {
            logger.error("Erro ao enviar email de cancelamento confirmado para {}: {}", destinatario, e.getMessage(), e);
        }
    }
    
    public void enviarEmailRemovidoPorAdmin(String destinatario, String nome) {
        if (!emailEnabled) {
            logger.warn("Email desabilitado. Email de remoção por admin não enviado para: {}", destinatario);
            return;
        }

        if (mockMode) {
            logger.info("=== MODO MOCK - EMAIL DE REMOÇÃO POR ADMIN ===");
            logger.info("Para: {}", destinatario);
            logger.info("Nome: {}", nome);
            logger.info("===============================================");
            return;
        }

        try {
            logger.info("Enviando email de remoção por admin para: {}", destinatario);
            
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(fromEmail);
            helper.setTo(destinatario);
            helper.setSubject("Remoção de Voluntariado - Voluntários Pro Bem");
            helper.setText(construirEmailRemovidoPorAdmin(nome), true);
            
            mailSender.send(message);
            logger.info("Email de remoção por admin enviado com sucesso para: {}", destinatario);
            
        } catch (Exception e) {
            logger.error("Erro ao enviar email de remoção por admin para {}: {}", destinatario, e.getMessage(), e);
        }
    }
    
    // ==================== TEMPLATES HTML DE VOLUNTARIADO ====================
    
    private String construirEmailPedidoVoluntario(String nome) {
        return String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
            </head>
            <body style="margin: 0; padding: 0; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f4f4f4;">
                <table width="100%%" cellpadding="0" cellspacing="0" style="background-color: #f4f4f4; padding: 20px 0;">
                    <tr>
                        <td align="center">
                            <table width="600" cellpadding="0" cellspacing="0" style="background-color: #ffffff; border-radius: 10px; overflow: hidden; box-shadow: 0 4px 6px rgba(0,0,0,0.1);">
                                <tr>
                                    <td style="background: linear-gradient(135deg, #B20000 0%%, #8B0000 100%%); padding: 40px 30px; text-align: center;">
                                        <h1 style="color: #ffffff; margin: 0; font-size: 28px; font-weight: bold;">📝 Pedido Recebido!</h1>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="padding: 40px 30px;">
                                        <p style="color: #333; font-size: 16px; line-height: 1.6; margin: 0 0 20px 0;">
                                            Olá <strong>%s</strong>,
                                        </p>
                                        <p style="color: #333; font-size: 16px; line-height: 1.6; margin: 0 0 30px 0;">
                                            Recebemos seu pedido para se tornar voluntário da <strong>Voluntários Pro Bem</strong>! 🎉
                                        </p>
                                        <table width="100%%" cellpadding="0" cellspacing="0" style="background-color: #FFF3CD; border-radius: 8px; margin-bottom: 30px;">
                                            <tr>
                                                <td style="padding: 20px;">
                                                    <p style="color: #856404; font-size: 14px; line-height: 1.6; margin: 0;">
                                                        <strong>⏳ Em análise:</strong> Nossa equipe está analisando seu pedido. 
                                                        Você receberá um email assim que for aprovado!
                                                    </p>
                                                </td>
                                            </tr>
                                        </table>
                                        <p style="color: #333; font-size: 16px; line-height: 1.6; margin: 0;">
                                            Obrigado por querer fazer parte da nossa família! ❤️
                                        </p>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="background-color: #f8f9fa; padding: 30px; text-align: center; border-top: 1px solid #e9ecef;">
                                        <p style="color: #6c757d; font-size: 14px; margin: 0 0 10px 0;">
                                            <strong>Voluntários Pro Bem</strong>
                                        </p>
                                        <p style="color: #6c757d; font-size: 12px; margin: 0;">
                                            Fazendo o bem, fazendo a diferença.
                                        </p>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </body>
            </html>
            """, nome);
    }
    
    private String construirEmailVoluntarioAprovado(String nome) {
        return String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
            </head>
            <body style="margin: 0; padding: 0; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f4f4f4;">
                <table width="100%%" cellpadding="0" cellspacing="0" style="background-color: #f4f4f4; padding: 20px 0;">
                    <tr>
                        <td align="center">
                            <table width="600" cellpadding="0" cellspacing="0" style="background-color: #ffffff; border-radius: 10px; overflow: hidden; box-shadow: 0 4px 6px rgba(0,0,0,0.1);">
                                <tr>
                                    <td style="background: linear-gradient(135deg, #28a745 0%%, #20c997 100%%); padding: 40px 30px; text-align: center;">
                                        <h1 style="color: #ffffff; margin: 0; font-size: 28px; font-weight: bold;">🎉 Parabéns!</h1>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="padding: 40px 30px;">
                                        <p style="color: #333; font-size: 16px; line-height: 1.6; margin: 0 0 20px 0;">
                                            Olá <strong>%s</strong>,
                                        </p>
                                        <p style="color: #333; font-size: 16px; line-height: 1.6; margin: 0 0 30px 0;">
                                            Você foi <strong>aprovado como voluntário</strong> da Voluntários Pro Bem! 🎊
                                        </p>
                                        <table width="100%%" cellpadding="0" cellspacing="0" style="background-color: #d4edda; border-radius: 8px; margin-bottom: 30px;">
                                            <tr>
                                                <td style="padding: 20px;">
                                                    <p style="color: #155724; font-size: 14px; line-height: 1.6; margin: 0;">
                                                        <strong>✅ Bem-vindo à equipe!</strong> Agora você pode participar de eventos exclusivos, 
                                                        gerenciar atividades e fazer ainda mais diferença na comunidade!
                                                    </p>
                                                </td>
                                            </tr>
                                        </table>
                                        <p style="color: #333; font-size: 16px; line-height: 1.6; margin: 0;">
                                            Estamos muito felizes em ter você conosco! Juntos faremos a diferença! ❤️
                                        </p>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="background-color: #f8f9fa; padding: 30px; text-align: center; border-top: 1px solid #e9ecef;">
                                        <p style="color: #6c757d; font-size: 14px; margin: 0 0 10px 0;">
                                            <strong>Voluntários Pro Bem</strong>
                                        </p>
                                        <p style="color: #6c757d; font-size: 12px; margin: 0;">
                                            Fazendo o bem, fazendo a diferença.
                                        </p>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </body>
            </html>
            """, nome);
    }
    
    private String construirEmailCodigoCancelamento(String nome, String codigo) {
        return String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
            </head>
            <body style="margin: 0; padding: 0; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f4f4f4;">
                <table width="100%%" cellpadding="0" cellspacing="0" style="background-color: #f4f4f4; padding: 20px 0;">
                    <tr>
                        <td align="center">
                            <table width="600" cellpadding="0" cellspacing="0" style="background-color: #ffffff; border-radius: 10px; overflow: hidden; box-shadow: 0 4px 6px rgba(0,0,0,0.1);">
                                <tr>
                                    <td style="background: linear-gradient(135deg, #ffc107 0%%, #ff9800 100%%); padding: 40px 30px; text-align: center;">
                                        <h1 style="color: #ffffff; margin: 0; font-size: 28px; font-weight: bold;">⚠️ Código de Cancelamento</h1>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="padding: 40px 30px;">
                                        <p style="color: #333; font-size: 16px; line-height: 1.6; margin: 0 0 20px 0;">
                                            Olá <strong>%s</strong>,
                                        </p>
                                        <p style="color: #333; font-size: 16px; line-height: 1.6; margin: 0 0 30px 0;">
                                            Você solicitou o cancelamento do seu voluntariado. Use o código abaixo para confirmar:
                                        </p>
                                        <table width="100%%" cellpadding="0" cellspacing="0" style="margin-bottom: 30px;">
                                            <tr>
                                                <td align="center">
                                                    <table cellpadding="0" cellspacing="0" style="background: linear-gradient(135deg, #f8f9fa 0%%, #e9ecef 100%%); border: 2px dashed #ffc107; border-radius: 10px; padding: 25px;">
                                                        <tr>
                                                            <td align="center">
                                                                <p style="color: #6c757d; font-size: 14px; margin: 0 0 10px 0; text-transform: uppercase; letter-spacing: 1px;">Código de Confirmação</p>
                                                                <p style="color: #ffc107; font-size: 36px; font-weight: bold; margin: 0; letter-spacing: 8px; font-family: 'Courier New', monospace;">%s</p>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr>
                                        </table>
                                        <table width="100%%" cellpadding="0" cellspacing="0" style="background-color: #fff3cd; border-radius: 8px; margin-bottom: 30px;">
                                            <tr>
                                                <td style="padding: 20px;">
                                                    <p style="color: #856404; font-size: 14px; line-height: 1.6; margin: 0;">
                                                        <strong>⏰ Atenção:</strong> Este código é válido por <strong>15 minutos</strong>. 
                                                        Se você não solicitou este cancelamento, ignore este email.
                                                    </p>
                                                </td>
                                            </tr>
                                        </table>
                                        <p style="color: #333; font-size: 16px; line-height: 1.6; margin: 0;">
                                            Sentiremos sua falta! Você sempre será bem-vindo de volta. 💙
                                        </p>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="background-color: #f8f9fa; padding: 30px; text-align: center; border-top: 1px solid #e9ecef;">
                                        <p style="color: #6c757d; font-size: 14px; margin: 0 0 10px 0;">
                                            <strong>Voluntários Pro Bem</strong>
                                        </p>
                                        <p style="color: #6c757d; font-size: 12px; margin: 0;">
                                            Fazendo o bem, fazendo a diferença.
                                        </p>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </body>
            </html>
            """, nome, codigo);
    }
    
    private String construirEmailCancelamentoConfirmado(String nome) {
        return String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
            </head>
            <body style="margin: 0; padding: 0; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f4f4f4;">
                <table width="100%%" cellpadding="0" cellspacing="0" style="background-color: #f4f4f4; padding: 20px 0;">
                    <tr>
                        <td align="center">
                            <table width="600" cellpadding="0" cellspacing="0" style="background-color: #ffffff; border-radius: 10px; overflow: hidden; box-shadow: 0 4px 6px rgba(0,0,0,0.1);">
                                <tr>
                                    <td style="background: linear-gradient(135deg, #6c757d 0%%, #495057 100%%); padding: 40px 30px; text-align: center;">
                                        <h1 style="color: #ffffff; margin: 0; font-size: 28px; font-weight: bold;">✓ Cancelamento Confirmado</h1>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="padding: 40px 30px;">
                                        <p style="color: #333; font-size: 16px; line-height: 1.6; margin: 0 0 20px 0;">
                                            Olá <strong>%s</strong>,
                                        </p>
                                        <p style="color: #333; font-size: 16px; line-height: 1.6; margin: 0 0 30px 0;">
                                            Seu voluntariado foi cancelado conforme solicitado.
                                        </p>
                                        <table width="100%%" cellpadding="0" cellspacing="0" style="background-color: #f8f9fa; border-radius: 8px; margin-bottom: 30px;">
                                            <tr>
                                                <td style="padding: 20px;">
                                                    <p style="color: #6c757d; font-size: 14px; line-height: 1.6; margin: 0;">
                                                        Agradecemos imensamente por todo o tempo e dedicação que você nos deu. 
                                                        Você fez a diferença na vida de muitas pessoas! 🙏
                                                    </p>
                                                </td>
                                            </tr>
                                        </table>
                                        <p style="color: #333; font-size: 16px; line-height: 1.6; margin: 0;">
                                            Você sempre será bem-vindo de volta quando quiser! As portas estão sempre abertas. 💙
                                        </p>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="background-color: #f8f9fa; padding: 30px; text-align: center; border-top: 1px solid #e9ecef;">
                                        <p style="color: #6c757d; font-size: 14px; margin: 0 0 10px 0;">
                                            <strong>Voluntários Pro Bem</strong>
                                        </p>
                                        <p style="color: #6c757d; font-size: 12px; margin: 0;">
                                            Fazendo o bem, fazendo a diferença.
                                        </p>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </body>
            </html>
            """, nome);
    }
    
    private String construirEmailRemovidoPorAdmin(String nome) {
        return String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
            </head>
            <body style="margin: 0; padding: 0; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f4f4f4;">
                <table width="100%%" cellpadding="0" cellspacing="0" style="background-color: #f4f4f4; padding: 20px 0;">
                    <tr>
                        <td align="center">
                            <table width="600" cellpadding="0" cellspacing="0" style="background-color: #ffffff; border-radius: 10px; overflow: hidden; box-shadow: 0 4px 6px rgba(0,0,0,0.1);">
                                <tr>
                                    <td style="background: linear-gradient(135deg, #dc3545 0%%, #c82333 100%%); padding: 40px 30px; text-align: center;">
                                        <h1 style="color: #ffffff; margin: 0; font-size: 28px; font-weight: bold;">⚠️ Remoção de Voluntariado</h1>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="padding: 40px 30px;">
                                        <p style="color: #333; font-size: 16px; line-height: 1.6; margin: 0 0 20px 0;">
                                            Olá <strong>%s</strong>,
                                        </p>
                                        <p style="color: #333; font-size: 16px; line-height: 1.6; margin: 0 0 30px 0;">
                                            Informamos que seu status de voluntário foi removido pela administração da ONG.
                                        </p>
                                        <table width="100%%" cellpadding="0" cellspacing="0" style="background-color: #f8d7da; border-radius: 8px; margin-bottom: 30px;">
                                            <tr>
                                                <td style="padding: 20px;">
                                                    <p style="color: #721c24; font-size: 14px; line-height: 1.6; margin: 0;">
                                                        Se você tiver dúvidas sobre esta decisão, entre em contato conosco para mais informações.
                                                    </p>
                                                </td>
                                            </tr>
                                        </table>
                                        <p style="color: #333; font-size: 16px; line-height: 1.6; margin: 0;">
                                            Agradecemos por todo o tempo que você dedicou à nossa causa. 🙏
                                        </p>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="background-color: #f8f9fa; padding: 30px; text-align: center; border-top: 1px solid #e9ecef;">
                                        <p style="color: #6c757d; font-size: 14px; margin: 0 0 10px 0;">
                                            <strong>Voluntários Pro Bem</strong>
                                        </p>
                                        <p style="color: #6c757d; font-size: 12px; margin: 0;">
                                            Fazendo o bem, fazendo a diferença.
                                        </p>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </body>
            </html>
            """, nome);
    }
}
