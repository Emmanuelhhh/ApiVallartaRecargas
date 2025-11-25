package com.tde.apiVallartaRecargas.service;

import com.tde.apiVallartaRecargas.dto.LoginRequest;
import com.tde.apiVallartaRecargas.dto.TokenResponse;
import com.tde.apiVallartaRecargas.entity.User;
import com.tde.apiVallartaRecargas.repository.OpeUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
public class AuthService {

    private final OpeUserRepository opeUserRepository;
    private final JwtService jwtService;

    public AuthService(OpeUserRepository opeUserRepository,
                       JwtService jwtService) {
        this.opeUserRepository = opeUserRepository;
        this.jwtService = jwtService;
    }

    public TokenResponse login(LoginRequest request) {
        if (!StringUtils.hasText(request.getUsername()) ||
            !StringUtils.hasText(request.getPassword())) {
            throw new IllegalArgumentException("El usuario y la contraseña son obligatorios");
        }

        // Consulta a BD: usuario + contraseña desencriptada en el query nativo
        Optional<User> userOpt = opeUserRepository.login(
                request.getUsername(),
                request.getPassword()
        );

        if (!userOpt.isPresent()) {
            throw new IllegalArgumentException("Credenciales inválidas");
        }

        String token = jwtService.generateToken(request.getUsername());
        return new TokenResponse(token);
    }

    public boolean isTokenValid(String token) {
        return jwtService.validateToken(token);
    }
}