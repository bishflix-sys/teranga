import { neon } from '@neondatabase/serverless';

const sql = process.env.POSTGRES_URL ? neon(process.env.POSTGRES_URL) : null;

export async function checkDatabase() {
  if (!sql) throw new Error('POSTGRES_URL is required');
  const result = await sql`SELECT 1 AS ready`;
  return result[0]?.ready === 1;
}

export async function findUserByEmail(email) {
  if (!sql) throw new Error('POSTGRES_URL is required');
  const result = await sql`SELECT id, email, password_hash FROM users WHERE email = ${email.toLowerCase()} LIMIT 1`;
  return result[0] ?? null;
}

export async function createUser({ id, email, passwordHash }) {
  if (!sql) throw new Error('POSTGRES_URL is required');
  await sql`
    INSERT INTO users (id, email, password_hash)
    VALUES (${id}, ${email.toLowerCase()}, ${passwordHash})
  `;
}

export async function createPayment({ id, userId, idempotencyKey, method, amountCfa }) {
  if (!sql) throw new Error('POSTGRES_URL is required');
  const result = await sql`
    INSERT INTO payments (id, user_id, idempotency_key, method, amount_cfa, status)
    VALUES (${id}, ${userId}, ${idempotencyKey}, ${method}, ${amountCfa}, 'pending')
    ON CONFLICT (idempotency_key) DO UPDATE SET idempotency_key = EXCLUDED.idempotency_key
    RETURNING id, status, method, amount_cfa
  `;
  return result[0];
}
