# Backend (Java 21 + Spring Boot 4.1.x)

Paquete base: `com.reservas.practica`. Organización **por feature**, no por capa:
`usuario`, `complejo` (complejo + cancha), `tarifa`, `reserva`, `common`.
Dentro de cada feature: `*Controller`, `*Service`, `*Repository`, entidad y `dto/`.

## Reglas
- Las entidades JPA nunca se exponen en la API: usar DTOs (records de Java).
- Controllers finos; la lógica de negocio vive en los `Service`.
- Transacciones en la capa de servicio (`@Transactional`), lecturas con `readOnly = true`.
- Asociaciones `LAZY` por defecto. Evitar N+1: `JOIN FETCH`, `@EntityGraph` o proyecciones; verificarlo con tests o logs de SQL.
- Esquema solo vía Liquibase (`src/main/resources/db/changelog`).
  `spring.jpa.hibernate.ddl-auto` en `validate`. Nunca editar una migración ya aplicada.
- Validación con Bean Validation en los DTOs de entrada.
- Errores con un manejador global (`@RestControllerAdvice`) y formato Problem Details.
- Seguridad: Spring Security, JWT, roles `ADMIN` y `CLIENTE`. Nunca guardar contraseñas en claro.
- Usar características de Java 21 cuando aporten claridad (records, switch con patrones, `Optional`).
- Spring Boot 4 cambió nombres de starters y paquetes respecto a 3.x: verificar en la documentación oficial antes de agregar dependencias.

## Tests
- Unitarios para reglas de negocio (JUnit 5, sin Spring cuando sea posible).
- Integración con Testcontainers (PostgreSQL real, no H2).
- La regla R1 (no solapar reservas) requiere un test de concurrencia.

## Comandos (desde `backend/`)
`./mvnw spring-boot:run` · `./mvnw test` · `./mvnw verify`
