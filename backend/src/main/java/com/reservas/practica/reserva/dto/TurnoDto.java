package com.reservas.practica.reserva.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.LocalTime;

public record TurnoDto(
    @JsonFormat(pattern = "HH:mm")
    LocalTime horaInicio,

    @JsonFormat(pattern = "HH:mm")
    LocalTime horaFin,

    BigDecimal precio,
    boolean disponible
) {}
