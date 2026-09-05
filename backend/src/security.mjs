import { createHmac, randomBytes, randomUUID, scryptSync, timingSafeEqual } from 'node:crypto';

const secret = process.env.JWT_SECRET;
if (!secret || secret.length < 32) throw new Error('JWT_SECRET must contain at least 32 characters');

const encode = value => Buffer.from(value).toString('base64url');
const signature = value => createHmac('sha256', secret).update(value).digest('base64url');
const equalSignatures = (left, right) => left.length === right.length && timingSafeEqual(Buffer.from(left), Buffer.from(right));

export function createToken(userId) {
  const header = encode(JSON.stringify({ alg: 'HS256', typ: 'JWT' }));
  const payload = encode(JSON.stringify({ sub: userId, exp: Math.floor(Date.now() / 1000) + 3600 }));
  return `${header}.${payload}.${signature(`${header}.${payload}`)}`;
}

export function userIdFromRequest(request) {
  try {
    const [scheme, token] = String(request.headers.authorization || '').split(' ');
    if (scheme !== 'Bearer' || !token) return null;
    const [header, payload, provided] = token.split('.');
    if (!header || !payload || !provided) return null;
    const claims = JSON.parse(Buffer.from(payload, 'base64url').toString());
    if (claims.alg && claims.alg !== 'HS256') return null;
    const expected = signature(`${header}.${payload}`);
    if (!equalSignatures(provided, expected) || typeof claims.sub !== 'string' || !Number.isFinite(claims.exp)) return null;
    return claims.exp > Math.floor(Date.now() / 1000) ? claims.sub : null;
  } catch {
    return null;
  }
}

export function hashPassword(password) {
  const salt = randomBytes(16);
  return `${salt.toString('hex')}:${scryptSync(password, salt, 64).toString('hex')}`;
}

export function verifyPassword(password, stored) {
  try {
    const [salt, hash] = String(stored).split(':');
    if (!salt || !hash) return false;
    const actual = scryptSync(password, Buffer.from(salt, 'hex'), 64);
    const expected = Buffer.from(hash, 'hex');
    return actual.length === expected.length && timingSafeEqual(actual, expected);
  } catch {
    return false;
  }
}

export { randomUUID };
