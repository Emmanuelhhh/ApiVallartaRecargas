package com.tde.apiVallartaRecargas.service;

import com.tde.apiVallartaRecargas.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private final Key signingKey;
    private final long expirationMinutes;

    public JwtService(
            @Value("${security.jwt.secret:default-secret-key-change-me}") String secret,
            @Value("${security.jwt.expiration-minutes:60}") long expirationMinutes) {

        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMinutes = expirationMinutes;
    }

    /**
     * NUEVO: genera un token a partir de la entidad User,
     * agregando userId y username en los claims.
     */
    public String generateToken(User user) {
        Instant now = Instant.now();
        Instant expiration = now.plusSeconds(expirationMinutes * 60);

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("username", user.getUser()); // asumiendo que getUser() es el username

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUser()) // también dejamos el username como subject
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiration))
                // si tu versión de jjwt lo permite, esta forma es la recomendada:
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Versión antigua: genera un token sólo con el username.
     * Puedes dejarla por compatibilidad, pero es preferible usar la que recibe User.
     */
    public String generateToken(String username) {
        Instant now = Instant.now();
        Instant expiration = now.plusSeconds(expirationMinutes * 60);

        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiration))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return claims.getExpiration().after(new Date());
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * NUEVO: extraer username desde el token.
     * Puedes usar el subject o el claim "username"; aquí uso el claim explícito.
     */
    public String extractUsername(String token) {
        return extractClaim(token, claims -> claims.get("username", String.class));
        // Alternativa: extractClaim(token, Claims::getSubject);
    }

    /**
     * NUEVO: extraer userId desde el token.
     */
    public Long extractUserId(String token) {
        return extractClaim(token, claims -> {
            Object value = claims.get("userId");
            if (value == null) {
                return null;
            }
            // A veces JJWT devuelve Integer si el valor cabe en un int
            if (value instanceof Integer) {
                return ((Integer) value).longValue();
            }
            if (value instanceof Long) {
                return (Long) value;
            }
            // último recurso: intentar parsear
            return Long.valueOf(value.toString());
        });
    }

    // ================== Helpers internos ==================

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
