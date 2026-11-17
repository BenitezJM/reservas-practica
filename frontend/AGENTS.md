# Frontend (Angular 22 + TypeScript 6)

Requiere Node.js 24.x (o 22.x reciente). Estructura en `src/app/`: `core/` (auth, interceptores, guards),
`shared/` (componentes comunes), `features/` (`auth`, `disponibilidad`, `reservas`, `admin`).

## Reglas
- Solo componentes standalone (sin NgModules). Nombres y APIs según la documentación oficial vigente de Angular 22.
- Estado con signals; `computed` para derivados; `resource` para cargas asíncronas.
- Formularios con Signal Forms (estable en Angular 22) cuando aplique.
- Control flow integrado (`@if`, `@for`), no `*ngIf`/`*ngFor`.
- Inyección con `inject()`. Rutas con carga diferida (`loadComponent` / `loadChildren`).
- OnPush es la estrategia por defecto en Angular 22; no hace falta declararla.
- HTTP: `HttpClient` con interceptores funcionales (token JWT, manejo de errores). Servicios por feature.
- Guards por rol (`ADMIN`, `CLIENTE`).
- TypeScript estricto; prohibido `any` salvo justificación.
- Verifica la API en angular.dev antes de usar algo que no conozcas; no la inventes.

## Comandos (desde `frontend/`)
`npm install` · `npm start` · `npm test` · `npm run build`
