package com.duoc.rutalimpia.solicitudes.service.impl;

import com.duoc.rutalimpia.solicitudes.dto.ActualizarEstadoSolicitudRequest;
import com.duoc.rutalimpia.solicitudes.dto.CrearSolicitudRequest;
import com.duoc.rutalimpia.solicitudes.dto.SolicitudResponse;
import com.duoc.rutalimpia.solicitudes.exception.RecursoNoEncontradoException;
import com.duoc.rutalimpia.solicitudes.exception.ReglaNegocioException;
import com.duoc.rutalimpia.solicitudes.mapper.SolicitudMapper;
import com.duoc.rutalimpia.solicitudes.model.Solicitud;
import com.duoc.rutalimpia.solicitudes.model.enums.EstadoSolicitud;
import com.duoc.rutalimpia.solicitudes.repository.SolicitudRepository;
import com.duoc.rutalimpia.solicitudes.service.FolioGenerator;
import com.duoc.rutalimpia.solicitudes.service.SolicitudService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SolicitudServiceImpl implements SolicitudService {

    private final SolicitudRepository repository;
    private final SolicitudMapper mapper;
    private final FolioGenerator folioGenerator;

    @Override
    @Transactional
    public SolicitudResponse crear(CrearSolicitudRequest request, Long vecinoId) {
        // 1. Guardar como PENDIENTE para obtener el id
        Solicitud solicitud = mapper.toEntity(request);
        solicitud.setVecinoId(vecinoId);
        solicitud.setEstado(EstadoSolicitud.PENDIENTE);
        solicitud.setFechaCreacion(LocalDateTime.now());
        solicitud = repository.save(solicitud);

        // 2. Generar el folio con el id y guardar de nuevo
        solicitud.setFolio(folioGenerator.generar(solicitud.getId()));
        solicitud = repository.save(solicitud);

        return mapper.toResponse(solicitud);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SolicitudResponse> listarPorVecino(Long vecinoId) {
        return repository.findByVecinoIdOrderByFechaCreacionDesc(vecinoId).stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public SolicitudResponse obtenerPorId(Long id, Long vecinoId) {
        Solicitud solicitud = repository.findById(id)
                .filter(s -> s.getVecinoId().equals(vecinoId))
                .orElseThrow(() -> new RecursoNoEncontradoException("Solicitud " + id + " no encontrada"));
        return mapper.toResponse(solicitud);
    }

    @Override
    @Transactional(readOnly = true)
    public SolicitudResponse obtenerPorFolio(String folio, Long vecinoId) {
        Solicitud solicitud = repository.findByFolio(folio)
                .filter(s -> s.getVecinoId().equals(vecinoId))
                .orElseThrow(() -> new RecursoNoEncontradoException("Solicitud con folio " + folio + " no encontrada"));
        return mapper.toResponse(solicitud);
    }

    @Override
    @Transactional
    public SolicitudResponse actualizarEstado(Long id, ActualizarEstadoSolicitudRequest request) {
        Solicitud solicitud = repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Solicitud " + id + " no encontrada"));

        validarTransicion(solicitud.getEstado(), request.estado());

        solicitud.setEstado(request.estado());
        if (request.camionId() != null) {
            solicitud.setCamionId(request.camionId());
        }
        if (request.paradaId() != null) {
            solicitud.setParadaId(request.paradaId());
        }
        if (request.motivoFallo() != null) {
            solicitud.setMotivoFallo(request.motivoFallo());
        }
        solicitud.setFechaActualizacion(LocalDateTime.now());

        return mapper.toResponse(repository.save(solicitud));
    }

    // Válidas: PENDIENTE -> ASIGNADA, ASIGNADA -> RETIRADA, ASIGNADA -> FALLIDA
    private void validarTransicion(EstadoSolicitud actual, EstadoSolicitud nuevo) {
        boolean valida = switch (actual) {
            case PENDIENTE -> nuevo == EstadoSolicitud.ASIGNADA;
            case ASIGNADA -> nuevo == EstadoSolicitud.RETIRADA || nuevo == EstadoSolicitud.FALLIDA;
            case RETIRADA, FALLIDA -> false;
        };
        if (!valida) {
            throw new ReglaNegocioException("No se puede cambiar de " + actual + " a " + nuevo);
        }
    }
}
