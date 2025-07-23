package com.ong.backend.services;

import java.util.Date;
import java.security.Key;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.ong.backend.entities.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class TokenService {

    @Value("${jwt.secret:minha-chave-secreta-super-secreta-de-256-bits-para-jwt-que-deve-ser-mantida-em-segredo}")
    private String secret;

    @Value("${jwt.expiration:86400000}")
    private Long expiration;

    public String gerarToken(Usuario usuario) {
        Key key = Keys.hmacShaKeyFor(secret.getBytes());

        return Jwts.builder()
                .setSubject(usuario.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
    }

    public String getSubject(String token) {
        Key key = Keys.hmacShaKeyFor(secret.getBytes());

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public boolean isTokenValid(String token) {
        try {
            Key key = Keys.hmacShaKeyFor(secret.getBytes());

            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
