import { createUser } from '../../src/db.mjs';
import { createToken, hashPassword, randomUUID } from '../../src/security.mjs';
import { readJson, rateLimit, reply, requirePost } from '../../src/http.mjs';

export default async function handler(request, response) {
  const methodError = requirePost(request, response);
  if (methodError) return methodError;
  try {
    const body = await readJson(request);
    const email = String(body.email || '').trim().toLowerCase();
    const password = String(body.password || '');
    if (!rateLimit(request, `register:${email}`)) return reply(response, 429, { error: 'too many registration attempts' });
    if (!/^\S+@\S+\.\S+$/.test(email) || password.length < 12) return reply(response, 400, { error: 'invalid credentials' });
    const id = randomUUID();
    await createUser({ id, email, passwordHash: hashPassword(password) });
    return reply(response, 201, { token: createToken(id), user: { id, email } });
  } catch (error) {
    return reply(response, error?.code === '23505' ? 409 : 400, { error: 'registration failed' });
  }
}
