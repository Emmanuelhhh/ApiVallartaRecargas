package com.tde.apiVallartaRecargas.controller;

import com.tde.apiVallartaRecargas.dto.LoginRequest;
import com.tde.apiVallartaRecargas.dto.RecargaRequestHotelDTO;
import com.tde.apiVallartaRecargas.dto.RecargaResponseDTO;
import com.tde.apiVallartaRecargas.dto.HotelResumenDTO;
import com.tde.apiVallartaRecargas.dto.TokenResponse;
import com.tde.apiVallartaRecargas.service.AuthService;
import com.tde.apiVallartaRecargas.service.HotelService;
import com.tde.apiVallartaRecargas.service.RecargaHotelService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v2") // nueva versión (recomendado)
public class ApiControllerV2 {

    private final AuthService authService;
    private final RecargaHotelService recargaService;
    private final HotelService hotelService;
    
    public ApiControllerV2(AuthService authService, RecargaHotelService recargaService, HotelService hotelService) {
        this.authService = authService;
        this.recargaService = recargaService;
        this.hotelService = hotelService;
    }

    // Mantienes login igual (puede quedarse en /api/v1 también)
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
        TokenResponse token = authService.login(request);
        return ResponseEntity.ok(token);
    }

    /**
     * Crear una recarga (nueva implementación)
     * POST /api/v2/recargas
     */
    @PostMapping("/recargas")
    public ResponseEntity<RecargaResponseDTO> recargar(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
            @RequestBody RecargaRequestHotelDTO requestBody,
            HttpServletRequest httpRequest
    ) {
        String token = extraerTokenBearer(authorization);

        // Ideal: esto lo hace Spring Security; si por ahora lo haces manual, ok:
        if (!authService.isTokenValid(token)) {
            throw new IllegalArgumentException("Token inválido o expirado");
        }

        // Necesitas poder obtener estos valores del JWT (claims) o de tu sesión/token store:
        Long idUser = authService.getUserIdFromToken(token);
        Long idHotel = authService.getHotelIdFromToken(token);

        String ipOrigen = obtenerIp(httpRequest);

        RecargaResponseDTO response = recargaService.realizarRecarga(
                requestBody,
                idUser,
                idHotel,
                ipOrigen
        );

        // Si fue rechazada, 400. Si aplicada, 201.
        if ("RECHAZADA".equalsIgnoreCase(response.getEstatus())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/hoteles")
    public ResponseEntity<List<HotelResumenDTO>> listarHoteles(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization
    ) {
        String token = extraerTokenBearer(authorization);

        if (!authService.isTokenValid(token)) {
            throw new IllegalArgumentException("Token inválido o expirado");
        }

        Long userId = authService.getUserIdFromToken(token);

        List<HotelResumenDTO> hoteles = hotelService.listarHotelesPermitidos(userId);

        return ResponseEntity.ok(hoteles);
    }
  
  private String extraerTokenBearer(String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Encabezado Authorization faltante o inválido");
        }
        return authorization.substring("Bearer ".length()).trim();
    }

    private String obtenerIp(HttpServletRequest request) {
        // Si estás detrás de proxy/load balancer, revisa X-Forwarded-For
        String xff = request.getHeader("X-Forwarded-For");
        if (xff != null && !xff.trim().isEmpty()) {
            return xff.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleBadRequest(IllegalArgumentException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());
        return ResponseEntity.badRequest().body(response);
    }
}
