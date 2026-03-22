#!/usr/bin/env node
/**
 * Script para agregar/restaurar los scripts de Playwright al package.json
 * Ejecutar después de regenerar con JHipster: node scripts/setup-e2e.js
 */

const fs = require('fs');
const path = require('path');

const packageJsonPath = path.join(__dirname, '..', 'package.json');

const e2eScripts = {
  'e2e': 'playwright test --config=e2e/playwright.config.ts',
  'e2e:headed': 'playwright test --config=e2e/playwright.config.ts --headed',
  'e2e:debug': 'playwright test --config=e2e/playwright.config.ts --debug',
  'e2e:ui': 'playwright test --config=e2e/playwright.config.ts --ui',
  'e2e:report': 'playwright show-report e2e/reports/html',
  'e2e:codegen': 'playwright codegen http://localhost:8080',
};

const e2eDevDependencies = {
  '@playwright/test': '^1.58.2',
  '@cucumber/cucumber': '^12.7.0',
  'playwright-bdd': '^8.5.0',
};

try {
  const packageJson = JSON.parse(fs.readFileSync(packageJsonPath, 'utf8'));

  // Agregar scripts
  let scriptsAdded = 0;
  for (const [key, value] of Object.entries(e2eScripts)) {
    if (!packageJson.scripts[key]) {
      packageJson.scripts[key] = value;
      scriptsAdded++;
    }
  }

  // Agregar dependencias
  let depsAdded = 0;
  for (const [key, value] of Object.entries(e2eDevDependencies)) {
    if (!packageJson.devDependencies[key]) {
      packageJson.devDependencies[key] = value;
      depsAdded++;
    }
  }

  fs.writeFileSync(packageJsonPath, JSON.stringify(packageJson, null, 2) + '\n');

  console.log(`✅ Setup E2E completado:`);
  console.log(`   - ${scriptsAdded} scripts agregados`);
  console.log(`   - ${depsAdded} dependencias agregadas`);

  if (depsAdded > 0) {
    console.log(`\n⚠️  Ejecuta 'npm install' para instalar las nuevas dependencias`);
  }
} catch (error) {
  console.error('❌ Error:', error.message);
  process.exit(1);
}

