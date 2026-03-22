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

