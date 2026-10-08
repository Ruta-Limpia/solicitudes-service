package com.duoc.rutalimpia.solicitudes.dto;

import com.duoc.rutalimpia.solicitudes.model.enums.EstadoSolicitud;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ActualizarEstadoSolicitudRequest(

        @NotNull(message = "es obligatorio")
        EstadoSolicitud estado,

        Long camionId,

        Long paradaId,

        @Size(max = 200, message = "máximo 200 caracteres")
        String motivoFallo
) {
}
