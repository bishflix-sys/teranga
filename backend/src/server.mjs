import { createServer } from 'node:http';
import { randomBytes, randomUUID, scryptSync, timingSafeEqual, createHmac } from 'node:crypto';
import { getMichelinAccessToken } from './michelin.mjs';

const port = Number(process.env.PORT || 8080);
const jwtSecret = process.env.JWT_SECRET;
if (!jwtSecret || jwtSecret.length < 32) throw new Error('JWT_SECRET must contain at least 32 characters');
const corsOrigin = process.env.CORS_ORIGIN || 'https://app.teranga-moov.sn';
const users = new Map();
const idempotentPayments = new Map();
const tickets = new Map();
const vehicles = [];
const alerts = [];

const json = (response, status, body) => {
  response.writeHead(status, {
    'content-type': 'application/json; charset=utf-8',
    'cache-control': 'no-store',
    'x-content-type-options': 'nosniff',
    'x-frame-options': 'DENY',
    'referrer-policy': 'no-referrer',
    'access-control-allow-origin': corsOrigin,
    'access-control-allow-headers': 'content-type, authorization, idempotency-key',
    'access-control-allow-methods': 'GET, POST, OPTIONS'
  });
  response.end(JSON.stringify(body));
};

const readBody = async request => {
  let raw = '';
  for await (const chunk of request) {
    raw += chunk;
    if (raw.length > 1_000_000) throw new Error('payload too large');
  }
  return raw ? JSON.parse(raw) : {};
};

const base64url = value => Buffer.from(value).toString('base64url');
const sign = value => createHmac('sha256', jwtSecret).update(value).digest('base64url');
const tokenFor = user => {
  const header = base64url(JSON.stringify({ alg: 'HS256', typ: 'JWT' }));
  const payload = base64url(JSON.stringify({ sub: user.id, exp: Math.floor(Date.now() / 1000) + 3600 }));
  return `${header}.${payload}.${sign(`${header}.${payload}`)}`;
};

const authUser = request => {
  const value = request.headers.authorization || '';
  const [scheme, token] = value.split(' ');
  if (scheme !== 'Bearer' || !token) return null;
  const [header, payload, signature] = token.split('.');
  if (!header || !payload || !signature || !timingSafeEqual(Buffer.from(signature), Buffer.from(sign(`${header}.${payload}`)))) return null;
  const claims = JSON.parse(Buffer.from(payload, 'base64url').toString());
  return claims.exp > Math.floor(Date.now() / 1000) ? users.get(claims.sub) : null;
};

const hashPassword = password => {
  const salt = randomBytes(16);
  return `${salt.toString('hex')}:${scryptSync(password, salt, 64).toString('hex')}`;
};
const verifyPassword = (password, stored) => {
  const [saltHex, hashHex] = stored.split(':');
  const actual = scryptSync(password, Buffer.from(saltHex, 'hex'), 64);
  return timingSafeEqual(actual, Buffer.from(hashHex, 'hex'));
};
const validPhone = phone => /^(?:70|74|75|76|77|78)\d{7}$/.test(String(phone).replace(/\D/g, '').replace(/^221/, ''));
const validMethod = method => ['Wave', 'Orange Money', 'Free Money'].includes(method);

const route = async (request, response) => {
  if (request.method === 'OPTIONS') return json(response, 204, {});
  if (request.method === 'GET' && request.url === '/health') return json(response, 200, { status: 'ok' });
  try {
    if (request.method === 'POST' && request.url === '/auth/register') {
      const body = await readBody(request);
      if (typeof body.email !== 'string' || !/^\S+@\S+\.\S+$/.test(body.email) || typeof body.password !== 'string' || body.password.length < 12) return json(response, 400, { error: 'invalid credentials' });
      if ([...users.values()].some(user => user.email === body.email.toLowerCase())) return json(response, 409, { error: 'account already exists' });
      const user = { id: randomUUID(), email: body.email.toLowerCase(), passwordHash: hashPassword(body.password) };
      users.set(user.id, user);
      return json(response, 201, { token: tokenFor(user), user: { id: user.id, email: user.email } });
    }
    if (request.method === 'POST' && request.url === '/auth/login') {
      const body = await readBody(request);
      const user = [...users.values()].find(candidate => candidate.email === String(body.email).toLowerCase());
      if (!user || typeof body.password !== 'string' || !verifyPassword(body.password, user.passwordHash)) return json(response, 401, { error: 'invalid credentials' });
      return json(response, 200, { token: tokenFor(user), user: { id: user.id, email: user.email } });
    }
    if (request.method === 'GET' && request.url === '/vehicles') return json(response, 200, vehicles);
    if (request.method === 'GET' && request.url === '/alerts') return json(response, 200, alerts);

    const user = authUser(request);
    if (!user) return json(response, 401, { error: 'authentication required' });
    if (request.method === 'GET' && request.url === '/michelin/token') {
      try {
        return json(response, 200, { access_token: await getMichelinAccessToken(), token_type: 'Bearer' });
      } catch (error) {
        return json(response, 503, { error: 'Michelin provider not configured' });
      }
    }
    if (request.method === 'POST' && request.url === '/payments/charge') {
      const key = request.headers['idempotency-key'];
      const body = await readBody(request);
      if (typeof key !== 'string' || key.length < 16 || !validMethod(body.method) || !validPhone(body.phone) || !Number.isInteger(body.amountCfa) || body.amountCfa <= 0) return json(response, 400, { error: 'invalid payment request' });
      if (idempotentPayments.has(key)) return json(response, 200, idempotentPayments.get(key));
      if (process.env.PAYMENTS_MODE !== 'live') return json(response, 503, { error: 'payment provider not configured' });
      const result = { id: randomUUID(), status: 'pending', method: body.method, amountCfa: body.amountCfa };
      idempotentPayments.set(key, result);
      return json(response, 202, result);
    }
    if (request.method === 'POST' && request.url === '/tickets/verify') {
      const body = await readBody(request);
      const ticket = tickets.get(body.ticketId);
      if (!ticket || ticket.used || ticket.expiresAt < Date.now()) return json(response, 409, { valid: false });
      ticket.used = true;
      return json(response, 200, { valid: true, ticketId: ticket.id });
    }
    return json(response, 404, { error: 'not found' });
  } catch (error) {
    return json(response, 400, { error: 'invalid request' });
  }
};

createServer(route).listen(port, () => console.log(`Téranga Moov API listening on :${port}`));
