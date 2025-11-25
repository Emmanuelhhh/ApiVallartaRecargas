package com.tde.apiVallartaRecargas.service;

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

        public String generateToken(String username) {
                Instant now = Instant.now();
                Instant expiration = now.plusSeconds(expirationMinutes * 60);

                Map<String, Object> claims = new HashMap<>();
                claims.put("username", username);

                return Jwts.builder()
                        .setClaims(claims)
                        .setIssuedAt(Date.from(now))
                        .setExpiration(Date.from(expiration))
                        .signWith(SignatureAlgorithm.HS256, signingKey)
                        .compact();
        }

        public boolean validateToken(String token) {
                try {
                        Claims claims = Jwts.parserBuilder()
                                        .setSigningKey(signingKey)
                                        .build()
                                        .parseClaimsJws(token)
                                        .getBody();
                        return claims.getExpiration().after(new Date());
                } catch (Exception ex) {
                        return false;
                }
        }
}