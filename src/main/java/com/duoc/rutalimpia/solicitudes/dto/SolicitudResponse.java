package com.duoc.rutalimpia.solicitudes.dto;

import com.duoc.rutalimpia.solicitudes.model.enums.BloqueHorario;
import com.duoc.rutalimpia.solicitudes.model.enums.EstadoSolicitud;
import com.duoc.rutalimpia.solicitudes.model.enums.TipoResiduo;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "Solicitud de retiro")
public record SolicitudResponse(
        Long id,
        @Schema(example = "RL-20261008-000001") String folio,
        String direccion,
        String comuna,
        TipoResiduo tipoResiduo,
        LocalDate fechaPreferida,
        BloqueHorario bloqueHorario,
        @Schema(example = "PENDIENTE") EstadoSolicitud estado,
        Long camionId,
        String motivoFallo,
        LocalDateTime fechaCreacion
) {
}
