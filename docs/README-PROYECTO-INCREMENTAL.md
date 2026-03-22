# Resumen del proyecto Evergreen - Sistema de distribución incremental

Este documento consolida los entregables creados para implementar el sistema de distribución de forma incremental utilizando JHipster con enfoque BDD (Behavior Driven Development) y Playwright como framework de pruebas E2E.

## Entregables

### 1. Archivos JDL para construcción incremental

Se crearon cuatro archivos JDL que permiten generar el proyecto en fases incrementales.

**Archivo jdl/00-aplicacion.jdl**

- Configuración base de la aplicación JHipster
- Sin framework de pruebas E2E por defecto (Playwright se configura manualmente)
- Dependencias: Ninguna (punto de partida)

**Archivo jdl/01-hu1-catalogos.jdl**

- Entidades: Cliente, Producto, CanalComercializacion, Transporte
- Funcionalidad: Gestión de catálogos base
- Dependencias: Requiere aplicación base

**Archivo jdl/02-hu2-pedidos.jdl**

- Entidades: Pedido (además de las entidades de HU-1)
- Funcionalidad: Gestión de pedidos con relaciones a catálogos
- Dependencias: Requiere HU-1

**Archivo jdl/03-hu3-logistica.jdl**

- Entidades: Empaque, Separacion (además de las anteriores)
- Funcionalidad: Gestión de tareas de empaque y separación
- Dependencias: Requiere HU-2

### 2. Especificaciones Gherkin (BDD)

Se crearon tres archivos `.feature` con sintaxis Gherkin que describen el comportamiento esperado del sistema. Todos los archivos se redactaron en español.

**Archivo features/HU-1-gestion-catalogos.feature**

Contiene 16 escenarios de prueba que cubren:

- Operaciones CRUD de Clientes (creación, edición, búsqueda, eliminación)
- Operaciones CRUD de Productos (validaciones, filtros)
- Operaciones CRUD de Canales de Comercialización (activación/desactivación)
- Operaciones CRUD de Transportes (tipos, capacidades, filtros)
- Validaciones de negocio

**Archivo features/HU-2-gestion-pedidos.feature**

Contiene 15 escenarios de prueba que cubren:

- Creación de pedidos (simple y con múltiples productos)
- Asignación de transporte
- Gestión de estados (Pendiente, En proceso, Despachado)
- Filtrado y búsqueda (por cliente, estado, fechas)
- Visualización de detalles
- Validaciones (cliente obligatorio, fechas coherentes)
- Paginación

**Archivo features/HU-3-gestion-tareas-logisticas.feature**

Contiene 18 escenarios de prueba que cubren:

- Operaciones CRUD de Tareas de Empaque (tipos, tamaños, tiempo)
- Operaciones CRUD de Tareas de Separación (lotes, ubicaciones)
- Filtrado por pedido y búsqueda por lote
- Visualización integrada desde pedidos
- Cálculo de tiempos y estadísticas
- Validaciones (cantidad, tiempo, campos obligatorios)
- Reportes

### 3. Documentación de implementación

**Archivo CONFIGURACION-PLAYWRIGHT.md**

Documento de configuración de Playwright:

- Instalación y configuración desde cero
- Integración con Gherkin mediante playwright-bdd
- Estructura de directorios para pruebas E2E
- Manejo de autenticación JWT
- Configuración de proyectos multi-navegador
- Scripts de ejecución
- Solución de problemas comunes

**Archivo GUIA-IMPLEMENTACION-INCREMENTAL.md**

Documento principal que incluye:

- Flujo de trabajo completo para implementar cada historia de usuario paso a paso
- Configuración inicial del proyecto con Playwright
- Comandos de generación con JHipster para cada fase
- Ejemplos de step definitions en TypeScript
- Lista de verificación por historia de usuario
- Solución de problemas comunes
- Comandos útiles para desarrollo

## Framework de pruebas E2E

El proyecto utiliza Playwright como framework de pruebas E2E por las siguientes razones:

- Soporte multi-navegador nativo (Chrome, Firefox, Safari/WebKit)
- Ejecución paralela nativa
- Mejor rendimiento
- Integración con Gherkin mediante playwright-bdd
- API moderna basada en promesas
- Storage state automático para autenticación JWT
- Herramientas de debug y codegen incluidas

## Enfoque de construcción incremental

### Ventajas del modelo adoptado

El desacoplamiento por capas de dominio permite una implementación progresiva:

- HU-1: Catálogos base (sin dependencias)
- HU-2: Procesos principales (pedidos)
- HU-3: Procesos de soporte (logística)

La validación continua garantiza que cada historia sea funcional y demostrable. Las pruebas BDD aseguran el comportamiento correcto y permiten la detección temprana de problemas.

El modelo facilita el trabajo en equipo, dado que las historias independientes pueden asignarse a diferentes desarrolladores. De este modo, los conflictos de merge se minimizan y las revisiones de código resultan más enfocadas.

Además, el enfoque permite adaptarse a cambios de requerimientos, afectando únicamente una historia. La regeneración parcial es posible mediante JHipster.

## Cobertura de escenarios BDD

En la Tabla 1 se presenta el resumen de la cobertura de escenarios por historia de usuario.

**Tabla 1.** Cobertura de escenarios BDD por historia de usuario.

| Historia | Escenarios | Entidades | Operaciones                                |
|----------|------------|-----------|-------------------------------------------|
| HU-1     | 16         | 4         | CRUD, Búsqueda, Filtrado, Validaciones    |
| HU-2     | 15         | 1 nueva   | CRUD, Relaciones, Estados, Paginación     |
| HU-3     | 18         | 2 nuevas  | CRUD, Reportes, Estadísticas, Integración |
| Total    | 49         | 7         | Cobertura completa                        |

## Estructura de archivos

```
evergreen/
│
├── jdl/
│   ├── 00-aplicacion.jdl
│   ├── 01-hu1-catalogos.jdl
│   ├── 02-hu2-pedidos.jdl
│   ├── 03-hu3-logistica.jdl
│   └── modelo-completo.jdl
│
├── features/
│   ├── HU-1-gestion-catalogos.feature
│   ├── HU-2-gestion-pedidos.feature
│   └── HU-3-gestion-tareas-logisticas.feature
│
├── docs/
│   ├── CONFIGURACION-PLAYWRIGHT.md
│   ├── GUIA-IMPLEMENTACION-INCREMENTAL.md
│   └── README-PROYECTO-INCREMENTAL.md
│
├── distribucion/                    # Proyecto JHipster
│   ├── e2e/                         # Pruebas E2E con Playwright
│   │   ├── playwright.config.ts
│   │   ├── auth.setup.ts
│   │   ├── steps/
│   │   │   ├── common.steps.ts
│   │   │   ├── catalogos.steps.ts
│   │   │   ├── pedidos.steps.ts
│   │   │   ├── empaque.steps.ts
│   │   │   └── separacion.steps.ts
│   │   ├── support/
│   │   │   ├── auth.ts
│   │   │   └── fixtures.ts
│   │   ├── fixtures/
│   │   │   └── test-data.json
│   │   ├── reports/
│   │   └── .auth/
│   │       └── admin.json
│   ├── src/
│   └── package.json
│
└── README.md
```

## Lista de verificación de implementación

### Fase 0: Configuración inicial

- Generar aplicación base con `jhipster jdl ../jdl/00-aplicacion.jdl` (desde `distribucion/`)
- Instalar Playwright: `npm install --save-dev @playwright/test playwright-bdd`
- Instalar navegadores: `npx playwright install`
- Crear estructura de directorios E2E en `distribucion/e2e/`
- Configurar `distribucion/e2e/playwright.config.ts`
- Implementar helpers de autenticación en `distribucion/e2e/support/auth.ts`
- Crear archivo de setup en `distribucion/e2e/auth.setup.ts`
- Agregar scripts al `distribucion/package.json`
- Registrar commit inicial

### Fase 1: HU-1 (Catálogos)

- Generar entidades con `jhipster jdl ../jdl/01-hu1-catalogos.jdl --force --skip-install` (desde `distribucion/`)
- Ejecutar `npm install` manualmente
- Compilar y arrancar la aplicación
- Realizar verificación manual de operaciones CRUD
- Implementar step definitions en `distribucion/e2e/steps/catalogos.steps.ts`
- Ejecutar pruebas E2E con `npm run e2e -- --grep "@HU-1"`
- Registrar commit con tag `v0.1.0`

### Fase 2: HU-2 (Pedidos)

- Generar entidades con `jhipster jdl ../jdl/02-hu2-pedidos.jdl --force --skip-install` (desde `distribucion/`)
- **Importante**: El JDL debe incluir todas las entidades en las opciones `dto`, `service`, `pagination` y `filter`
- Ejecutar `npm install` manualmente
- Verificar relaciones con catálogos
- Implementar step definitions en `distribucion/e2e/steps/pedidos.steps.ts`
- Ejecutar suite de regresión (HU-1 + HU-2)
- Registrar commit con tag `v0.2.0`

### Fase 3: HU-3 (Tareas logísticas)

- Generar entidades con `jhipster jdl ../jdl/03-hu3-logistica.jdl --force --skip-install` (desde `distribucion/`)
- **Importante**: El JDL debe incluir todas las entidades en las opciones `dto`, `service`, `pagination` y `filter`
- Ejecutar `npm install` manualmente
- Verificar relaciones con pedidos
- Implementar step definitions en `distribucion/e2e/steps/empaque.steps.ts` y `separacion.steps.ts`
- Ejecutar suite completa de regresión
- Registrar commit con tag `v1.0.0`
- Realizar release final

## Tecnologías utilizadas

- JHipster 8.x para generación de código
- Spring Boot para el backend
- React para el frontend
- JWT para autenticación
- H2 Database para desarrollo
- PostgreSQL para producción
- Playwright para pruebas E2E
- playwright-bdd para integración con Gherkin
- Gherkin/Cucumber para especificaciones BDD
- Liquibase para gestión de base de datos

## Referencias

### Documentación oficial

- JHipster Official Documentation: https://www.jhipster.tech/
- JHipster Testing Guide: https://www.jhipster.tech/running-tests/
- Playwright Documentation: https://playwright.dev/
- Cucumber Documentation: https://cucumber.io/docs/cucumber/

### Herramientas

- playwright-bdd: https://github.com/vitalets/playwright-bdd

## Información del proyecto

Proyecto Evergreen - Sistema de Distribución
EAFIT - Industrialización de Software
Semestre 2 - 2025

Este proyecto tiene fines académicos y educativos.

