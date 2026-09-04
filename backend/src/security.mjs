import { createHmac, randomBytes, randomUUID, scryptSync, timingSafeEqual } from 'node:crypto';

const secret = process.env.JWT_SECRET;
if (!secret || secret.length < 32) throw new Error('JWT_SECRET must contain at least 32 characters');

const encode = value => Buffer.from(value).toString('base64url');
const signature = value => createHmac('sha256', secret).update(value).digest('base64url');

export function createToken(userId) {
  const header = encode(JSON.stringify({ alg: 'HS256', typ: 'JWT' }));
  const payload = encode(JSON.stringify({ sub: userId, exp: Math.floor(Date.now() / 1000) + 3600 }));
  return `${header}.${payload}.${signature(`${header}.${payload}`)}`;
}

export function userIdFromRequest(request) {
  const [scheme, token] = String(request.headers.authorization || '').split(' ');
  if (scheme !== 'Bearer' || !token) return null;
  const [header, payload, provided] = token.split('.');
  if (!header || !payload || !provided) return null;
  const expected = signature(`${header}.${payload}`);
  if (provided.length !== expected.length || !timingSafeEqual(Buffer.from(provided), Buffer.from(expected))) return null;
  const claims = JSON.parse(Buffer.from(payload, 'base64url').toString());
  return claims.exp > Math.floor(Date.now() / 1000) ? claims.sub : null;
}

export function hashPassword(password) {
  const salt = randomBytes(16);
  return `${salt.toString('hex')}:${scryptSync(password, salt, 64).toString('hex')}`;
}

export function verifyPassword(password, stored) {
  const [salt, hash] = stored.split(':');
  const actual = scryptSync(password, Buffer.from(salt, 'hex'), 64);
  const expected = Buffer.from(hash, 'hex');
  return actual.length === expected.length && timingSafeEqual(actual, expected);
}

export { randomUUID };
