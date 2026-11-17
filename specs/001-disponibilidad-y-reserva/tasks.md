# Tareas 001: Disponibilidad y creación de reservas

Cada tarea es pequeña, verificable, incluye sus propios tests y se debe marcar (`[x]`) al completarse.

---

### Fase 1: Cimientos del Backend (Scaffolding)
- [x] **T1.1 — Configuración inicial de Spring Boot, Maven y conexión a BD remota (Neon):**  
  Crear `backend/pom.xml` con Java 21, dependencias (Spring Web, Spring Data JPA, PostgreSQL Driver, Liquibase Core, Bean Validation, Testcontainers PostgreSQL y records nativos), clase principal `ReservasApplication.java` y `application.yml` configurado para soportar base de datos PostgreSQL remota (Neon) vía variables de entorno (`SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`) con SSL habilitado (`sslmode=require`) y protección de credenciales mediante `.env` (ignorado por Git). Mantener Testcontainers configurado para la ejecución de tests aislados.  
  *Verificación:* Compilación exitosa con `./mvnw test-compile` y ejecución del test de contexto (`./mvnw test`).

---

### Fase 2: Modelo de datos y Persistencia (Liquibase + JPA)
- [ ] **T2.1 — Changelogs de Liquibase para tablas base y Exclusion Constraint (R1):**  
  Crear changelog XML para las tablas (`complejo`, `cancha`, `tarifa_horaria`, `usuario`, `reserva`) y changelog para la extensión `btree_gist` y la `EXCLUDE CONSTRAINT` de no solapamiento. Incluirlos en `db.changelog-master.xml`.  
  *Verificación:* Test de integración con Testcontainers que aplique las migraciones en PostgreSQL y valide la creación del esquema y la constraint.
- [ ] **T2.2 — Entidades JPA y Repositorios:**  
  Crear las entidades `@Entity` (`Complejo`, `Cancha`, `TarifaHoraria`, `Usuario`, `Reserva`) con relaciones `LAZY` y sus correspondientes interfaces `JpaRepository`.  
  *Verificación:* Test de persistencia con `@DataJpaTest` o Testcontainers guardando y consultando entidades.
- [ ] **T2.3 — Changelog de datos semilla (Seed data):**  
  Crear changelog XML con datos iniciales (1 complejo con horario 08:00 a 23:00, 2 canchas, tarifa horaria y 1 usuario de prueba) para desarrollo local y tests.  
  *Verificación:* Inicio de la aplicación contra PostgreSQL local o de test confirmando los registros cargados.

---

### Fase 3: Lógica de negocio, API REST y Tests (Spring Boot)
- [ ] **T3.1 — DTOs y Manejo global de excepciones:**  
  Definir los DTOs inmutables (records de Java) para disponibilidad y reservas. Crear `GlobalExceptionHandler` (`@RestControllerAdvice`) que formatee errores en estándar *Problem Details* (RFC 7807) para errores 400, 404, 409 y 500.  
  *Verificación:* Test unitario del handler verificando la estructura de error JSON ante excepciones de validación y de conflicto.
- [ ] **T3.2 — Endpoint de Disponibilidad (GET /canchas/{id}/disponibilidad):**  
  Implementar `DisponibilidadService` y `DisponibilidadController`. Generar slots de 60 minutos según el horario del complejo (R2), verificar fechas pasadas (R6) y marcar turnos libres u ocupados según reservas existentes.  
  *Verificación:* Tests unitarios del cálculo de turnos y test de integración `DisponibilidadControllerIT` contra Testcontainers.
- [ ] **T3.3 — Endpoint de Creación de Reserva (POST /reservas):**  
  Implementar `ReservaService` y `ReservaController`. Validar horario, calcular y congelar tarifa (R3), persistir la reserva en estado `CONFIRMADA` y capturar violación de constraint (R1) para responder `409 Conflict`.  
  *Verificación:* Test de integración `ReservaControllerIT` probando creación exitosa (201) y rechazo por turno ocupado (409).
- [ ] **T3.4 — Test de Concurrencia para Regla R1:**  
  Escribir un test de integración concurrente con `ExecutorService` o hilos paralelos que intente reservar el mismo turno exactamente al mismo tiempo con dos usuarios distintos.  
  *Verificación:* Uno de los hilos obtiene `201 Created` y el otro obtiene `409 Conflict`, garantizando cero reservas solapadas en la base.

---

### Fase 4: Frontend y UI (Angular 22)
- [ ] **T4.1 — Inicialización del proyecto Angular:**  
  Inicializar el esqueleto del frontend en `frontend/` con Angular 22, componentes standalone, configuración de routing y configuración de `provideHttpClient()`.  
  *Verificación:* `npm test` y `npm start` compilan sin errores.
- [ ] **T4.2 — Servicio HTTP de Disponibilidad y Reservas:**  
  Crear `disponibilidad.service.ts` con métodos tipados para consultar turnos y solicitar reserva contra la API backend.  
  *Verificación:* Test unitario del servicio con `HttpTestingController`.
- [ ] **T4.3 — Componente interactivo de Calendario y Turnos:**  
  Crear componente standalone con selector de fecha, listado de turnos usando Signals (`signal`, `computed`), estados visuales (disponible / no disponible) y botón de confirmación de reserva.  
  *Verificación:* Test unitario del componente y verificación manual en el navegador conectando con el backend.
