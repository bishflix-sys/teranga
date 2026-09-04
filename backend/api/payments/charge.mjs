import { createPayment } from '../../src/db.mjs';
import { userIdFromRequest, randomUUID } from '../../src/security.mjs';
import { readJson, reply, requirePost } from '../../src/http.mjs';

const methods = new Set(['Wave', 'Orange Money', 'Free Money']);
const validPhone = value => /^(?:70|74|75|76|77|78)\d{7}$/.test(String(value).replace(/\D/g, '').replace(/^221/, ''));

export default async function handler(request, response) {
  const methodError = requirePost(request, response);
  if (methodError) return methodError;
  const userId = userIdFromRequest(request);
  if (!userId) return reply(response, 401, { error: 'authentication required' });
  try {
    const body = await readJson(request);
    const key = request.headers['idempotency-key'];
    if (typeof key !== 'string' || key.length < 16 || !methods.has(body.method) || !validPhone(body.phone) || !Number.isInteger(body.amountCfa) || body.amountCfa <= 0) return reply(response, 400, { error: 'invalid payment request' });
    if (process.env.PAYMENTS_MODE !== 'live') return reply(response, 503, { error: 'payment provider not configured' });
    const payment = await createPayment({ id: randomUUID(), userId, idempotencyKey: key, method: body.method, amountCfa: body.amountCfa });
    return reply(response, 202, payment);
  } catch { return reply(response, 400, { error: 'payment request failed' }); }
}
