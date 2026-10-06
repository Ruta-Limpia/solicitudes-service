package com.duoc.rutalimpia.solicitudes.repository;

import com.duoc.rutalimpia.solicitudes.model.Solicitud;
import com.duoc.rutalimpia.solicitudes.model.enums.EstadoSolicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {

    List<Solicitud> findByVecinoIdOrderByFechaCreacionDesc(Long vecinoId);

    Optional<Solicitud> findByFolio(String folio);

    List<Solicitud> findByEstado(EstadoSolicitud estado);
}
