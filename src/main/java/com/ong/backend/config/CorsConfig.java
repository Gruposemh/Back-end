package com.ong.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")  // Todas as rotas
                    .allowedOrigins(
                        "http://localhost:5173", // Sua origem do frontend
                        "http://127.0.0.1:5500", 
                        "http://localhost:5500",
                        "http://127.0.0.1:3000",
                        "file://"  // Para arquivos locais
                    )
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                    .allowedHeaders("*")
                    .allowCredentials(true); // Permitir envio de cookies
            }
        };
    }
}