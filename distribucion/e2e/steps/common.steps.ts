import { createBdd } from 'playwright-bdd';
import { test, expect } from '@playwright/test';

const { Given, When, Then } = createBdd(test);

// Steps comunes de autenticación
Given('que el usuario está autenticado en el sistema', async ({ page }) => {
  await page.goto('/');
  await expect(page.locator('[data-cy="entity-menu"]')).toBeVisible();
});

Given('que el usuario tiene rol de administrador', async ({ page }) => {
  await page.goto('/');
  await expect(page.locator('[data-cy="adminMenu"]')).toBeVisible();
});

// Steps comunes de navegación
Given('el usuario navega a la sección {string}', async ({ page }, seccion: string) => {
  await page.click('[data-cy="entity-menu"]');
  await page.click(`[data-cy="${seccion.toLowerCase()}"]`);
});

When('el usuario accede al menú de entidades', async ({ page }) => {
  await page.click('[data-cy="entity-menu"]');
});

When('el usuario hace clic en {string}', async ({ page }, texto: string) => {
  await page.click(`text=${texto}`);
});

When('el usuario hace clic en el botón {string}', async ({ page }, boton: string) => {
  await page.click(`button:has-text("${boton}")`);
});

// Steps comunes de formularios
When('el usuario completa el campo {string} con {string}', async ({ page }, campo: string, valor: string) => {
  await page.fill(`[data-cy="${campo}"]`, valor);
});

When('el usuario selecciona {string} en el campo {string}', async ({ page }, valor: string, campo: string) => {
  await page.selectOption(`[data-cy="${campo}"]`, { label: valor });
});

When('el usuario guarda el formulario', async ({ page }) => {
  await page.click('[data-cy="entityCreateSaveButton"]');
});

// Steps comunes de verificación
Then('el sistema muestra el formulario de creación', async ({ page }) => {
  await expect(page.locator('form')).toBeVisible();
});

Then('el sistema muestra un mensaje de éxito', async ({ page }) => {
  await expect(page.locator('.alert-success')).toBeVisible();
});

Then('el sistema muestra un mensaje de error', async ({ page }) => {
  await expect(page.locator('.alert-danger')).toBeVisible();
});

Then('el registro aparece en la lista', async ({ page }) => {
  await expect(page.locator('table tbody tr')).toHaveCount({ minimum: 1 });
});

Then('la lista está vacía', async ({ page }) => {
  await expect(page.locator('table tbody tr')).toHaveCount(0);
});

Then('el sistema muestra {int} registros en la lista', async ({ page }, cantidad: number) => {
  await expect(page.locator('table tbody tr')).toHaveCount(cantidad);
});

