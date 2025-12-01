package com.ong.backend.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ong.backend.entities.Newsletter;
import com.ong.backend.services.NewsletterService;

@RestController
@RequestMapping("/newsletter")
public class NewsletterController {

    @Autowired
    private NewsletterService newsletterService;

    @PostMapping("/inscrever")
    public ResponseEntity<?> inscrever(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            String nome = request.getOrDefault("nome", "");

            if (email == null || email.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Email é obrigatório."));
            }

            Newsletter newsletter = newsletterService.inscrever(email, nome);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Inscrição realizada com sucesso! Você receberá notificações sobre novos eventos e atividades.");
            response.put("email", newsletter.getEmail());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Erro ao processar inscrição."));
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Newsletter>> listar() {
        return ResponseEntity.ok(newsletterService.listarAtivos());
    }

    @DeleteMapping("/desinscrever")
    public ResponseEntity<?> desinscrever(@RequestParam String email) {
        try {
            newsletterService.desinscrever(email);
            return ResponseEntity.ok(Map.of("message", "Desinscrição realizada com sucesso."));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", e.getMessage()));
        }
    }
}
