package com.reservas.practica.reserva;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    @Query("""
        SELECT r FROM Reserva r
        WHERE r.cancha.id = :canchaId
          AND r.estado IN ('CONFIRMADA', 'PENDIENTE')
          AND r.fechaFin > :desde
          AND r.fechaInicio < :hasta
    """)
    List<Reserva> findReservasActivasEnRango(
        @Param("canchaId") Long canchaId,
        @Param("desde") OffsetDateTime desde,
        @Param("hasta") OffsetDateTime hasta
    );
}
