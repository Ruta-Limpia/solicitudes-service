package com.duoc.rutalimpia.solicitudes.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Solicitud {
    @Id
    @GeneratedValue
    private Long id;

    private String folio;


    private Long vecinoId;

    private String direccion;

    private String Comuna;

    private Object tipoResiduo;

    private LocalDate fechaPreferida;

    private Object bloqueHorario;

    private String observaciones;

    private Object estado;

    private Long camionId;

    private Long paradaId;

    private String motivoFallo;

    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaActualizacion;


}
