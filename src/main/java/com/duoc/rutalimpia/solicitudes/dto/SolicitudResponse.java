package com.duoc.rutalimpia.solicitudes.dto;

import com.duoc.rutalimpia.solicitudes.model.enums.BloqueHorario;
import com.duoc.rutalimpia.solicitudes.model.enums.EstadoSolicitud;
import com.duoc.rutalimpia.solicitudes.model.enums.TipoResiduo;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record SolicitudResponse(
        Long id,
        String folio,
        String direccion,
        String comuna,
        TipoResiduo tipoResiduo,
        LocalDate fechaPreferida,
        BloqueHorario bloqueHorario,
        EstadoSolicitud estado,
        Long camionId,
        String motivoFallo,
        LocalDateTime fechaCreacion
) {
}
