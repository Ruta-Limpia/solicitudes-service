package com.duoc.rutalimpia.solicitudes.controller;

import com.duoc.rutalimpia.solicitudes.dto.ActualizarEstadoSolicitudRequest;
import com.duoc.rutalimpia.solicitudes.dto.CrearSolicitudRequest;
import com.duoc.rutalimpia.solicitudes.dto.SolicitudResponse;
import com.duoc.rutalimpia.solicitudes.service.SolicitudService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SolicitudController {

    private final SolicitudService service;

    @PostMapping("/solicitudes")
    @PreAuthorize("hasRole('VECINO')")
    public ResponseEntity<SolicitudResponse> crear(@Valid @RequestBody CrearSolicitudRequest request,
                                                   Authentication authentication) {
        SolicitudResponse creada = service.crear(request, vecinoId(authentication));
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @GetMapping("/solicitudes/mis-solicitudes")
    @PreAuthorize("hasRole('VECINO')")
    public ResponseEntity<List<SolicitudResponse>> listarMisSolicitudes(Authentication authentication) {
        return ResponseEntity.ok(service.listarPorVecino(vecinoId(authentication)));
    }

    @GetMapping("/solicitudes/{id}")
    @PreAuthorize("hasRole('VECINO')")
    public ResponseEntity<SolicitudResponse> obtenerPorId(@PathVariable Long id, Authentication authentication) {
        return ResponseEntity.ok(service.obtenerPorId(id, vecinoId(authentication)));
    }

    @GetMapping("/solicitudes/folio/{folio}")
    @PreAuthorize("hasRole('VECINO')")
    public ResponseEntity<SolicitudResponse> obtenerPorFolio(@PathVariable String folio, Authentication authentication) {
        return ResponseEntity.ok(service.obtenerPorFolio(folio, vecinoId(authentication)));
    }

    @PatchMapping("/internal/solicitudes/{id}/estado")
    public ResponseEntity<SolicitudResponse> actualizarEstado(@PathVariable Long id,
                                                              @Valid @RequestBody ActualizarEstadoSolicitudRequest request) {
        return ResponseEntity.ok(service.actualizarEstado(id, request));
    }

    // El id del vecino siempre sale del token, nunca del body
    private Long vecinoId(Authentication authentication) {
        return (Long) authentication.getPrincipal();
    }
}
