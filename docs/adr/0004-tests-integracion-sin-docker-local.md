# ADR 0004: Estrategia de tests de integración sin Docker local

- Estado: aceptada
- Contexto: el principio 5 de la Constitución exige tests reales contra PostgreSQL (para verificar exclusion constraints, btree_gist y concurrencia de la regla R1), pero el entorno de desarrollo local no dispone de Docker instalado ni recursos para ejecutar Testcontainers localmente.
- Decisión:
  1. En desarrollo local: ejecutar tests de integración contra una base de datos o branch de pruebas dedicada en Neon vía perfil `test` (`application-test.yml`).
  2. En integración continua (CI): ejecutar los tests con Testcontainers dentro del pipeline de GitHub Actions (donde Docker está disponible de forma nativa y gratuita).
- Alternativas:
  - Forzar instalación de Docker Desktop en la máquina local (descartada por consumo de memoria y recursos en entornos de desarrollo limitados).
  - Volver a H2 (descartada tajantemente: no soporta extensiones `btree_gist`, exclusion constraints ni el comportamiento real de PostgreSQL).
- Consecuencias: se mantiene al 100% la garantía de probar contra PostgreSQL real sin exigir Docker en la máquina del desarrollador; los tests de integración locales requieren conexión a internet hacia Neon.
