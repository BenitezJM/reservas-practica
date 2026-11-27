package com.reservas.practica.reserva.dto;

import com.reservas.practica.reserva.EstadoReserva;
import com.reservas.practica.reserva.Reserva;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record ReservaResponse(
    Long id,
    Long canchaId,
    Long usuarioId,
    OffsetDateTime fechaInicio,
    OffsetDateTime fechaFin,
    EstadoReserva estado,
    BigDecimal precioFinal,
    OffsetDateTime creadaEn
) {
    public static ReservaResponse fromEntity(Reserva reserva) {
        return new ReservaResponse(
            reserva.getId(),
            reserva.getCancha().getId(),
            reserva.getUsuario().getId(),
            reserva.getFechaInicio(),
            reserva.getFechaFin(),
            reserva.getEstado(),
            reserva.getPrecioFinal(),
            reserva.getCreadaEn()
        );
    }
}
