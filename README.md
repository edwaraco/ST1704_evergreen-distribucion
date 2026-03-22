# Evergreen - Sistema de distribución

Este repositorio contiene el código fuente del sistema de gestión de distribución de productos desarrollado con JHipster. El proyecto se construyó mediante un enfoque de desarrollo incremental basado en historias de usuario.

## Estructura del proyecto

El repositorio se organizó de la siguiente manera:

```
evergreen/
├── distribucion/                # Proyecto JHipster (código fuente)
│
├── docker/                      # Configuración Docker para desarrollo
│   ├── docker-compose.jhipster.yml
│   ├── Dockerfile.jhipster
│   └── jhipster-shell.sh
│
├── jdl/                         # Modelos JDL incrementales
│   ├── 00-aplicacion.jdl        # Configuración base
│   ├── 01-hu1-catalogos.jdl     # HU-1: Catálogos
│   ├── 02-hu2-pedidos.jdl       # HU-2: Pedidos
│   ├── 03-hu3-logistica.jdl     # HU-3: Logística
│   └── modelo-completo.jdl      # Referencia del modelo completo
│
├── features/                    # Especificaciones BDD (Gherkin)
│   ├── HU-1-gestion-catalogos.feature
│   ├── HU-2-gestion-pedidos.feature
│   └── HU-3-gestion-tareas-logisticas.feature
│
├── e2e/                         # Pruebas E2E con Playwright
│   ├── playwright.config.ts
│   ├── steps/                   # Step definitions
│   └── support/                 # Helpers y fixtures
│
├── docs/                        # Documentación del proyecto
│
└── README.md
```

## Historias de usuario

En la Tabla 1 se presentan las tres historias de usuario que conforman el alcance del sistema.

**Tabla 1.** Historias de usuario del sistema de distribución.

| HU   | Nombre                 | Entidades                                            | Dependencias |
|------|------------------------|------------------------------------------------------|--------------|
| HU-1 | Gestión de catálogos   | Cliente, Producto, CanalComercializacion, Transporte | Ninguna      |
| HU-2 | Gestión de pedidos     | Pedido                                               | HU-1         |
| HU-3 | Tareas logísticas      | Empaque, Separacion                                  | HU-2         |

## Desarrollo con Docker

### Levantar el entorno

Para iniciar el entorno de desarrollo, se ejecutan los siguientes comandos desde el directorio raíz del proyecto:

```bash
docker compose -f docker/docker-compose.jhipster.yml up -d
docker exec -it jhipster-dev bash
```

### Estructura dentro del contenedor

Una vez dentro del contenedor, los directorios se encuentran organizados de la siguiente forma:

```
/evergreen/
├── distribucion/   # Proyecto JHipster
├── jdl/            # Modelos JDL
└── features/       # Especificaciones BDD
```

## Flujo de desarrollo incremental

El desarrollo del sistema se dividió en tres sprints, cada uno correspondiente a una historia de usuario.

### Sprint 1: HU-1 - Catálogos base

```bash
cd /evergreen/distribucion

# Generación del proyecto con entidades de catálogos
jhipster jdl ../jdl/00-aplicacion.jdl ../jdl/01-hu1-catalogos.jdl

# Compilación y verificación
./mvnw

# Registro de versión (desde el host)
git add . && git commit -m "feat(HU-1): gestión de catálogos base"
git tag v0.1.0
```

### Sprint 2: HU-2 - Pedidos

```bash
cd /evergreen/distribucion

# Aplicación de la entidad Pedido sobre el proyecto existente
jhipster jdl ../jdl/02-hu2-pedidos.jdl

# Compilación (genera nuevos changelogs de Liquibase)
./mvnw

# Registro de versión
git add . && git commit -m "feat(HU-2): gestión de pedidos"
git tag v0.2.0
```

### Sprint 3: HU-3 - Logística

```bash
cd /evergreen/distribucion

# Aplicación de entidades de logística
jhipster jdl ../jdl/03-hu3-logistica.jdl

# Compilación
./mvnw

# Registro de versión
git add . && git commit -m "feat(HU-3): tareas logísticas"
git tag v1.0.0
```

## Pruebas BDD

Los archivos `.feature` ubicados en el directorio `features/` contienen las especificaciones escritas en Gherkin.

El proyecto utiliza Playwright como framework de pruebas E2E, configurado con `playwright-bdd` para la integración con Gherkin.

### Configuración inicial

```bash
cd /evergreen/distribucion

# Instalación de Playwright y playwright-bdd
npm install --save-dev @playwright/test playwright-bdd @cucumber/cucumber

# Instalación de navegadores
npx playwright install
```

### Ejecución de pruebas

```bash
# Ejecutar todas las pruebas
npm run e2e

# Ejecutar pruebas de una historia específica
npm run e2e -- --grep "@HU-1"

# Ejecutar con interfaz visual
npm run e2e:headed

# Modo debug
npm run e2e:debug

# Ver reporte HTML
npm run e2e:report
```

Para más detalles sobre la configuración de Playwright, consultar `docs/CONFIGURACION-PLAYWRIGHT.md`.

## Migraciones de base de datos

JHipster utiliza Liquibase para gestionar las migraciones de base de datos. Cada vez que se aplica un archivo JDL, el comportamiento es el siguiente:

1. Se generan nuevos changesets para las entidades nuevas.
2. Los changesets anteriores permanecen sin modificaciones.
3. Los datos existentes se preservan.

La estructura de los archivos de migración se encuentra en:

```
distribucion/src/main/resources/config/liquibase/changelog/
├── 00000000000000_initial_schema.xml
├── YYYYMMDDHHMMSS_added_entity_Cliente.xml      # HU-1
├── YYYYMMDDHHMMSS_added_entity_Producto.xml     # HU-1
├── YYYYMMDDHHMMSS_added_entity_Pedido.xml       # HU-2
├── YYYYMMDDHHMMSS_added_entity_Empaque.xml      # HU-3
└── ...
```

## Tecnologías utilizadas

El sistema se desarrolló con las siguientes tecnologías:

- Spring Boot 3 y Java 21 para el backend
- React 18 para el frontend
- PostgreSQL como base de datos de producción y H2 para desarrollo
- JWT para autenticación
- JUnit para pruebas unitarias de backend
- Playwright con playwright-bdd para pruebas E2E
- Gherkin/Cucumber para especificaciones BDD
- Maven y npm como herramientas de construcción
- Docker para contenedores

