
package com.ong.backend.config;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.ong.backend.services.AutenticacaoService;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private SecurityFilter securityFilter;
    
    @Autowired
    private AutenticacaoService autenticacaoService;
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors().and()
            .csrf().disable()
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Rotas públicas da AuthController
                .requestMatchers("/auth/register", "/auth/login").permitAll()

                // Usuário
                .requestMatchers(HttpMethod.GET, "/usuario/todos").hasRole("ADMIN")
                .requestMatchers("/usuario/deletar/**").hasAnyRole("USUARIO", "ADMIN")
                .requestMatchers("/usuario/atualizar/**").hasAnyRole("USUARIO", "ADMIN")

             // Blog
                .requestMatchers(HttpMethod.POST, "/blog/criar").authenticated()
                .requestMatchers(HttpMethod.GET, "/blog/listar").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/blog/buscar").permitAll()
                .requestMatchers(HttpMethod.GET, "/blog/blogs").permitAll()
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
                .requestMatchers(HttpMethod.GET, "/inscricao/listar").hasRole("ADMIN")
                
                // Pagamento
                .requestMatchers(HttpMethod.POST, "/pagamento/efetuar").authenticated()
                .requestMatchers(HttpMethod.GET, "/pagamento/listar").hasRole("ADMIN")
                
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
                .requestMatchers(HttpMethod.PUT, "voluntario/aprovar/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "voluntario/negar/**").hasRole("ADMIN")
                
                // Notificações
                .requestMatchers(HttpMethod.POST, "notificacao/criar").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "notificarUsuario/notificar").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "notificarUsuario/listar/usuario/**").hasAnyRole("ADMIN", "USUARIO")
                
                // Relatório
                .requestMatchers(HttpMethod.GET, "/relatorios").hasRole("ADMIN")

            )
            .userDetailsService(autenticacaoService)
            .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}