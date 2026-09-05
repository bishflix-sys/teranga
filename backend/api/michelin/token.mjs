import { getMichelinAccessToken } from '../../src/michelin.mjs';
import { userIdFromRequest } from '../../src/security.mjs';
import { reply } from '../../src/http.mjs';

export default async function handler(request, response) {
  if (request.method === 'OPTIONS') return reply(response, 204, {});
  if (request.method !== 'GET') return reply(response, 405, { error: 'method not allowed' }, 'GET, OPTIONS');
  if (!userIdFromRequest(request)) return reply(response, 401, { error: 'authentication required' });
  try {
    return reply(response, 200, { access_token: await getMichelinAccessToken(), token_type: 'Bearer' });
  } catch {
    return reply(response, 503, { error: 'Michelin provider not configured' });
  }
}
