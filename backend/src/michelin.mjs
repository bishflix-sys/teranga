const tokenUrl = 'https://api.michelin.com/idp/v1/internal/oauth/token/accesstoken';

let cachedToken = null;

export async function getMichelinAccessToken() {
  const clientId = process.env.MICHELIN_CLIENT_ID;
  const clientSecret = process.env.MICHELIN_CLIENT_SECRET;
  const apiKey = process.env.MICHELIN_API_KEY;
  if (!clientId || !clientSecret || !apiKey) throw new Error('Michelin OAuth is not configured');
  if (cachedToken && cachedToken.expiresAt > Date.now() + 30_000) return cachedToken.accessToken;

  const body = new URLSearchParams({
    client_id: clientId,
    client_secret: clientSecret,
    ...(process.env.MICHELIN_SCOPE ? { scope: process.env.MICHELIN_SCOPE } : {})
  });
  const response = await fetch(`${tokenUrl}?grant_type=client_credentials`, {
    method: 'POST',
    headers: {
      apikey: apiKey,
      'content-type': 'application/x-www-form-urlencoded',
      accept: 'application/json'
    },
    body
  });
  if (!response.ok) throw new Error(`Michelin OAuth failed with status ${response.status}`);

  const payload = await response.json();
  if (typeof payload.access_token !== 'string' || !payload.access_token) throw new Error('Michelin OAuth returned no access token');
  const expiresIn = Number(payload.expires_in) || 300;
  cachedToken = { accessToken: payload.access_token, expiresAt: Date.now() + expiresIn * 1000 };
  return cachedToken.accessToken;
}
