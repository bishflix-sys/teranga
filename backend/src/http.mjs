export async function readJson(request) {
  let body = '';
  for await (const chunk of request) {
    body += chunk;
    if (body.length > 1_000_000) throw new Error('payload too large');
  }
  return body ? JSON.parse(body) : {};
}

export function reply(response, status, body, allow = 'GET, POST, OPTIONS') {
  response.setHeader('access-control-allow-origin', process.env.CORS_ORIGIN || 'https://app.teranga-moov.sn');
  response.setHeader('access-control-allow-headers', 'content-type, authorization, idempotency-key');
  response.setHeader('access-control-allow-methods', allow);
  response.setHeader('cache-control', 'no-store');
  response.setHeader('x-content-type-options', 'nosniff');
  response.setHeader('x-frame-options', 'DENY');
  return response.status(status).json(body);
}

export function requirePost(request, response) {
  if (request.method === 'OPTIONS') return reply(response, 204, {});
  if (request.method !== 'POST') return reply(response, 405, { error: 'method not allowed' }, 'POST, OPTIONS');
}
