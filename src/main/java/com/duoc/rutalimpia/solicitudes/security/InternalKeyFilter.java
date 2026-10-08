package com.duoc.rutalimpia.solicitudes.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.List;

public class InternalKeyFilter extends OncePerRequestFilter {

    private static final String PREFIJO_INTERNO = "/api/v1/internal/";

    private final String internalKey;
    private final HandlerExceptionResolver resolver;

    public InternalKeyFilter(String internalKey, HandlerExceptionResolver resolver) {
        this.internalKey = internalKey;
        this.resolver = resolver;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getRequestURI().startsWith(PREFIJO_INTERNO);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String clave = request.getHeader("X-Internal-Key");

        if (!internalKey.equals(clave)) {
            // Lo responde GlobalExceptionHandler como 403 con ErrorResponse
            resolver.resolveException(request, response, null, new AccessDeniedException("Clave interna inválida"));
            return;
        }

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                "internal", null, List.of(new SimpleGrantedAuthority("ROLE_INTERNAL")));
        SecurityContextHolder.getContext().setAuthentication(auth);
        chain.doFilter(request, response);
    }
}
