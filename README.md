# Evergreen - Sistema de Distribución

Sistema de gestión de distribución de productos construido con JHipster, usando un enfoque de desarrollo incremental basado en Historias de Usuario.

## Estructura del Proyecto

```
proyecto/
├── docker/                      # Configuración Docker para JHipster
│   ├── docker-compose.jhipster.yml
│   ├── Dockerfile.jhipster
│   └── jhipster-shell.sh
│
├── jdl/                         # Modelos JDL incrementales
│   ├── 00-aplicacion.jdl        # Configuración base
│   ├── 01-hu1-catalogos.jdl     # HU-1: Catálogos
│   ├── 02-hu2-pedidos.jdl       # HU-2: Pedidos
│   └── 03-hu3-logistica.jdl     # HU-3: Logística
│
├── features/                    # Especificaciones BDD (Gherkin)
│   ├── HU-1-gestion-catalogos.feature
│   ├── HU-2-gestion-pedidos.feature
│   └── HU-3-gestion-tareas-logisticas.feature
│
├── diagrama_clase_distribucion.mmd
└── README.md
```

## Historias de Usuario

| HU | Nombre | Entidades | Dependencias |
|----|--------|-----------|--------------|
| HU-1 | Gestión de Catálogos | Cliente, Producto, CanalComercializacion, Transporte | - |
| HU-2 | Gestión de Pedidos | Pedido | HU-1 |
| HU-3 | Tareas Logísticas | Empaque, Separacion | HU-2 |

## Flujo de Desarrollo Incremental

### Requisitos Previos

- Java 17+
- Node.js 18+
- Docker (opcional, para BD PostgreSQL)

### Sprint 1: HU-1 - Catálogos Base

```bash
# Crear proyecto y generar entidades de catálogos
mkdir evergreen && cd evergreen
jhipster jdl ../jdl/00-aplicacion.jdl ../jdl/01-hu1-catalogos.jdl

# Compilar y verificar
./mvnw

# Ejecutar tests
./mvnw verify

# Tag de versión
git add . && git commit -m "feat(HU-1): gestión de catálogos base"
git tag v0.1.0
```

### Sprint 2: HU-2 - Pedidos

```bash
cd evergreen

# Aplicar entidad Pedido sobre proyecto existente
jhipster jdl ../jdl/02-hu2-pedidos.jdl

# Compilar (genera nuevos changelogs Liquibase)
./mvnw

# Ejecutar tests
./mvnw verify

# Tag de versión
git add . && git commit -m "feat(HU-2): gestión de pedidos"
git tag v0.2.0
```

### Sprint 3: HU-3 - Logística

```bash
cd evergreen

# Aplicar entidades de logística
jhipster jdl ../jdl/03-hu3-logistica.jdl

# Compilar
./mvnw

# Ejecutar tests
./mvnw verify

# Tag de versión
git add . && git commit -m "feat(HU-3): tareas logísticas"
git tag v1.0.0
```

## Desarrollo con Docker

```bash
# Levantar entorno de desarrollo con JHipster
cd docker
docker-compose -f docker-compose.jhipster.yml up -d

# Acceder al shell de JHipster
./jhipster-shell.sh
```

## Testing BDD

Los archivos `.feature` en `features/` contienen las especificaciones en Gherkin.

Para ejecutar tests E2E con Cypress + Cucumber:

```bash
cd evergreen

# Instalar plugin Cucumber
npm install --save-dev @badeball/cypress-cucumber-preprocessor

# Copiar features
cp ../features/*.feature src/test/javascript/cypress/e2e/

# Ejecutar tests
npm run e2e:cypress
```

## Migraciones de Base de Datos

JHipster usa Liquibase para migraciones. Cada vez que se aplica un JDL:

1. Se generan nuevos changesets para las entidades nuevas
2. Los changesets anteriores NO se modifican
3. Los datos existentes se preservan

```
src/main/resources/config/liquibase/changelog/
├── 00000000000000_initial_schema.xml
├── YYYYMMDDHHMMSS_added_entity_Cliente.xml      # HU-1
├── YYYYMMDDHHMMSS_added_entity_Producto.xml     # HU-1
├── YYYYMMDDHHMMSS_added_entity_Pedido.xml       # HU-2
├── YYYYMMDDHHMMSS_added_entity_Empaque.xml      # HU-3
└── ...
```

## Tecnologías

- **Backend**: Spring Boot 3, Java 17
- **Frontend**: React 18
- **Base de datos**: PostgreSQL (prod), H2 (dev)
- **Autenticación**: JWT
- **Testing**: JUnit, Cypress, Gherkin/Cucumber
- **Build**: Maven, npm

