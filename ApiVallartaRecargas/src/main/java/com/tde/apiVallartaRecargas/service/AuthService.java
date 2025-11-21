package com.tde.apiVallartaRecargas.service;

import com.tde.apiVallartaRecargas.dto.LoginRequest;
import com.tde.apiVallartaRecargas.dto.TokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class AuthService {

        private final String expectedUsername;
        private final String expectedPassword;
        private final JwtService jwtService;

        public AuthService(
                        @Value("${security.user.username:usuarior}") String expectedUsername,
                        @Value("${security.user.password:12345678}") String expectedPassword,
                        JwtService jwtService) {
                this.expectedUsername = expectedUsername;
                this.expectedPassword = expectedPassword;
                this.jwtService = jwtService;
        }

        public TokenResponse login(LoginRequest request) {
                if (!StringUtils.hasText(request.username()) || !StringUtils.hasText(request.password())) {
                        throw new IllegalArgumentException("El usuario y la contraseña son obligatorios");
                }

                if (!expectedUsername.equals(request.username()) || !expectedPassword.equals(request.password())) {
                        throw new IllegalArgumentException("Credenciales inválidas");
                }

                String token = jwtService.generateToken(request.username());
                return new TokenResponse(token);
        }

        public boolean isTokenValid(String token) {
                return jwtService.validateToken(token);
        }
}