# Dominio: Reservas de canchas

## Roles
- **CLIENTE:** se registra, consulta disponibilidad, reserva, ve y cancela sus reservas.
- **ADMIN:** gestiona complejos, canchas y tarifas; ve todas las reservas.

## Entidades
- `Complejo` 1—N `Cancha`
- `Cancha` 1—N `Reserva`
- `Cancha` 1—N `TarifaHoraria` (precio por franja)
- `Usuario` 1—N `Reserva`
- `Reserva`: estados `PENDIENTE`, `CONFIRMADA`, `CANCELADA` (opcional `COMPLETADA`)

## Reglas de negocio
- **R1:** una cancha no puede tener dos reservas activas que se solapen.
- **R2:** turnos de duración fija, dentro del horario de apertura del complejo.
- **R3:** el precio se calcula con la tarifa vigente al reservar y se guarda en la reserva.
- **R4:** solo son válidas ciertas transiciones de estado.
- **R5:** el cliente solo cancela con al menos X horas de anticipación (configurable).
- **R6:** no se puede reservar en el pasado.

## Fuera del MVP
Pagos reales, notificaciones por email, lista de espera, reservas recurrentes, reseñas.

## Glosario
- **Turno:** franja de tiempo reservable de una cancha.
- **Reserva activa:** reserva en estado `PENDIENTE` o `CONFIRMADA`.
