# ADR 0001: Monorepo y organización por feature

- Estado: aceptada
- Contexto: backend y frontend de un mismo producto, desarrollados por una sola persona con agentes.
- Decisión: un único repositorio; en el backend, paquetes por feature (no por capa).
- Alternativas: dos repositorios; paquetes por capa.
- Consecuencias: specs que cubren API + UI, contexto completo para el agente; menos aislamiento entre front y back.
