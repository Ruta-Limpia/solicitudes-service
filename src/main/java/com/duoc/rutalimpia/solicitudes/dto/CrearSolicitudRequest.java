package com.duoc.rutalimpia.solicitudes.dto;

import com.duoc.rutalimpia.solicitudes.model.enums.BloqueHorario;
import com.duoc.rutalimpia.solicitudes.model.enums.TipoResiduo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CrearSolicitudRequest(

        @Schema(description = "Dirección del retiro", example = "Av. Siempre Viva 742")
        @NotBlank(message = "es obligatoria")
        @Size(max = 200, message = "máximo 200 caracteres")
        String direccion,

        @Schema(description = "Comuna", example = "Maipú")
        @NotBlank(message = "es obligatoria")
        @Size(max = 80, message = "máximo 80 caracteres")
        String comuna,

        @Schema(description = "Tipo de residuo a retirar", example = "VIDRIO")
        @NotNull(message = "es obligatorio")
        TipoResiduo tipoResiduo,

        @Schema(description = "Fecha preferida (hoy o futura), formato yyyy-MM-dd", example = "2026-10-20")
        @NotNull(message = "es obligatoria")
        @FutureOrPresent(message = "no puede ser una fecha pasada")
        LocalDate fechaPreferida,

        @Schema(description = "Bloque horario preferido", example = "MANANA")
        @NotNull(message = "es obligatorio")
        BloqueHorario bloqueHorario,

        @Schema(description = "Indicaciones para el conductor (opcional)", example = "Dejar en conserjería")
        @Size(max = 300, message = "máximo 300 caracteres")
        String observaciones
) {
}
