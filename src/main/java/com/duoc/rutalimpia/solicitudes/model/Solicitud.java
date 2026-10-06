package com.duoc.rutalimpia.solicitudes.model;

import com.duoc.rutalimpia.solicitudes.model.enums.BloqueHorario;
import com.duoc.rutalimpia.solicitudes.model.enums.EstadoSolicitud;
import com.duoc.rutalimpia.solicitudes.model.enums.TipoResiduo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "solicitudes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Solicitud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "folio", unique = true, length = 30)
    private String folio;

    @Column(name = "vecino_id", nullable = false)
    private Long vecinoId;

    @Column(name = "direccion", nullable = false, length = 200)
    private String direccion;

    @Column(name = "comuna", nullable = false, length = 80)
    private String comuna;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_residuo", nullable = false, length = 20)
    private TipoResiduo tipoResiduo;

    @Column(name = "fecha_preferida", nullable = false)
    private LocalDate fechaPreferida;

    @Enumerated(EnumType.STRING)
    @Column(name = "bloque_horario", nullable = false, length = 10)
    private BloqueHorario bloqueHorario;

    @Column(name = "observaciones", length = 300)
    private String observaciones;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoSolicitud estado;

    @Column(name = "camion_id")
    private Long camionId;

    @Column(name = "parada_id")
    private Long paradaId;

    @Column(name = "motivo_fallo", length = 200)
    private String motivoFallo;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
}
