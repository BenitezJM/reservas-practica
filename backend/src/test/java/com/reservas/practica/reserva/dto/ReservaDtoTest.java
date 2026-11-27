package com.reservas.practica.reserva.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.reservas.practica.complejo.Cancha;
import com.reservas.practica.complejo.Complejo;
import com.reservas.practica.reserva.EstadoReserva;
import com.reservas.practica.reserva.Reserva;
import com.reservas.practica.usuario.RolUsuario;
import com.reservas.practica.usuario.Usuario;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ReservaDtoTest {

    private ObjectMapper objectMapper;
    private Validator validator;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    @DisplayName("TurnoDto y DisponibilidadResponse se serializan correctamente a JSON con formato HH:mm y yyyy-MM-dd")
    void testDisponibilidadResponseSerialization() throws Exception {
        TurnoDto turno = new TurnoDto(
                LocalTime.of(8, 0),
                LocalTime.of(9, 0),
                new BigDecimal("5000.00"),
                true
        );
        DisponibilidadResponse response = new DisponibilidadResponse(
                1L,
                LocalDate.of(2026, 11, 20),
                List.of(turno)
        );

        String json = objectMapper.writeValueAsString(response);

        assertThat(json).contains("\"canchaId\":1");
        assertThat(json).contains("\"fecha\":\"2026-11-20\"");
        assertThat(json).contains("\"horaInicio\":\"08:00\"");
        assertThat(json).contains("\"horaFin\":\"09:00\"");
        assertThat(json).contains("\"precio\":5000.00");
        assertThat(json).contains("\"disponible\":true");
    }

    @Test
    @DisplayName("CrearReservaRequest válido pasa la validación Bean Validation")
    void testCrearReservaRequestValidationSuccess() {
        CrearReservaRequest request = new CrearReservaRequest(
                1L,
                2L,
                OffsetDateTime.of(2026, 11, 20, 8, 0, 0, 0, ZoneOffset.UTC),
                OffsetDateTime.of(2026, 11, 20, 9, 0, 0, 0, ZoneOffset.UTC)
        );

        Set<ConstraintViolation<CrearReservaRequest>> violations = validator.validate(request);

        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("CrearReservaRequest con campos nulos falla validación con mensajes esperados")
    void testCrearReservaRequestValidationFailure() {
        CrearReservaRequest request = new CrearReservaRequest(null, null, null, null);

        Set<ConstraintViolation<CrearReservaRequest>> violations = validator.validate(request);

        assertThat(violations).hasSize(3);
        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(
                        "El id de la cancha es obligatorio",
                        "La fecha y hora de inicio es obligatoria",
                        "La fecha y hora de fin es obligatoria"
                );
    }

    @Test
    @DisplayName("CrearReservaRequest se deserializa correctamente desde JSON ISO-8601")
    void testCrearReservaRequestDeserialization() throws Exception {
        String json = """
                {
                    "canchaId": 1,
                    "usuarioId": 2,
                    "fechaInicio": "2026-11-20T08:00:00Z",
                    "fechaFin": "2026-11-20T09:00:00Z"
                }
                """;

        CrearReservaRequest request = objectMapper.readValue(json, CrearReservaRequest.class);

        assertThat(request.canchaId()).isEqualTo(1L);
        assertThat(request.usuarioId()).isEqualTo(2L);
        assertThat(request.fechaInicio()).isEqualTo(OffsetDateTime.of(2026, 11, 20, 8, 0, 0, 0, ZoneOffset.UTC));
        assertThat(request.fechaFin()).isEqualTo(OffsetDateTime.of(2026, 11, 20, 9, 0, 0, 0, ZoneOffset.UTC));
    }

    @Test
    @DisplayName("ReservaResponse mapea correctamente desde entidad Reserva")
    void testReservaResponseFromEntity() {
        Complejo complejo = new Complejo("Complejo Central", LocalTime.of(8, 0), LocalTime.of(23, 0));
        complejo.setId(10L);

        Cancha cancha = new Cancha(complejo, "Cancha 1", true);
        cancha.setId(1L);

        Usuario usuario = new Usuario("juan@example.com", "Juan Perez", RolUsuario.CLIENTE);
        usuario.setId(2L);

        OffsetDateTime inicio = OffsetDateTime.of(2026, 11, 20, 8, 0, 0, 0, ZoneOffset.UTC);
        OffsetDateTime fin = OffsetDateTime.of(2026, 11, 20, 9, 0, 0, 0, ZoneOffset.UTC);
        OffsetDateTime creadaEn = OffsetDateTime.of(2026, 11, 17, 16, 0, 0, 0, ZoneOffset.UTC);

        Reserva reserva = new Reserva(cancha, usuario, inicio, fin, EstadoReserva.CONFIRMADA, new BigDecimal("5000.00"));
        reserva.setId(100L);

        try {
            var creadaEnField = Reserva.class.getDeclaredField("creadaEn");
            creadaEnField.setAccessible(true);
            creadaEnField.set(reserva, creadaEn);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        ReservaResponse response = ReservaResponse.fromEntity(reserva);

        assertThat(response.id()).isEqualTo(100L);
        assertThat(response.canchaId()).isEqualTo(1L);
        assertThat(response.usuarioId()).isEqualTo(2L);
        assertThat(response.fechaInicio()).isEqualTo(inicio);
        assertThat(response.fechaFin()).isEqualTo(fin);
        assertThat(response.estado()).isEqualTo(EstadoReserva.CONFIRMADA);
        assertThat(response.precioFinal()).isEqualByComparingTo("5000.00");
        assertThat(response.creadaEn()).isEqualTo(creadaEn);
    }
}
