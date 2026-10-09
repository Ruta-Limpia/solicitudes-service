package com.duoc.rutalimpia.solicitudes.controller;

import com.duoc.rutalimpia.solicitudes.config.OpenApiConfig;
import com.duoc.rutalimpia.solicitudes.dto.ActualizarEstadoSolicitudRequest;
import com.duoc.rutalimpia.solicitudes.dto.CrearSolicitudRequest;
import com.duoc.rutalimpia.solicitudes.dto.SolicitudResponse;
import com.duoc.rutalimpia.solicitudes.exception.ErrorResponse;
import com.duoc.rutalimpia.solicitudes.service.SolicitudService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    @Tag(name = "Solicitudes (vecino)")
    @Operation(summary = "Crear solicitud de retiro",
            description = "Registra la solicitud en estado PENDIENTE y genera su folio (RL-yyyyMMdd-000000). "
                    + "El vecino se toma del token.",
            security = @SecurityRequirement(name = OpenApiConfig.BEARER))
    @ApiResponse(responseCode = "201", description = "Solicitud creada")
    @ApiResponse(responseCode = "400", description = "Datos inválidos o fecha pasada",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "401", description = "Token ausente o inválido",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "403", description = "El token no es de rol VECINO",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<SolicitudResponse> crear(@Valid @RequestBody CrearSolicitudRequest request,
                                                   @Parameter(hidden = true) Authentication authentication) {
        SolicitudResponse creada = service.crear(request, vecinoId(authentication));
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @GetMapping("/solicitudes/mis-solicitudes")
    @PreAuthorize("hasRole('VECINO')")
    @Tag(name = "Solicitudes (vecino)")
    @Operation(summary = "Listar mis solicitudes",
            description = "Devuelve las solicitudes del vecino del token, la más reciente primero.",
            security = @SecurityRequirement(name = OpenApiConfig.BEARER))
    @ApiResponse(responseCode = "200", description = "Lista de solicitudes (puede venir vacía)")
    @ApiResponse(responseCode = "401", description = "Token ausente o inválido",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "403", description = "El token no es de rol VECINO",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<List<SolicitudResponse>> listarMisSolicitudes(@Parameter(hidden = true) Authentication authentication) {
        return ResponseEntity.ok(service.listarPorVecino(vecinoId(authentication)));
    }

    @GetMapping("/solicitudes/{id}")
    @PreAuthorize("hasRole('VECINO')")
    @Tag(name = "Solicitudes (vecino)")
    @Operation(summary = "Obtener solicitud por id",
            description = "Solo devuelve la solicitud si pertenece al vecino del token.",
            security = @SecurityRequirement(name = OpenApiConfig.BEARER))
    @ApiResponse(responseCode = "200", description = "Solicitud encontrada")
    @ApiResponse(responseCode = "401", description = "Token ausente o inválido",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "403", description = "El token no es de rol VECINO",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "No existe o es de otro vecino",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<SolicitudResponse> obtenerPorId(@Parameter(description = "Id de la solicitud", example = "1")
                                                          @PathVariable Long id,
                                                          @Parameter(hidden = true) Authentication authentication) {
        return ResponseEntity.ok(service.obtenerPorId(id, vecinoId(authentication)));
    }

    @GetMapping("/solicitudes/folio/{folio}")
    @PreAuthorize("hasRole('VECINO')")
    @Tag(name = "Solicitudes (vecino)")
    @Operation(summary = "Obtener solicitud por folio",
            description = "Busca por el folio de recepción. Solo devuelve solicitudes del vecino del token.",
            security = @SecurityRequirement(name = OpenApiConfig.BEARER))
    @ApiResponse(responseCode = "200", description = "Solicitud encontrada")
    @ApiResponse(responseCode = "401", description = "Token ausente o inválido",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "403", description = "El token no es de rol VECINO",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "No existe o es de otro vecino",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<SolicitudResponse> obtenerPorFolio(@Parameter(description = "Folio de recepción", example = "RL-20261008-000001")
                                                             @PathVariable String folio,
                                                             @Parameter(hidden = true) Authentication authentication) {
        return ResponseEntity.ok(service.obtenerPorFolio(folio, vecinoId(authentication)));
    }

    @PatchMapping("/internal/solicitudes/{id}/estado")
    @Tag(name = "Interno (entre servicios)")
    @Operation(summary = "Actualizar estado de una solicitud",
            description = "Transiciones válidas: PENDIENTE → ASIGNADA, ASIGNADA → RETIRADA, ASIGNADA → FALLIDA. "
                    + "Requiere el header X-Internal-Key.",
            security = @SecurityRequirement(name = OpenApiConfig.INTERNAL))
    @ApiResponse(responseCode = "200", description = "Estado actualizado")
    @ApiResponse(responseCode = "400", description = "Estado ausente o inválido",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "403", description = "Clave interna ausente o incorrecta",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "La solicitud no existe",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "409", description = "Transición de estado no permitida",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<SolicitudResponse> actualizarEstado(@Parameter(description = "Id de la solicitud", example = "1")
                                                              @PathVariable Long id,
                                                              @Valid @RequestBody ActualizarEstadoSolicitudRequest request) {
        return ResponseEntity.ok(service.actualizarEstado(id, request));
    }

    // El id del vecino siempre sale del token, nunca del body
    private Long vecinoId(Authentication authentication) {
        return (Long) authentication.getPrincipal();
    }
}
