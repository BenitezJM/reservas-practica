# Plan 001: Disponibilidad y creación de reservas

## Enfoque por fases
Para cumplir con el Principio 10 de la Constitución (*"Cambios pequeños. Una tarea, un cambio revisable, un commit"*), el plan se organiza en 4 fases incrementales:
1. **Fase 1: Cimientos del Backend (Scaffolding)**
2. **Fase 2: Modelo de datos y Persistencia (Liquibase + JPA)**
3. **Fase 3: Lógica de negocio, API REST y Tests (Spring Boot + Testcontainers)**
4. **Fase 4: Frontend y UI (Angular 22 Standalone)**

---

## 1. Modelo de datos (PostgreSQL + Liquibase)

### Tablas requeridas para la feature:
1. **`complejo`**:
   - `id` (BIGSERIAL / BIGINT, PK)
   - `nombre` (VARCHAR(100), NOT NULL)
   - `hora_apertura` (TIME, NOT NULL) - ej: `08:00:00`
   - `hora_cierre` (TIME, NOT NULL) - ej: `23:00:00`
2. **`cancha`**:
   - `id` (BIGSERIAL / BIGINT, PK)
   - `complejo_id` (BIGINT, FK a `complejo(id)`, NOT NULL)
   - `nombre` (VARCHAR(50), NOT NULL) - ej: "Cancha 1 - Fútbol 5"
   - `activa` (BOOLEAN, NOT NULL, DEFAULT true)
3. **`tarifa_horaria`**:
   - `id` (BIGSERIAL / BIGINT, PK)
   - `cancha_id` (BIGINT, FK a `cancha(id)`, NOT NULL)
   - `precio` (NUMERIC(10, 2), NOT NULL)
4. **`usuario`**:
   - `id` (BIGSERIAL / BIGINT, PK)
   - `email` (VARCHAR(150), UNIQUE, NOT NULL)
   - `nombre` (VARCHAR(100), NOT NULL)
   - `rol` (VARCHAR(20), NOT NULL) - `CLIENTE` o `ADMIN`
5. **`reserva`**:
   - `id` (BIGSERIAL / BIGINT, PK)
   - `cancha_id` (BIGINT, FK a `cancha(id)`, NOT NULL)
   - `usuario_id` (BIGINT, FK a `usuario(id)`, NOT NULL)
   - `fecha_inicio` (TIMESTAMPTZ, NOT NULL)
   - `fecha_fin` (TIMESTAMPTZ, NOT NULL)
   - `estado` (VARCHAR(20), NOT NULL) - `CONFIRMADA`, `CANCELADA`
   - `precio_final` (NUMERIC(10, 2), NOT NULL)
   - `creada_en` (TIMESTAMPTZ, NOT NULL, DEFAULT CURRENT_TIMESTAMP)

### Restricción crítica de integridad (Regla R1):
- Uso de la extensión `btree_gist` en PostgreSQL.
- **Exclusion Constraint** en la tabla `reserva` para impedir solapamientos en reservas activas:
  ```sql
  CREATE EXTENSION IF NOT EXISTS btree_gist;
  ALTER TABLE reserva ADD CONSTRAINT no_solapar_reservas_activas
  EXCLUDE USING gist (
      cancha_id WITH =,
      tstzrange(fecha_inicio, fecha_fin) WITH &&
  ) WHERE (estado IN ('CONFIRMADA', 'PENDIENTE'));
  ```

### Estructura de migraciones en Liquibase (XML):
- `src/main/resources/db/changelog/db.changelog-master.xml` incluye:
  - `001-crear-tablas-iniciales.xml`: creación de tablas, índices y claves foráneas.
  - `002-exclusion-constraint-reserva.xml`: habilitación de `btree_gist` y creación del constraint de no solapamiento.
  - `003-datos-semilla-desarrollo.xml`: carga inicial de un complejo, 2 canchas, tarifa y 1 usuario de prueba para poder probar en desarrollo local.

---

## 2. Contrato de API (Endpoints REST)

Base path: `/api/v1`

### 2.1. Consultar disponibilidad de una cancha
- **Método y ruta:** `GET /canchas/{canchaId}/disponibilidad?fecha=YYYY-MM-DD`
- **Rol:** Público o `CLIENTE`
- **Respuesta 200 OK:**
  ```json
  {
    "canchaId": 1,
    "fecha": "2026-11-20",
    "turnos": [
      {
        "horaInicio": "08:00",
        "horaFin": "09:00",
        "precio": 5000.00,
        "disponible": true
      },
      {
        "horaInicio": "09:00",
        "horaFin": "10:00",
        "precio": 5000.00,
        "disponible": false
      }
    ]
  }
  ```
- **Errores:**
  - `400 Bad Request`: Si la fecha tiene formato inválido o es anterior a hoy (Regla R6).
  - `404 Not Found`: Si la `canchaId` no existe o no está activa.

### 2.2. Crear una reserva
- **Método y ruta:** `POST /reservas`
- **Rol:** `CLIENTE`
- **Request Body:**
  ```json
  {
    "canchaId": 1,
    "fechaInicio": "2026-11-20T08:00:00Z",
    "fechaFin": "2026-11-20T09:00:00Z"
  }
  ```
- **Respuesta 201 Created:**
  ```json
  {
    "id": 10,
    "canchaId": 1,
    "usuarioId": 2,
    "fechaInicio": "2026-11-20T08:00:00Z",
    "fechaFin": "2026-11-20T09:00:00Z",
    "estado": "CONFIRMADA",
    "precioFinal": 5000.00,
    "creadaEn": "2026-11-17T16:00:00Z"
  }
  ```
- **Errores:**
  - `400 Bad Request`: Horario fuera de apertura del complejo, duración distinta a 60 min, o fecha en el pasado.
  - `404 Not Found`: Cancha o usuario no encontrado.
  - `409 Conflict`: Si el turno ya se encuentra reservado por otra reserva activa (Regla R1).

---

## 3. Backend (Java 21 + Spring Boot 3.4.x)

Organización en paquetes por feature:

```
com.reservas.practica
├── common
│   ├── exception (GlobalExceptionHandler, ProblemDetails)
│   └── config (SecurityConfig básica / CORS)
├── complejo
│   ├── Complejo.java, Cancha.java
│   └── CanchaRepository.java
├── tarifa
│   ├── TarifaHoraria.java
│   └── TarifaRepository.java
├── usuario
│   ├── Usuario.java
│   └── UsuarioRepository.java
└── reserva
    ├── Reserva.java
    ├── ReservaRepository.java
    ├── ReservaService.java (crear reserva, validar R1/R2/R3/R6)
    ├── DisponibilidadService.java (generar slots del día y contrastar con reservas)
    ├── ReservaController.java
    ├── DisponibilidadController.java
    └── dto
        ├── DisponibilidadResponse.java (record)
        ├── TurnoDto.java (record)
        ├── CrearReservaRequest.java (record)
        └── ReservaResponse.java (record)
```

---

## 4. Frontend (Angular 22)

Estructura modular en `frontend/src/app`:
- `core/`: Configuración de `HttpClient`, interceptor de API URL base.
- `features/disponibilidad/`:
  - `disponibilidad.service.ts`: llamadas HTTP para turnos y reservas.
  - `components/calendario-turnos/`:
    - Selector de fecha (`<input type="date">` o similar, validando mínimo hoy).
    - Grilla de tarjetas o botones para cada franja horaria (los no disponibles aparecen deshabilitados/grises).
    - Estado manejado con Signals: `selectedDate = signal(...)`, `turnosResource = resource(...)`.
    - Modal o confirmación para reservar el turno seleccionado.

---

## 5. Decisiones y alternativas
- **ADR 0001 (Monorepo y feature packages):** Se respeta la estructura de paquetes por feature.
- **ADR 0002 (PostgreSQL):** Se aprovecha `btree_gist` para la exclusion constraint de rangos de tiempo.
- **ADR 0003 (Liquibase XML):** Migraciones administradas vía changelogs XML modulares incluidos en `db.changelog-master.xml`.

---

## 6. Estrategia de tests

1. **Unitarios (JUnit 5 + AssertJ):**
   - Validar algoritmos de generación de turnos en `DisponibilidadService` (horario de apertura vs intervalos de 60 minutos).
   - Validar validaciones de fecha futura (R6).
2. **Integración con Testcontainers (PostgreSQL real):**
   - `DisponibilidadControllerIT`: consultar turnos y verificar que los turnos con reservas confirmadas aparecen como no disponibles.
   - `ReservaControllerIT`: flujo completo de crear una reserva y persistencia en BD.
   - **Test de Concurrencia (Crucial para R1):**
     Ejecutar 2 peticiones concurrentes simultáneas (`ExecutorService` con 2 hilos) intentando reservar exactamente el mismo turno. Se verifica que una retorna `201 Created` y la otra `409 Conflict`, sin excepciones no controladas.

---

## 7. Riesgos y mitigaciones
- **Riesgo: Condición de carrera (Race Condition) en reservas simultáneas.**
  - *Mitigación:* Se previene a nivel de base de datos con la `EXCLUDE CONSTRAINT` de PostgreSQL y en Spring Boot se captura la violación de integridad (`DataIntegrityViolationException`) traduciéndola a código HTTP `409 Conflict` (Problem Details).
- **Riesgo: Manejo de zonas horarias entre frontend y backend.**
  - *Mitigación:* Se almacena y transmite todo en UTC (`TIMESTAMPTZ` e ISO 8601 con sufijo `Z`).
