package com.reservas.practica.reserva.dto;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO que representa la respuesta del endpoint de disponibilidad.
 * Contiene el id de la cancha, la fecha consultada y la lista de turnos (slots) generados.
 */
public record DisponibilidadResponse(
    Long canchaId,
    LocalDate fecha,
    List<TurnoDto> turnos
) {}
