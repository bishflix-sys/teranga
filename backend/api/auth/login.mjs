import { findUserByEmail } from '../../src/db.mjs';
import { createToken, verifyPassword } from '../../src/security.mjs';
import { readJson, rateLimit, reply, requirePost } from '../../src/http.mjs';

export default async function handler(request, response) {
  const methodError = requirePost(request, response);
  if (methodError) return methodError;
  try {
    const body = await readJson(request);
    const email = String(body.email || '').trim().toLowerCase();
    if (!rateLimit(request, `login:${email}`)) return reply(response, 429, { error: 'too many login attempts' });
    const user = await findUserByEmail(email);
    if (!user || !verifyPassword(String(body.password || ''), user.password_hash)) return reply(response, 401, { error: 'invalid credentials' });
    return reply(response, 200, { token: createToken(user.id), user: { id: user.id, email: user.email } });
  } catch {
    return reply(response, 401, { error: 'invalid credentials' });
  }
}
