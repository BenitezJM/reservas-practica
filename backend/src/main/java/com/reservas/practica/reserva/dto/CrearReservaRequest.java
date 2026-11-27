package com.reservas.practica.reserva.dto;

import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;

public record CrearReservaRequest(
    @NotNull(message = "El id de la cancha es obligatorio")
    Long canchaId,

    Long usuarioId,

    @NotNull(message = "La fecha y hora de inicio es obligatoria")
    OffsetDateTime fechaInicio,

    @NotNull(message = "La fecha y hora de fin es obligatoria")
    OffsetDateTime fechaFin
) {}
