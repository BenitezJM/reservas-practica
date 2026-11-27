package com.reservas.practica.complejo;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CanchaRepository extends JpaRepository<Cancha, Long> {
    List<Cancha> findByComplejoIdAndActivaTrue(Long complejoId);
}
