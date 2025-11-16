package com.ong.backend.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.http.MediaType;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.ong.backend.services.AutenticacaoService;
import com.ong.backend.services.CustomOAuth2UserService;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private SecurityFilter securityFilter;

    @Autowired
    private AutenticacaoService autenticacaoService;
    
    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;
    
    @Autowired
    private OAuth2SuccessHandler oauth2SuccessHandler;

    @Value("${app.security.cors.allowed-origins}")
    private String allowedOrigins;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(allowedOrigins.split(",")));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            // API stateless: desabilitar CSRF para evitar 403 em clients (Postman/SPA) sem token CSRF
            .csrf(csrf -> csrf.disable())
            // Retornar JSON em vez de redirecionar para páginas HTML quando não autenticado/sem permissão
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(jsonAuthEntryPoint())
                .accessDeniedHandler(jsonAccessDeniedHandler())
            )
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                    // Rotas públicas de autenticação
                    .requestMatchers("/auth/register", "/auth/register-modern", "/auth/verify-email-modern", 
                            "/auth/verify-email-legacy", "/auth/set-password",
                            "/auth/login", "/auth/request-otp", "/auth/login-otp",
                            "/auth/resend-verification", "/auth/request-password-reset",
                            "/auth/reset-password", "/auth/refresh-token", "/auth/check",
                            "/auth/logout", "/auth/create-admin-temp").permitAll()

                    // OAuth2 endpoints
                    .requestMatchers("/oauth2/**", "/login/oauth2/**").permitAll()

                    // Google Auth direto
                    .requestMatchers("/google-auth/**").permitAll()

                    // Endpoints protegidos de autenticação
                    .requestMatchers("/auth/logout-all-devices", 
                            "/auth/token-status", "/auth/login-history").authenticated()

                    // Endpoints de teste (remover em produção)
                    .requestMatchers("/test/**").permitAll()

                    // Usuário
                    .requestMatchers(HttpMethod.GET, "/usuario/todos").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/usuario/deletar/**").hasAnyRole("USUARIO", "ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/usuario/atualizar/**").hasAnyRole("USUARIO", "ADMIN")

                    // Blog
                    .requestMatchers(HttpMethod.POST, "/blog/criar").authenticated()
                    .requestMatchers(HttpMethod.GET, "/blog/listar").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.GET, "/blog/pendentes").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.GET, "/blog/buscar/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/blog/aprovados").permitAll()
                    .requestMatchers(HttpMethod.PUT, "/blog/atualizar/**").authenticated()
                    .requestMatchers(HttpMethod.DELETE, "/blog/deletar/**").authenticated()

                    // Aprovação e negação — apenas ADMIN
                    .requestMatchers(HttpMethod.PUT, "/blog/aprovar/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/blog/negar/**").hasRole("ADMIN")
                    
                    // Curso
                    .requestMatchers(HttpMethod.POST, "/curso/cadastrar").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/curso/atualizar/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/curso/deletar/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.GET, "/curso/listar").permitAll()
                    .requestMatchers(HttpMethod.GET, "/curso/buscar").permitAll()

                    // Comentarios
                    .requestMatchers(HttpMethod.POST, "/comentario/postar").permitAll()
                    .requestMatchers(HttpMethod.GET, "/comentario/listar").permitAll()
                    .requestMatchers(HttpMethod.GET, "/comentario/blog/**").permitAll()

                    // Apenas seus comentários(atualizar ou deletar apenas o que você comentou)
                    .requestMatchers(HttpMethod.PUT, "/comentario/**").authenticated()
                    .requestMatchers(HttpMethod.DELETE, "/comentario/**").authenticated()

                    // Doação
                    .requestMatchers(HttpMethod.POST, "/doacao/doar").authenticated()
                    .requestMatchers(HttpMethod.DELETE, "/doacao/deletar/**").authenticated()
                    .requestMatchers(HttpMethod.GET, "/doacao/usuario/**").authenticated()
                    .requestMatchers(HttpMethod.GET, "/doacao/doacoes").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.GET, "/doacao/{id}").hasRole("ADMIN")

                    // Evento
                    .requestMatchers(HttpMethod.POST, "/evento/marcar").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/evento/atualizar/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/evento/deletar/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.GET, "/evento/**").permitAll()

                    // Inscrição
                    .requestMatchers(HttpMethod.POST, "/inscricao/inscrever").authenticated()
                    .requestMatchers(HttpMethod.DELETE, "/inscricao/deletar/**").authenticated()

                    // Participações
                    .requestMatchers(HttpMethod.POST, "/participar").hasAnyRole("ADMIN", "USUARIO")
                    .requestMatchers(HttpMethod.DELETE, "/participar/deletar/**").hasAnyRole("ADMIN", "USUARIO")
                    .requestMatchers(HttpMethod.GET, "/participar/listar").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.GET, "/participar/{id}").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.GET, "/participar/usuario/**").hasRole("ADMIN")

                    //Voluntario
                    .requestMatchers(HttpMethod.POST, "/voluntario/tornar").hasAnyRole("ADMIN", "USUARIO")
                    .requestMatchers(HttpMethod.GET, "/voluntario/listar").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/voluntario/cancelar/**").hasAnyRole("ADMIN", "USUARIO")
                    .requestMatchers(HttpMethod.GET, "/voluntario/listar/aprovados").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.GET, "/voluntario/listar/pendentes").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "voluntario/aprovar/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "voluntario/negar/**").hasRole("ADMIN")

                    // Notificações
                    .requestMatchers(HttpMethod.POST, "notificacao/criar").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.POST, "notificarUsuario/notificar").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.GET, "notificarUsuario/listar/usuario/**").hasAnyRole("ADMIN", "USUARIO")
                    
                    // Relatório
                    .requestMatchers(HttpMethod.GET, "/relatorio/usuarios").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.GET, "/relatorio/doacoes").hasRole("ADMIN")

                    // Qualquer outra requisição precisa de autenticação
                    .anyRequest().authenticated()
            )
            .oauth2Login(oauth2 -> oauth2
                    .userInfoEndpoint(userInfo -> userInfo
                        .userService(customOAuth2UserService)
                    )
                    .successHandler(oauth2SuccessHandler)
                    .failureUrl("http://localhost:8080/oauth2/failure")
                    .permitAll()
            )
            .userDetailsService(autenticacaoService)
            .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    private AuthenticationEntryPoint jsonAuthEntryPoint() {
        return (request, response, authException) -> {
            response.setStatus(401);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write("{\"status\":401,\"error\":\"Unauthorized\",\"message\":\"Autenticação necessária\"}");
        };
    }

    private AccessDeniedHandler jsonAccessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            response.setStatus(403);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write("{\"status\":403,\"error\":\"Forbidden\",\"message\":\"Acesso negado\"}");
        };
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
