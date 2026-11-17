# ADR 0003: Cambio gestor base de datos a Liquibase

- Estado: aceptada
- Contexto: se requiere una herramienta de control de versiones y migraciones de esquema de base de datos para PostgreSQL en Spring Boot
- Decisión: usar Liquibase con changelogs en formato XML (db.changelog-master.xml y changelogs modulares).
- Alternativas: Flyway (propuesta inicial, descartada por preferencia del equipo y soporte nativo de rollback en Liquibase).
- Consecuencias: migraciones estructuradas con soporte de rollback y verificación de precondiciones. Requiere mantener la sintaxis XML de changelogs en lugar de scripts SQL planos. 
