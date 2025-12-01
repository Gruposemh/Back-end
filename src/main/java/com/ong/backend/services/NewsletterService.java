package com.ong.backend.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ong.backend.entities.Newsletter;
import com.ong.backend.repositories.NewsletterRepository;

@Service
public class NewsletterService {

    @Autowired
    private NewsletterRepository newsletterRepository;

    @Autowired
    private EmailService emailService;

    @Transactional
    public Newsletter inscrever(String email, String nome) {
        if (newsletterRepository.existsByEmail(email)) {
            throw new RuntimeException("Este email já está inscrito na newsletter.");
        }

        Newsletter newsletter = new Newsletter(email, nome);
        newsletter.setEmailConfirmado(true); // Auto-confirmar por enquanto
        newsletter.setDataConfirmacao(LocalDateTime.now());
        
        Newsletter saved = newsletterRepository.save(newsletter);
        
        // Enviar email de boas-vindas
        try {
            emailService.enviarEmailBoasVindasNewsletter(email, nome);
        } catch (Exception e) {
            System.err.println("Erro ao enviar email de boas-vindas: " + e.getMessage());
        }
        
        return saved;
    }

    public List<Newsletter> listarAtivos() {
        return newsletterRepository.findByAtivoTrue();
    }

    public List<Newsletter> listarConfirmados() {
        return newsletterRepository.findByEmailConfirmadoTrue();
    }

    @Transactional
    public void desinscrever(String email) {
        Newsletter newsletter = newsletterRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email não encontrado na newsletter."));
        newsletter.setAtivo(false);
        newsletterRepository.save(newsletter);
    }

    public void notificarNovoEvento(String tituloEvento, String dataEvento, String localEvento) {
        List<Newsletter> inscritos = listarConfirmados();
        
        for (Newsletter inscrito : inscritos) {
            if (inscrito.getAtivo()) {
                try {
                    emailService.enviarNotificacaoEvento(
                        inscrito.getEmail(), 
                        inscrito.getNome(), 
                        tituloEvento, 
                        dataEvento, 
                        localEvento
                    );
                } catch (Exception e) {
                    System.err.println("Erro ao enviar email para " + inscrito.getEmail() + ": " + e.getMessage());
                }
            }
        }
    }

    public void notificarNovaAtividade(String tituloAtividade, String descricao) {
        List<Newsletter> inscritos = listarConfirmados();
        
        for (Newsletter inscrito : inscritos) {
            if (inscrito.getAtivo()) {
                try {
                    emailService.enviarNotificacaoAtividade(
                        inscrito.getEmail(), 
                        inscrito.getNome(), 
                        tituloAtividade, 
                        descricao
                    );
                } catch (Exception e) {
                    System.err.println("Erro ao enviar email para " + inscrito.getEmail() + ": " + e.getMessage());
                }
            }
        }
    }
}
