package com.reservas.practica.complejo;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "cancha")
public class Cancha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "complejo_id", nullable = false)
    private Complejo complejo;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @Column(name = "activa", nullable = false)
    private boolean activa = true;

    public Cancha() {
    }

    public Cancha(Complejo complejo, String nombre, boolean activa) {
        this.complejo = complejo;
        this.nombre = nombre;
        this.activa = activa;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Complejo getComplejo() {
        return complejo;
    }

    public void setComplejo(Complejo complejo) {
        this.complejo = complejo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cancha cancha = (Cancha) o;
        return Objects.equals(id, cancha.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
