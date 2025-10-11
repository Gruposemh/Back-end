package com.ong.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ong.backend.services.EmailService;

@RestController
@RequestMapping(value = "/test")
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:5500", "http://localhost:5500", "http://127.0.0.1:3000"})
public class TestController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/email")
    public ResponseEntity<?> testarEmail(@RequestBody TestEmailRequest request) {
        try {
            emailService.enviarEmailBoasVindas(request.getEmail(), "Usuário Teste");
            return ResponseEntity.ok(new TestResponse("Email enviado com sucesso para: " + request.getEmail()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new TestResponse("Erro ao enviar email: " + e.getMessage()));
        }
    }
    
    @GetMapping("/status")
    public ResponseEntity<?> verificarStatus() {
        return ResponseEntity.ok(new TestResponse("Servidor back-end está funcionando corretamente"));
    }

    // Classes auxiliares
    public static class TestEmailRequest {
        private String email;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    public static class TestResponse {
        private String message;

        public TestResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
