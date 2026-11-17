# Reservas Práctica
Reserva de canchas deportivas (monorepo: `backend/` Java + `frontend/` Angular).
Proyecto de práctica para entrevistas

## Cómo trabajamos (SDD)
1. Toda feature nace en `specs/NNN-nombre/` con `spec.md` → `plan.md` → `tasks.md`.
2. No implementes nada que no esté en una spec aprobada. Si falta información, pregunta o propón cambios a la spec.
3. Implementa tarea por tarea (`tasks.md`), con tests. Marca cada tarea al completarla.
4. Decisiones técnicas relevantes se registran como ADR en `docs/adr/`.
5. Principios no negociables: `specs/000-constitution.md`.

## Reglas para el agente
- Explica brevemente qué hiciste y por qué en cada cambio; el autor debe entenderlo.
- Cambios pequeños y revisables. No refactorices fuera del alcance de la tarea.
- No agregues dependencias sin justificarlas.
- No asumas versiones ni nombres de dependencias de memoria (Spring Boot 4 y Angular 22 son recientes): verifica en la documentación oficial.
- Ante la duda entre simple y "elegante", elige simple.

## Idioma y nombres
- Dominio en español (`Reserva`, `Cancha`, `Complejo`); sufijos técnicos en inglés (`Controller`, `Service`, `Repository`).
- Documentación y mensajes de commit en inglés. Commits tipo Conventional Commits.

## Versiones
Java 21 · Spring Boot 4.1.x · Maven · PostgreSQL 17 · Angular 22 · TypeScript 6 · Node.js 24.x

## Comandos
- Base de datos: `docker compose up -d`
- Backend (desde `backend/`): `./mvnw spring-boot:run` · tests: `./mvnw verify`
- Frontend (desde `frontend/`): `npm install` · `npm start` · `npm test`

## Más contexto
- Dominio y glosario: `docs/dominio.md`
- Convenciones backend: `backend/AGENTS.md`
- Convenciones frontend: `frontend/AGENTS.md`
