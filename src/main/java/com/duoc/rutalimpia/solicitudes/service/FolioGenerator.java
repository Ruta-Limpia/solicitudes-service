package com.duoc.rutalimpia.solicitudes.service;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class FolioGenerator {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyyMMdd");

    // Ejemplo: RL-20261007-000042
    public String generar(Long id) {
        return "RL-" + LocalDate.now().format(FORMATO_FECHA) + "-" + String.format("%06d", id);
    }
}
