package com.duoc.rutalimpia.solicitudes.exception;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Formato común de error")
public record ErrorResponse(
        LocalDateTime timestamp,
        @Schema(example = "404") int status,
        @Schema(example = "NOT_FOUND") String error,
        @Schema(example = "Solicitud 9999 no encontrada") String mensaje,
        String path
) {
}
