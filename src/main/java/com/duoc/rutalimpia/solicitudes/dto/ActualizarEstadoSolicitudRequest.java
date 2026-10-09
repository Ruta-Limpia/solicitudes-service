package com.duoc.rutalimpia.solicitudes.dto;

import com.duoc.rutalimpia.solicitudes.model.enums.EstadoSolicitud;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ActualizarEstadoSolicitudRequest(

        @Schema(description = "Nuevo estado", example = "ASIGNADA")
        @NotNull(message = "es obligatorio")
        EstadoSolicitud estado,

        @Schema(description = "Camión asignado (al pasar a ASIGNADA)", example = "1")
        Long camionId,

        @Schema(description = "Parada en la hoja de ruta (al pasar a ASIGNADA)", example = "1")
        Long paradaId,

        @Schema(description = "Motivo (solo si pasa a FALLIDA)", example = "Nadie en el domicilio")
        @Size(max = 200, message = "máximo 200 caracteres")
        String motivoFallo
) {
}
