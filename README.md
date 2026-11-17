# Reservas Práctica

Aplicación de reserva de canchas deportivas. Proyecto de práctica para entrevistas full stack,
desarrollado con Spec-Driven Development (SDD) y agentes en Antigravity.

## Stack
- Backend: Java 21, Spring Boot 4.1.x, Hibernate/JPA, Flyway, PostgreSQL, Maven
- Frontend: Angular 22, TypeScript 6, Node.js 24.x
- Infra local: Docker Compose

## Estructura
- `specs/` especificaciones por feature (spec → plan → tasks)
- `docs/` modelo de dominio y ADR (decisiones de arquitectura)
- `backend/` API REST
- `frontend/` cliente Angular
- `.agents/` skills, MCP y hooks para Antigravity

## Puesta en marcha
```bash
docker compose up -d        # PostgreSQL
# backend y frontend: ver backend/AGENTS.md y frontend/AGENTS.md
```

## Estado
Ver `specs/` para las features y su avance.
