# Configuración de Playwright para pruebas E2E

## Contexto

Este documento describe la configuración de Playwright como framework de pruebas E2E para el proyecto JHipster Evergreen. El proyecto se genera sin el framework de pruebas E2E por defecto y se configura Playwright desde el inicio.

## Ventajas de Playwright

Playwright ofrece las siguientes características para proyectos JHipster:

- Soporte multi-navegador nativo para Chrome, Firefox y Safari (WebKit).
- Mejor manejo de contextos y paralelización, lo que resulta en pruebas más rápidas.
- Integración nativa con Cucumber/Gherkin mediante el paquete `playwright-bdd`.
- API basada en promesas nativas.
- Mejor soporte para escenarios complejos como autenticación, múltiples pestañas e interceptación de red.
- Storage state automático para manejo de autenticación JWT.

## Generación del proyecto JHipster sin Cypress

Al generar el proyecto JHipster, se omite Cypress para configurar Playwright desde el inicio:

```bash
jhipster jdl jdl/00-aplicacion.jdl --skip-client
# O durante la generación interactiva, seleccionar "No" en la opción de pruebas E2E
```

Alternativamente, en el archivo `.yo-rc.json` se puede establecer:

```json
{
  "generator-jhipster": {
    "cypressTests": false
  }
}
```

## Instalación de Playwright

### Paso 1: Instalación de dependencias

```bash
npm install --save-dev @playwright/test playwright-bdd @cucumber/cucumber
```

### Paso 2: Instalación de navegadores

```bash
npx playwright install chromium
```

Este comando descarga Chromium, el navegador utilizado para las pruebas E2E.

## Estructura de directorios

```
evergreen/
├── features/                           # Archivos .feature (Gherkin)
│   ├── HU-1-gestion-catalogos.feature
│   ├── HU-2-gestion-pedidos.feature
│   └── HU-3-gestion-tareas-logisticas.feature
│
└── distribucion/                       # Proyecto JHipster
    ├── e2e/                            # Pruebas E2E con Playwright
    │   ├── playwright.config.ts        # Configuración de Playwright
    │   ├── auth.setup.ts               # Setup de autenticación
    │   ├── support/
    │   │   ├── fixtures.ts             # Fixtures compartidos
    │   │   ├── hooks.ts                # Hooks de Cucumber (Before/After)
    │   │   └── auth.ts                 # Helpers de autenticación
    │   ├── steps/
    │   │   ├── common.steps.ts         # Steps comunes
    │   │   ├── cliente.steps.ts        # Steps de Cliente
    │   │   ├── producto.steps.ts       # Steps de Producto
    │   │   ├── canal.steps.ts          # Steps de CanalComercializacion
    │   │   ├── transporte.steps.ts     # Steps de Transporte
    │   │   ├── pedido.steps.ts         # Steps de Pedido
    │   │   ├── empaque.steps.ts        # Steps de Empaque
    │   │   └── separacion.steps.ts     # Steps de Separacion
    │   ├── fixtures/
    │   │   └── test-data.json          # Datos de prueba
    │   ├── reports/                    # Reportes de ejecución
    │   └── .auth/                      # Storage state de autenticación
    │       └── admin.json
    └── package.json
```

## Configuración de Playwright con Cucumber

### Archivo playwright.config.ts

```typescript
import { defineConfig, devices } from '@playwright/test';
import { defineBddConfig } from 'playwright-bdd';

const testDir = defineBddConfig({
  features: '../../features/**/*.feature',
  steps: './steps/**/*.steps.ts',
});

export default defineConfig({
  testDir,
  fullyParallel: true,
  forbidOnly: !!process.env.CI,
  retries: process.env.CI ? 2 : 0,
  workers: process.env.CI ? 1 : undefined,
  reporter: [
    ['html', { outputFolder: 'e2e/reports/html' }],
    ['json', { outputFile: 'e2e/reports/results.json' }],
  ],
  use: {
    baseURL: 'http://localhost:8080',
    trace: 'on-first-retry',
    screenshot: 'only-on-failure',
    actionTimeout: 30000,
    navigationTimeout: 60000,
  },
  projects: [
    {
      name: 'chromium',
      use: { ...devices['Desktop Chrome'] },
    },
    {
      name: 'firefox',
      use: { ...devices['Desktop Firefox'] },
    },
    {
      name: 'webkit',
      use: { ...devices['Desktop Safari'] },
    },
  ],
  webServer: {
    command: 'npm run app:start',
    url: 'http://localhost:8080/management/health',
    reuseExistingServer: !process.env.CI,
    timeout: 120000,
  },
});
```

## Integración con scripts del proyecto

Se agregan los siguientes scripts al archivo `package.json`:

```json
{
  "scripts": {
    "e2e": "playwright test",
    "e2e:headed": "playwright test --headed",
    "e2e:debug": "playwright test --debug",
    "e2e:report": "playwright show-report e2e/reports/html",
    "e2e:ui": "playwright test --ui",
    "e2e:codegen": "playwright codegen http://localhost:8080"
  }
}
```

## Manejo de autenticación JWT

JHipster utiliza JWT para autenticación. La estrategia recomendada es:

1. Realizar el login mediante API (más rápido que mediante la interfaz).
2. Guardar el storage state con el token JWT.
3. Reutilizar el estado entre pruebas.

### Archivo e2e/support/auth.ts

```typescript
import { Page, BrowserContext } from '@playwright/test';

export interface AuthCredentials {
  username: string;
  password: string;
}

export async function login(page: Page, credentials: AuthCredentials): Promise<string> {
  const response = await page.request.post('/api/authenticate', {
    data: {
      username: credentials.username,
      password: credentials.password,
      rememberMe: false,
    },
  });

  const body = await response.json();
  return body.id_token;
}

export async function loginAndSaveState(
  page: Page,
  context: BrowserContext,
  credentials: AuthCredentials,
  storagePath: string
): Promise<void> {
  const token = await login(page, credentials);

  await context.addCookies([]);
  await page.evaluate((jwt) => {
    localStorage.setItem('jhi-authenticationToken', JSON.stringify(jwt));
  }, token);

  await context.storageState({ path: storagePath });
}

export const adminCredentials: AuthCredentials = {
  username: 'admin',
  password: 'admin',
};

export const userCredentials: AuthCredentials = {
  username: 'user',
  password: 'user',
};
```

### Configuración de proyecto autenticado

```typescript
// En playwright.config.ts
projects: [
  {
    name: 'setup',
    testMatch: /.*\.setup\.ts/,
  },
  {
    name: 'chromium',
    use: {
      ...devices['Desktop Chrome'],
      storageState: 'e2e/.auth/admin.json',
    },
    dependencies: ['setup'],
  },
],
```

### Archivo de setup para autenticación

```typescript
// e2e/auth.setup.ts
import { test as setup } from '@playwright/test';
import { loginAndSaveState, adminCredentials } from './support/auth';

const authFile = 'e2e/.auth/admin.json';

setup('authenticate as admin', async ({ page, context }) => {
  await page.goto('/');
  await loginAndSaveState(page, context, adminCredentials, authFile);
});
```

## Ejecución de pruebas por historia de usuario

### Historia 1 (Catálogos)

```bash
# Ejecutar solo HU-1
npx playwright test --grep "@HU-1"

# Ejecutar escenarios de Cliente
npx playwright test --grep "@Cliente"
```

### Historia 2 (Pedidos)

```bash
npx playwright test --grep "@HU-2"
```

### Historia 3 (Tareas logísticas)

```bash
npx playwright test --grep "@HU-3"
```

## Ejecución durante el desarrollo

### Desarrollo de HU-1 (Primera iteración)

1. Generar entidades con `jhipster jdl jdl/01-hu1-catalogos.jdl`.
2. Levantar la aplicación: `npm run app:start`.
3. Ejecutar pruebas de HU-1: `npm run e2e -- --grep "@HU-1"`.

### Desarrollo de HU-2 (Segunda iteración)

1. Generar nuevas entidades: `jhipster jdl jdl/02-hu2-pedidos.jdl`.
2. Levantar la aplicación: `npm run app:start`.
3. Ejecutar pruebas de HU-1 y HU-2: `npm run e2e -- --grep "@HU-1|@HU-2"`.

### Desarrollo de HU-3 (Tercera iteración)

1. Generar entidades finales: `jhipster jdl jdl/03-hu3-logistica.jdl`.
2. Levantar la aplicación: `npm run app:start`.
3. Ejecutar todas las pruebas: `npm run e2e`.

## Solución de problemas comunes

### Error: "Timeout waiting for network idle"

Este error ocurre debido a que JHipster carga múltiples recursos (JavaScript, CSS, fuentes).

Solución: Aumentar el timeout en `playwright.config.ts`:

```typescript
use: {
  actionTimeout: 30000,
  navigationTimeout: 60000,
}
```

### Error: "401 Unauthorized" en pruebas

Este error indica que el token JWT expiró o no está presente.

Solución: Verificar que el archivo de setup de autenticación se ejecuta antes de las pruebas.

### Pruebas lentas en primera ejecución

Este comportamiento se debe a que JHipster necesita tiempo para iniciar (compilación y arranque de base de datos).

Solución: Configurar `webServer` en `playwright.config.ts` con timeout adecuado o utilizar `wait-on`:

```bash
npx wait-on http://localhost:8080/management/health && npm run e2e
```

### Error: "Browser not found"

Solución: Ejecutar la instalación de Chromium:

```bash
npx playwright install chromium
```

## Referencias

- Playwright Documentation: https://playwright.dev/
- playwright-bdd: https://github.com/vitalets/playwright-bdd
- Cucumber.js Documentation: https://github.com/cucumber/cucumber-js
- JHipster Testing Guide: https://www.jhipster.tech/running-tests/

