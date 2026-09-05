const attempts = new Map();

export async function readJson(request) {
  let body = '';
  for await (const chunk of request) {
    body += chunk;
    if (body.length > 100_000) throw new Error('payload too large');
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
  response.setHeader('x-xss-protection', '0');
  response.setHeader('referrer-policy', 'no-referrer');
  response.setHeader('permissions-policy', 'camera=(), microphone=(), geolocation=()');
  response.setHeader('strict-transport-security', 'max-age=31536000; includeSubDomains');
  response.setHeader('vary', 'Origin');
  return response.status(status).json(body);
}

export function rateLimit(request, key, limit = 5, windowMs = 900_000) {
  const now = Date.now();
  const address = request.headers['x-forwarded-for']?.split(',')[0]?.trim() || request.socket?.remoteAddress || 'unknown';
  const bucketKey = `${key}:${address}`;
  const bucket = attempts.get(bucketKey) || { count: 0, resetAt: now + windowMs };
  if (bucket.resetAt <= now) { bucket.count = 0; bucket.resetAt = now + windowMs; }
  bucket.count += 1;
  attempts.set(bucketKey, bucket);
  if (attempts.size > 10_000) for (const [storedKey, stored] of attempts) if (stored.resetAt <= now) attempts.delete(storedKey);
  return bucket.count <= limit;
}

export function requirePost(request, response) {
  if (request.method === 'OPTIONS') return reply(response, 204, {});
  if (request.method !== 'POST') return reply(response, 405, { error: 'method not allowed' }, 'POST, OPTIONS');
}
