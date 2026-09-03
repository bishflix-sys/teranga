import { checkDatabase } from '../src/db.mjs';

export default async function handler(request, response) {
  if (request.method !== 'GET') {
    response.setHeader('Allow', 'GET');
    return response.status(405).json({ error: 'method not allowed' });
  }

  try {
    const databaseReady = await checkDatabase();
    return response.status(200).json({ status: 'ok', database: databaseReady ? 'ready' : 'unavailable' });
  } catch {
    return response.status(503).json({ status: 'degraded', database: 'unavailable' });
  }
}
