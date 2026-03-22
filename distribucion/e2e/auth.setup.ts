import { test as setup } from '@playwright/test';
import { loginAndSaveState, adminCredentials } from './support/auth';

const authFile = 'e2e/.auth/admin.json';

setup('authenticate as admin', async ({ page, context }) => {
  await page.goto('/');
  await loginAndSaveState(page, context, adminCredentials, authFile);
});

