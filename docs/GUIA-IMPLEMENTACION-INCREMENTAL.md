# Guía de implementación incremental

Este documento describe el proceso de construcción del sistema de distribución de forma incremental mediante tres historias de usuario. Cada historia cuenta con su modelo JDL, especificaciones Gherkin y pruebas E2E con Playwright.

## Framework de pruebas E2E

El proyecto utiliza Playwright como framework de pruebas E2E por las siguientes razones:

1. Soporte multi-navegador nativo (Chrome, Firefox, Safari).
2. Ejecución paralela nativa.
3. Mejor rendimiento en comparación con otras alternativas.
4. Integración con Gherkin mediante `playwright-bdd`.
5. API basada en promesas nativas.
6. Manejo automático de storage state para autenticación JWT.

## Estructura del proyecto

El proyecto JHipster se configura con las siguientes características:

- Siete entidades generadas desde archivos JDL incrementales
- Playwright como framework de pruebas E2E
- H2 Disk como base de datos de desarrollo
- PostgreSQL como base de datos de producción
- React para el frontend
- Spring Boot con JWT para el backend

## División en historias de usuario

### Historia 1: Gestión de catálogos base

Esta historia corresponde al archivo JDL `jdl/01-hu1-catalogos.jdl` y comprende las siguientes entidades: Cliente, Producto, CanalComercializacion y Transporte.

La especificación BDD se encuentra en `features/HU-1-gestion-catalogos.feature`. El valor de negocio de esta historia radica en establecer los catálogos que serán referenciados por los pedidos. No presenta dependencias con otras historias.

### Historia 2: Gestión de pedidos

Esta historia corresponde al archivo JDL `jdl/02-hu2-pedidos.jdl` e introduce la entidad Pedido con sus relaciones hacia las entidades de la HU-1.

La especificación BDD se encuentra en `features/HU-2-gestion-pedidos.feature`. El valor de negocio consiste en permitir la creación y gestión de pedidos completos asociando clientes, productos, canales y transportes. Requiere que la HU-1 se encuentre completada.

### Historia 3: Gestión de tareas logísticas

Esta historia corresponde al archivo JDL `jdl/03-hu3-logistica.jdl` e introduce las entidades Empaque y Separacion.

La especificación BDD se encuentra en `features/HU-3-gestion-tareas-logisticas.feature`. El valor de negocio consiste en gestionar el proceso de preparación de productos para entrega mediante tareas de empaque y separación. Requiere que la HU-2 se encuentre completada.

## Flujo de implementación

### Fase 0: Configuración inicial del proyecto

#### Paso 0.1: Generación del proyecto base

Desde el directorio `distribucion/`:

```bash
cd /evergreen/distribucion
jhipster jdl ../jdl/00-aplicacion.jdl
```

El archivo `00-aplicacion.jdl` configura la aplicación sin Cypress:

```jdl
application {
  config {
    baseName distribucion
    applicationType monolith
    packageName com.evergreen.distribucion
    authenticationType jwt
    prodDatabaseType postgresql
    devDatabaseType h2Disk
    clientFramework react
    testFrameworks []
    enableTranslation true
    nativeLanguage es
    languages [es, en]
  }
}
```

#### Paso 0.2: Instalación de Playwright

```bash
npm install --save-dev @playwright/test playwright-bdd @cucumber/cucumber
npx playwright install chromium
```

#### Paso 0.3: Configuración de Playwright

Se crea el archivo `e2e/playwright.config.ts` según la documentación de configuración.

#### Paso 0.4: Agregar scripts al package.json

```json
{
  "scripts": {
    "e2e": "playwright test",
    "e2e:headed": "playwright test --headed",
    "e2e:debug": "playwright test --debug",
    "e2e:report": "playwright show-report e2e/reports/html",
    "e2e:ui": "playwright test --ui"
  }
}
```

#### Paso 0.5: Crear estructura de directorios

Desde el directorio `distribucion/`:

```bash
mkdir -p e2e/steps e2e/support e2e/fixtures e2e/reports e2e/.auth
```

#### Paso 0.6: Configurar helpers de autenticación

Se crea el archivo `e2e/support/auth.ts` con los helpers de autenticación JWT.

#### Paso 0.7: Crear archivo de setup de autenticación

Se crea el archivo `e2e/auth.setup.ts` para el login automático antes de las pruebas.

#### Paso 0.8: Registro del commit inicial

```bash
git add .
git commit -m "chore: configuración inicial con Playwright

- Proyecto JHipster base configurado
- Playwright instalado y configurado
- Estructura de pruebas E2E creada
- Helpers de autenticación implementados"
```

### Fase 1: Implementación de HU-1 (Catálogos)

#### Paso 1.1: Generación de entidades

Desde el directorio `distribucion/`:

```bash
jhipster jdl ../jdl/01-hu1-catalogos.jdl --skip-install

# Restaurar configuración de Playwright (si fue sobrescrita)
node scripts/setup-e2e.js
```

Este comando genera los siguientes artefactos:

- Cuatro entidades Java con sus repositorios
- Cuatro servicios con DTOs
- Cuatro controladores REST
- Componentes React para operaciones CRUD
- Pruebas unitarias y de integración
- Changelogs de Liquibase

#### Paso 1.2: Compilación y arranque

```bash
npm install
./mvnw clean package -DskipTests
./mvnw
```

#### Paso 1.3: Verificación manual

Se accede a `http://localhost:8080` y se realizan las siguientes verificaciones:

- Inicio de sesión con las credenciales admin/admin
- Confirmación de que el menú "Entidades" contiene: Clientes, Productos, Canales y Transportes
- Creación de registros de prueba en cada entidad

#### Paso 1.4: Creación de step definitions

Se crea el archivo `e2e/steps/catalogos.steps.ts`:

```typescript
import { createBdd } from 'playwright-bdd';
import { test, expect } from '@playwright/test';

const { Given, When, Then } = createBdd(test);

Given('que el usuario está autenticado en el sistema', async ({ page }) => {
  // La autenticación se maneja mediante storage state
  await page.goto('/');
  await expect(page.locator('[data-cy="entity-menu"]')).toBeVisible();
});

Given('el usuario navega a la sección {string}', async ({ page }, seccion: string) => {
  await page.click('[data-cy="entity-menu"]');
  await page.click(`[data-cy="${seccion.toLowerCase()}"]`);
});

When('el usuario hace clic en {string}', async ({ page }, boton: string) => {
  await page.click(`button:has-text("${boton}")`);
});

Then('el sistema muestra el formulario de creación', async ({ page }) => {
  await expect(page.locator('form')).toBeVisible();
});

Then('el registro aparece en la lista', async ({ page }) => {
  await expect(page.locator('table tbody tr')).toHaveCount({ minimum: 1 });
});
```

#### Paso 1.5: Ejecución de pruebas

```bash
# En una terminal: arrancar aplicación
./mvnw

# En otra terminal: ejecutar pruebas
npm run e2e -- --grep "@HU-1"
```

Alternativamente, con el webServer configurado:

```bash
npm run e2e -- --grep "@HU-1"
```

#### Paso 1.6: Registro del commit

```bash
git add .
git commit -m "feat(HU-1): Implementar gestión de catálogos base

- Entidades: Cliente, Producto, CanalComercializacion, Transporte
- Pruebas E2E con Playwright y Gherkin para CRUD completo
- Validaciones de negocio implementadas"
```

### Fase 2: Implementación de HU-2 (Pedidos)

#### Paso 2.1: Generación de la entidad Pedido

Desde el directorio `distribucion/`:

```bash
jhipster jdl ../jdl/02-hu2-pedidos.jdl --skip-install
```

JHipster detecta que las entidades Cliente, Producto, CanalComercializacion y Transporte existen previamente. En consecuencia, solo genera o actualiza los archivos relacionados con Pedido y actualiza las relaciones en las entidades existentes.

#### Paso 2.2: Recompilación y verificación

```bash
npm install
./mvnw clean package -DskipTests
./mvnw
```

#### Paso 2.3: Verificación manual

Se realizan las siguientes comprobaciones:

- Creación de un pedido desde la interfaz de usuario
- Verificación de que se pueden asociar clientes, productos, canales y transportes existentes
- Confirmación del funcionamiento de las relaciones

#### Paso 2.4: Implementación de step definitions

Se crea el archivo `e2e/steps/pedidos.steps.ts`:

```typescript
import { createBdd } from 'playwright-bdd';
import { test, expect } from '@playwright/test';

const { Given, When, Then } = createBdd(test);

Given('existen clientes y productos en el sistema', async ({ page }) => {
  // Verificar que existen datos de catálogos
  await page.goto('/cliente');
  await expect(page.locator('table tbody tr')).toHaveCount({ minimum: 1 });
});

When('el usuario crea un pedido con cliente {string}', async ({ page }, cliente: string) => {
  await page.goto('/pedido/new');
  await page.selectOption('[data-cy="cliente"]', { label: cliente });
});

When('selecciona el producto {string}', async ({ page }, producto: string) => {
  await page.selectOption('[data-cy="producto"]', { label: producto });
});

When('guarda el pedido', async ({ page }) => {
  await page.click('[data-cy="submit"]');
});

Then('el pedido se crea con estado {string}', async ({ page }, estado: string) => {
  await expect(page.locator(`text=${estado}`)).toBeVisible();
});
```

#### Paso 2.5: Ejecución de pruebas de regresión

```bash
# Ejecutar pruebas de HU-1 y HU-2
npm run e2e -- --grep "@HU-1|@HU-2"
```

#### Paso 2.6: Registro del commit

```bash
git add .
git commit -m "feat(HU-2): Implementar gestión de pedidos

- Entidad Pedido con relaciones a catálogos
- Pruebas E2E con Playwright y Gherkin para creación y edición de pedidos
- Validaciones de integridad referencial"
```

### Fase 3: Implementación de HU-3 (Tareas logísticas)

#### Paso 3.1: Generación de entidades

Desde el directorio `distribucion/`:

```bash
jhipster jdl ../jdl/03-hu3-logistica.jdl --skip-install
```

#### Paso 3.2: Recompilación y verificación

```bash
npm install
./mvnw clean package -DskipTests
./mvnw
```

#### Paso 3.3: Implementación de step definitions

Se crean los archivos en `e2e/steps/`:

- `empaque.steps.ts`
- `separacion.steps.ts`

#### Paso 3.4: Ejecución de la suite completa

```bash
# Ejecutar suite completa de regresión
npm run e2e
```

#### Paso 3.5: Registro del commit

```bash
git add .
git commit -m "feat(HU-3): Implementar gestión de tareas logísticas

- Entidades: Empaque, Separacion
- Pruebas E2E con Playwright y Gherkin para tareas asociadas a pedidos
- Modelo completo de distribución funcional"
```

## Ventajas del enfoque incremental

El enfoque incremental adoptado presenta las siguientes ventajas:

1. Validación temprana: cada historia entrega valor funcional completo.
2. Reducción de riesgo: los problemas se detectan en fases tempranas.
3. Facilidad de pruebas: las pruebas resultan más pequeñas y enfocadas por historia.
4. Colaboración en equipo: diferentes desarrolladores pueden trabajar en paralelo.
5. Demostración continua: el progreso se puede mostrar al cliente en cada fase.

## Comandos útiles durante el desarrollo

### Backend

```bash
# Compilar solo backend
./mvnw compile

# Ejecutar pruebas unitarias
./mvnw test

# Ver logs de Liquibase
./mvnw liquibase:status
```

### Frontend

```bash
# Compilar solo frontend
npm run webapp:build

# Pruebas unitarias React
npm test

# Linter
npm run lint:fix
```

### Pruebas E2E con Playwright

```bash
# Ejecutar todas las pruebas
npm run e2e

# Ejecutar con interfaz visual
npm run e2e:headed

# Modo debug con inspector
npm run e2e:debug

# Interfaz de usuario de Playwright
npm run e2e:ui

# Generar código con codegen
npm run e2e:codegen

# Ver reporte HTML
npm run e2e:report

# Ejecutar por tag
npm run e2e -- --grep "@HU-1"

# Ejecutar en navegador específico
npm run e2e -- --project=firefox
```

### Base de datos

```bash
# Acceder a consola H2 (desarrollo)
# URL: http://localhost:8080/h2-console
# JDBC URL: jdbc:h2:file:./target/h2db/db/distribucion
# User: distribucion
# Password: (vacío)

# Levantar PostgreSQL con Docker (producción)
npm run docker:db:up
```

## Lista de verificación por historia

Para cada historia de usuario se recomienda verificar los siguientes puntos:

- Archivo JDL creado y revisado
- Entidades generadas con `jhipster jdl`
- Aplicación compilada sin errores
- Aplicación arranca correctamente
- Pruebas unitarias de backend superadas
- Pruebas unitarias de frontend superadas
- Step definitions implementados en `e2e/steps/`
- Pruebas E2E con Playwright superadas
- Verificación manual exitosa
- Commit realizado con mensaje descriptivo
- Tag de versión creado (opcional): `git tag v1.0.0-HU-X`

## Solución de problemas comunes

### Error: "Liquibase changelog conflicts"

Este error ocurre al intentar regenerar entidades que tienen changelogs diferentes.

Solución:

```bash
# Opción 1: Eliminar base de datos H2
rm -rf target/h2db/

# Opción 2: Usar flag --force (con precaución)
jhipster jdl historia-X.jdl --force
```

### Error: "Port 8080 already in use"

Solución:

```bash
# Encontrar proceso usando el puerto
lsof -ti:8080

# Terminar proceso
kill -9 $(lsof -ti:8080)
```

### Error: Pruebas de Playwright con timeout

Este error suele ocurrir cuando la aplicación no está lista al iniciar las pruebas.

Solución: Configurar el `webServer` en `playwright.config.ts` o usar wait-on:

```bash
npx wait-on http://localhost:8080/management/health && npm run e2e
```

### Error: "Browser not found"

Solución:

```bash
npx playwright install chromium
```

### Error: "401 Unauthorized" en pruebas

Solución: Verificar que el archivo de setup de autenticación se ejecuta correctamente y que el storage state se guarda en `e2e/.auth/admin.json`.

## Referencias

- JHipster Official Documentation: https://www.jhipster.tech/
- Playwright Documentation: https://playwright.dev/
- playwright-bdd: https://github.com/vitalets/playwright-bdd
- Cucumber Documentation: https://cucumber.io/docs/cucumber/
- JHipster Testing Guide: https://www.jhipster.tech/running-tests/

