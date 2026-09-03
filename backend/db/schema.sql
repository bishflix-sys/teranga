CREATE TABLE IF NOT EXISTS users (
  id UUID PRIMARY KEY,
  email TEXT NOT NULL UNIQUE,
  password_hash TEXT NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS payments (
  id UUID PRIMARY KEY,
  user_id UUID NOT NULL REFERENCES users(id),
  idempotency_key TEXT NOT NULL UNIQUE,
  method TEXT NOT NULL CHECK (method IN ('Wave', 'Orange Money', 'Free Money')),
  amount_cfa INTEGER NOT NULL CHECK (amount_cfa > 0),
  status TEXT NOT NULL CHECK (status IN ('pending', 'paid', 'failed')),
  provider_reference TEXT,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS tickets (
  id UUID PRIMARY KEY,
  user_id UUID NOT NULL REFERENCES users(id),
  ticket_number TEXT NOT NULL UNIQUE,
  qr_token_hash TEXT NOT NULL UNIQUE,
  origin TEXT NOT NULL,
  destination TEXT NOT NULL,
  fare_cfa INTEGER NOT NULL CHECK (fare_cfa > 0),
  expires_at TIMESTAMPTZ NOT NULL,
  used_at TIMESTAMPTZ,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS alerts (
  id UUID PRIMARY KEY,
  category TEXT NOT NULL,
  location_name TEXT NOT NULL,
  description TEXT NOT NULL,
  severity TEXT NOT NULL,
  confirmations_count INTEGER NOT NULL DEFAULT 1,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS vehicles (
  id TEXT PRIMARY KEY,
  line_code TEXT NOT NULL,
  category TEXT NOT NULL,
  latitude DOUBLE PRECISION NOT NULL,
  longitude DOUBLE PRECISION NOT NULL,
  eta_minutes INTEGER NOT NULL,
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS tickets_user_idx ON tickets(user_id);
CREATE INDEX IF NOT EXISTS alerts_created_at_idx ON alerts(created_at DESC);
CREATE INDEX IF NOT EXISTS vehicles_updated_at_idx ON vehicles(updated_at DESC);
