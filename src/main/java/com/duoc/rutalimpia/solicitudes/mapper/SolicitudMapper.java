package com.duoc.rutalimpia.solicitudes.mapper;

import com.duoc.rutalimpia.solicitudes.dto.CrearSolicitudRequest;
import com.duoc.rutalimpia.solicitudes.dto.SolicitudResponse;
import com.duoc.rutalimpia.solicitudes.model.Solicitud;
import org.springframework.stereotype.Component;

@Component
public class SolicitudMapper {

    // vecinoId, estado y fechaCreacion los pone el service
    public Solicitud toEntity(CrearSolicitudRequest request) {
        return Solicitud.builder()
                .direccion(request.direccion())
                .comuna(request.comuna())
                .tipoResiduo(request.tipoResiduo())
                .fechaPreferida(request.fechaPreferida())
                .bloqueHorario(request.bloqueHorario())
                .observaciones(request.observaciones())
                .build();
    }

    public SolicitudResponse toResponse(Solicitud solicitud) {
        return new SolicitudResponse(
                solicitud.getId(),
                solicitud.getFolio(),
                solicitud.getDireccion(),
                solicitud.getComuna(),
                solicitud.getTipoResiduo(),
                solicitud.getFechaPreferida(),
                solicitud.getBloqueHorario(),
                solicitud.getEstado(),
                solicitud.getCamionId(),
                solicitud.getMotivoFallo(),
                solicitud.getFechaCreacion()
        );
    }
}
