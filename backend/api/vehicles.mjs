import { listVehicles } from '../src/db.mjs';
import { reply } from '../src/http.mjs';

export default async function handler(request, response) {
  if (request.method !== 'GET') return reply(response, 405, { error: 'method not allowed' }, 'GET, OPTIONS');
  try { return reply(response, 200, await listVehicles()); }
  catch { return reply(response, 503, { error: 'vehicles unavailable' }); }
}
