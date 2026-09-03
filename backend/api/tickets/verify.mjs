import { consumeTicket } from '../../src/db.mjs';
import { userIdFromRequest } from '../../src/security.mjs';
import { readJson, reply, requirePost } from '../../src/http.mjs';

export default async function handler(request, response) {
  const methodError = requirePost(request, response);
  if (methodError) return methodError;
  if (!userIdFromRequest(request)) return reply(response, 401, { error: 'authentication required' });
  try {
    const body = await readJson(request);
    if (!body.ticketId) return reply(response, 400, { error: 'ticketId is required' });
    const valid = await consumeTicket(body.ticketId);
    return reply(response, valid ? 200 : 409, { valid });
  } catch { return reply(response, 400, { error: 'ticket verification failed' }); }
}
