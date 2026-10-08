package com.duoc.rutalimpia.solicitudes.service;

import com.duoc.rutalimpia.solicitudes.dto.ActualizarEstadoSolicitudRequest;
import com.duoc.rutalimpia.solicitudes.dto.CrearSolicitudRequest;
import com.duoc.rutalimpia.solicitudes.dto.SolicitudResponse;

import java.util.List;

public interface SolicitudService {

    SolicitudResponse crear(CrearSolicitudRequest request, Long vecinoId);

    List<SolicitudResponse> listarPorVecino(Long vecinoId);

    SolicitudResponse obtenerPorId(Long id, Long vecinoId);

    SolicitudResponse obtenerPorFolio(String folio, Long vecinoId);

    SolicitudResponse actualizarEstado(Long id, ActualizarEstadoSolicitudRequest request);
}
