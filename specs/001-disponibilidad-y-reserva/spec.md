# Spec 001: Disponibilidad y creación de reservas

## Contexto y objetivo
Permitir a los usuarios con rol `CLIENTE` consultar la disponibilidad de turnos de una cancha para una fecha específica y realizar la reserva de un turno disponible, garantizando que no existan reservas solapadas y congelando el precio acordado según la tarifa vigente.

## Alcance

### Incluye:
- Consulta de turnos (disponibles y ocupados) de una cancha para una fecha específica (`YYYY-MM-DD`).
- Creación de una reserva para un turno disponible seleccionado por un cliente autenticado.
- En el MVP sin pasarela de pagos externa, la reserva creada pasa directamente a estado `CONFIRMADA`.
- Validación estricta de solapamiento de horarios (Regla R1).
- Validación de turnos dentro del horario de apertura del complejo (Regla R2).
- Cálculo y congelamiento del precio final en la reserva según la tarifa horaria de la cancha (Regla R3).
- Validación de que el horario solicitado sea estrictamente en el futuro (Regla R6).

### Excluye:
- Cancelación de reservas por parte del cliente o administrador (se abordará en la `Spec 002: Cancelación de reservas`).
- Pasarela de pagos reales (fuera del MVP según `docs/dominio.md`).
- Administración y CRUD de complejos, canchas y tarifas (se gestiona vía administración / datos semilla iniciales).
- Notificaciones por email o SMS.

---

## Historias de usuario

### HU1: Consultar disponibilidad de una cancha
**Como** `CLIENTE`  
**Quiero** consultar los turnos disponibles de una cancha para una fecha determinada  
**Para** conocer qué horarios están libres y elegir el más conveniente.

### HU2: Crear una reserva de cancha
**Como** `CLIENTE`  
**Quiero** reservar un turno disponible indicando la cancha, fecha y horario  
**Para** asegurar mi espacio de juego con el precio confirmado.

---

## Reglas de negocio

- **R1 (Sin solapamiento):** Una cancha no puede tener dos reservas activas (`PENDIENTE` o `CONFIRMADA`) que se solapen en fecha y horario. La integridad debe garantizarse tanto a nivel de aplicación como en la base de datos (PostgreSQL).
- **R2 (Turnos válidos):** Los turnos tienen una duración fija de 60 minutos y deben comenzar y terminar dentro del horario de apertura y cierre del complejo deportivo al que pertenece la cancha (ej. de 08:00 a 23:00, iniciando en horas en punto: 08:00, 09:00, etc.).
- **R3 (Precio congelado):** Al crear la reserva, el sistema calcula el precio a partir de la tarifa horaria vigente para ese día/horario y lo guarda de forma inmutable en el registro de la reserva (`precioFinal`). Cambios futuros en las tarifas no alteran reservas previas.
- **R6 (Solo futuro):** No se permite consultar disponibilidad ni reservar turnos cuya fecha y hora de inicio pertenezcan al pasado respecto al momento de la solicitud.

---

## Criterios de aceptación

### Consulta de disponibilidad
- [ ] **CA1.1 - Fecha válida con turnos:** Dado un complejo abierto de 09:00 a 12:00 (turnos de 60 min) y una cancha sin reservas para una fecha futura, al consultar la disponibilidad se devuelven 3 turnos disponibles: `09:00 - 10:00`, `10:00 - 11:00` y `11:00 - 12:00` con sus respectivos precios.
- [ ] **CA1.2 - Turno ocupado no disponible:** Dado un turno de `10:00 - 11:00` con una reserva activa (`CONFIRMADA`), al consultar disponibilidad dicho turno se marca como `DISPONIBLE = false` (o se excluye de las opciones seleccionables).
- [ ] **CA1.3 - Fecha en el pasado:** Dado un intento de consultar una fecha anterior a la fecha actual, el sistema responde con error de validación `400 Bad Request`.

### Creación de reserva
- [ ] **CA2.1 - Reserva exitosa:** Dado un cliente autenticado y un turno libre dentro del horario válido, cuando solicita la reserva, el sistema la crea en estado `CONFIRMADA`, asigna el `precioFinal` calculado, asocia el ID del usuario y retorna código `201 Created` con los datos de la reserva.
- [ ] **CA2.2 - Intento de reserva solapada (Conflicto):** Dado un turno que ya tiene una reserva activa, cuando otro usuario intenta reservar ese mismo turno, el sistema rechaza la operación con código `409 Conflict` y un mensaje claro.
- [ ] **CA2.3 - Concurrencia (Dos reservas simultáneas):** Dados dos clientes enviando al mismo milisegundo una solicitud de reserva para el mismo turno libre, exactamente una debe completarse con éxito (`201 Created`) y la otra debe fallar con `409 Conflict`.
- [ ] **CA2.4 - Horario fuera de apertura:** Dado un turno fuera del rango de atención del complejo (ej. a las 02:00 AM), el sistema rechaza la solicitud con `400 Bad Request`.
- [ ] **CA2.5 - Reserva en el pasado:** Dado un intento de reservar un horario ya transcurrido, el sistema rechaza la solicitud con `400 Bad Request`.

---

## Casos borde y errores

- **Formato de errores:** Todos los errores de la API deben seguir el estándar RFC 7807 / RFC 9457 (*Problem Details*) con campos `title`, `status`, `detail` e `instance`.
- **Cancha inexistente:** Si el identificador de la cancha no existe, responder `404 Not Found`.
- **Complejo cerrado:** Si la fecha solicitada cae en un día en que el complejo no opera, la consulta debe devolver lista de turnos vacía o indicar que el complejo está cerrado.

---

## Preguntas abiertas
*(Ninguna por el momento. La política de cancelación y pagos diferidos se tratará en las specs 002 y posteriores).*
