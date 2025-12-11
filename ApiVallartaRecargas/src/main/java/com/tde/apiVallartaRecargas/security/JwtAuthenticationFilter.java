package com.tde.apiVallartaRecargas.security;

import com.tde.apiVallartaRecargas.service.JwtService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        // 1) No procesamos JWT para el login
        if ("/api/v1/login".equals(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2) Leer header Authorization
        String authHeader = request.getHeader("Authorization");

        if (!StringUtils.hasText(authHeader) || !authHeader.startsWith("Bearer ")) {
            // No hay token → seguimos sin autenticar, ya SecurityConfig decidirá si permite o no
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        // 3) Validar token
        if (!jwtService.validateToken(token)) {
            // Token inválido → puedes cortar con 401 o dejar pasar sin autenticar
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido");
            return;
        }

        // 4) Extraer datos del token
        Long userId = jwtService.extractUserId(token);
        String username = jwtService.extractUsername(token);

        if (userId != null && username != null &&
            SecurityContextHolder.getContext().getAuthentication() == null) {

            // Creamos un "principal" simple que contiene id y username
            JwtUserPrincipal principal = new JwtUserPrincipal(userId, username);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            principal,
                            null,
                            Collections.emptyList() // sin roles por ahora
                    );
            authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
