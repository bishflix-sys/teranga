# Téranga Moov API

Backend minimal sécurisé pour les comptes, les tickets, les paiements et les données temps réel.

## Stockage Vercel

1. Importer le dossier `backend` dans Vercel.
2. Créer une intégration **Vercel Postgres / Neon** dans le projet Vercel.
3. Vérifier que `POSTGRES_URL` est injectée dans les environnements Preview et Production.
4. Exécuter `db/schema.sql` dans la console SQL du fournisseur.
5. Vérifier `GET /api/health` : la réponse doit indiquer `database: ready`.

Pour produire l'APK avec l'URL Vercel de l'API :

```powershell
.\gradlew.bat :app:assembleDebug -PterangaApiUrl=https://votre-api.vercel.app/
```

Ne jamais committer `POSTGRES_URL`, `JWT_SECRET` ou des clés d’opérateurs de paiement.

## Démarrage

```powershell
cd backend
$env:JWT_SECRET = 'replace-with-at-least-32-random-characters'
npm start
```

Routes disponibles :

- `GET /health`
- `POST /auth/register`
- `POST /auth/login`
- `GET /vehicles`
- `GET /alerts`
- `GET /api/michelin/token` avec `Authorization: Bearer ...` pour obtenir un token Michelin temporaire sur Vercel (`/michelin/token` en local)
- `POST /payments/charge` avec `Authorization` et `Idempotency-Key`
- `POST /tickets/verify` avec `Authorization`

Le mode paiement reste désactivé par défaut (`PAYMENTS_MODE=disabled`). Un adaptateur Wave, Orange Money ou Free Money doit être ajouté côté serveur avant de passer en `live`. Les cartes, mots de passe et secrets ne doivent jamais être stockés dans le dépôt.

### Configuration Michelin

Renseigner `MICHELIN_API_KEY`, `MICHELIN_CLIENT_ID`, `MICHELIN_CLIENT_SECRET` et, si nécessaire, `MICHELIN_SCOPE` dans l’environnement du backend. Le backend appelle `POST https://api.michelin.com/idp/v1/internal/oauth/token/accesstoken` avec `grant_type=client_credentials` en query string et les identifiants en `application/x-www-form-urlencoded`. Le `client_secret` reste uniquement côté serveur.

Ce socle utilise des stockages mémoire pour le développement. Avant production, remplacer les `Map` par PostgreSQL/Redis, ajouter TLS au reverse proxy, rotation des secrets, rate limiting distribué, logs structurés et validation webhook opérateur.

Les routes d’authentification appliquent une limite de tentatives, les JWT malformés sont rejetés sans exception et les payloads JSON sont limités à 100 Ko. En production, compléter avec un rate limiting distribué, la rotation des secrets et une supervision des logs.
