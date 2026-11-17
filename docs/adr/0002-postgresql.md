# ADR 0002: PostgreSQL como base de datos

- Estado: aceptada
- Contexto: la regla R1 exige impedir reservas solapadas con concurrencia.
- Decisión: PostgreSQL, que permite exclusion constraints sobre rangos de tiempo (extensión `btree_gist`).
- Alternativas: MySQL, H2 (descartada para tests: no reproduce el comportamiento real).
- Consecuencias: la integridad de R1 queda garantizada en la base; se acopla a una característica de PostgreSQL.
- Pendiente: ADR 0003 comparando exclusion constraint vs locking optimista/pesimista en JPA.
