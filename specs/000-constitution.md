# Constitución del proyecto

Principios no negociables. Toda spec, plan y tarea debe respetarlos.

1. **Entendimiento primero.** Si el autor no puede explicar un fragmento de código, no entra al repositorio.
2. **Spec antes que código.** No se implementa sin spec aprobada con criterios de aceptación verificables.
3. **Simplicidad.** La solución más simple que cumpla la spec. Sin abstracciones especulativas.
4. **La integridad la garantiza la base de datos.** Las reglas críticas (como R1, no solapar reservas) se imponen también en PostgreSQL, no solo en el código.
5. **Tests reales.** Integración contra PostgreSQL con Testcontainers. Cada regla de negocio tiene al menos un test.
6. **Migraciones inmutables.** El esquema cambia solo con nuevas migraciones Liquibase.
7. **Contrato explícito.** La API se documenta con OpenAPI y no expone entidades JPA.
8. **Seguridad por defecto.** Endpoints protegidos salvo los públicos explícitos; validación de entrada siempre.
9. **Decisiones registradas.** Todo trade-off relevante tiene un ADR en `docs/adr/`.
10. **Cambios pequeños.** Una tarea, un cambio revisable, un commit.
