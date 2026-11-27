package com.reservas.practica.tarifa;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TarifaHorariaRepository extends JpaRepository<TarifaHoraria, Long> {
    Optional<TarifaHoraria> findFirstByCanchaId(Long canchaId);
}
