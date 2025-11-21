package com.tde.apiVallartaRecargas.controller;

import com.tde.apiVallartaRecargas.dto.LoginRequest;
import com.tde.apiVallartaRecargas.dto.RecargaRequest;
import com.tde.apiVallartaRecargas.dto.TokenResponse;
import com.tde.apiVallartaRecargas.entity.Recarga;
import com.tde.apiVallartaRecargas.service.AuthService;
import com.tde.apiVallartaRecargas.service.RecargaService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class ApiController {

        private final AuthService authService;
        private final RecargaService recargaService;

        public ApiController(AuthService authService, RecargaService recargaService) {
                this.authService = authService;
                this.recargaService = recargaService;
        }

        @PostMapping("/login")
        public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
                TokenResponse token = authService.login(request);
                return ResponseEntity.ok(token);
        }

        @PostMapping("/recargas/add")
        public ResponseEntity<Recarga> agregarRecarga(
                        @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
                        @RequestBody RecargaRequest request) {
                validarToken(authorization);
                Recarga recarga = recargaService.crearRecarga(request);
                return ResponseEntity.status(HttpStatus.CREATED).body(recarga);
        }

        @PostMapping("/recargas/add/all")
        public ResponseEntity<List<Recarga>> agregarRecargas(
                        @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
                        @RequestBody List<RecargaRequest> requests) {
                validarToken(authorization);
                List<Recarga> recargas = recargaService.crearRecargas(requests);
                return ResponseEntity.status(HttpStatus.CREATED).body(recargas);
        }

        private void validarToken(String authorization) {
                if (authorization == null || !authorization.startsWith("Bearer ")) {
                        throw new IllegalArgumentException("Encabezado Authorization faltante o inválido");
                }
                String token = authorization.substring("Bearer ".length());
                if (!authService.isTokenValid(token)) {
                        throw new IllegalArgumentException("Token inválido o expirado");
                }
        }

        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<Map<String, String>> handleBadRequest(IllegalArgumentException ex) {
                return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
}