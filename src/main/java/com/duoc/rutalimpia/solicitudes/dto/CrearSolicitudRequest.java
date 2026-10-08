package com.duoc.rutalimpia.solicitudes.dto;

import com.duoc.rutalimpia.solicitudes.model.enums.BloqueHorario;
import com.duoc.rutalimpia.solicitudes.model.enums.TipoResiduo;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CrearSolicitudRequest(

        @NotBlank(message = "es obligatoria")
        @Size(max = 200, message = "máximo 200 caracteres")
        String direccion,

        @NotBlank(message = "es obligatoria")
        @Size(max = 80, message = "máximo 80 caracteres")
        String comuna,

        @NotNull(message = "es obligatorio")
        TipoResiduo tipoResiduo,

        @NotNull(message = "es obligatoria")
        @FutureOrPresent(message = "no puede ser una fecha pasada")
        LocalDate fechaPreferida,

        @NotNull(message = "es obligatorio")
        BloqueHorario bloqueHorario,

        @Size(max = 300, message = "máximo 300 caracteres")
        String observaciones
) {
}
