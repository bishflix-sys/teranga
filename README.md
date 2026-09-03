# Téranga Moov

Téranga Moov est une super-app citoyenne de mobilité au Sénégal. Elle aide les usagers à suivre les transports, payer leurs trajets, recevoir des alertes fiables et participer à l'amélioration de la mobilité locale.

## Vision

La plateforme combine géolocalisation en temps réel, notifications intelligentes, paiement numérique et signalement citoyen. Elle est conçue pour réduire l'attente inutile, faciliter les déplacements et rester accessible aux différentes communautés linguistiques du Sénégal.

## Fonctionnalités

- Carte interactive Leaflet centrée sur Dakar avec véhicules en temps réel et effet de perspective 3D.
- Suivi des bus, BRT, TER, cars rapides et taxis.
- Alertes sur les retards, embouteillages, incidents et itinéraires alternatifs.
- Tickets, abonnements et paiements numériques avec Wave, Orange Money et Free Money.
- Signalements citoyens avec catégories d'incidents et partage WhatsApp.
- Pass numériques pour étudiants, travailleurs et usagers réguliers.
- Statistiques personnalisées sur les trajets et les économies réalisées.
- Lecture vocale et interface multilingue.
- Recentrage GPS « Autour de moi », bouton SOS et mode économie de données.
- QR de tickets chiffrés hors ligne, expiration de 15 minutes et anti-réutilisation locale.
- Sélecteur TER, BRT, DDD, AFTU et Taxi avec tarification TER par zone ou classe.

## Architecture

Le dépôt contient deux parties :

- `app/` : application Android Kotlin/Jetpack Compose, Room, WebView Leaflet et Android Keystore.
- `backend/` : API Node.js déployable sur Vercel avec Neon PostgreSQL.

Le backend expose notamment `GET /api/health`, `POST /api/auth/register`, `POST /api/auth/login`, ainsi que les routes de paiement et de vérification de tickets documentées dans [backend/README.md](backend/README.md).

## Business Model Canvas

| Bloc | Détail |
| --- | --- |
| Proposition de valeur | Mobilité en temps réel, paiement sans espèces, alertes utiles et service inclusif. |
| Segments clients | Usagers quotidiens, étudiants, travailleurs, entreprises, municipalités, ONG et autorités. |
| Canaux | Application Android, WhatsApp, réseaux sociaux, radios locales et partenariats de transport. |
| Relations clients | Interface simple, assistance multilingue, notifications vocales et participation citoyenne. |
| Sources de revenus | Premium, commissions de paiement, publicité locale, offres entreprises et statistiques anonymisées. |
| Ressources clés | Application, données de mobilité, infrastructure cloud, équipe technique et expertise linguistique. |
| Activités clés | Développement, exploitation des données, gestion des paiements, support et animation communautaire. |
| Partenaires clés | Transporteurs, opérateurs mobile money, municipalités, ministère, universités et incubateurs locaux. |
| Structure des coûts | Développement, hébergement, sécurité, support, traduction, communication et partenariats. |

## Langues nationales ciblées

L'application prévoit une base multilingue à enrichir avec des linguistes et locuteurs natifs :

1. Wolof
2. Pulaar
3. Sérère
4. Mandinka
5. Soninké
6. Diola
7. Balante
8. Mancagne
9. Noon
10. Manjaque
11. Saafi
12. Bassari
13. Bayot
14. Bédik
15. Ndut
16. Palor
17. Léhar
18. Badiaranké
19. Baïnouk
20. Jalonké

Le français reste disponible comme langue de référence. Les traductions, la terminologie et les voix doivent être validées localement avant diffusion publique.

## Logo et icône

Le logo officiel Téranga Moov doit être conservé sans modification et utilisé de manière cohérente dans l'application. La ressource principale est [img_app_icon.jpg](app/src/main/res/drawable/img_app_icon.jpg).

Pour Android, fournir idéalement des exports carrés aux densités suivantes : `mdpi` 48 x 48 px, `hdpi` 72 x 72 px, `xhdpi` 96 x 96 px, `xxhdpi` 144 x 144 px et `xxxhdpi` 192 x 192 px. Garder une zone de sécurité intérieure pour éviter la découpe par les masques adaptatifs.

## Développement local

**Prérequis :** Android Studio, Java 17 et le Gradle Wrapper inclus dans le projet.

```powershell
.\gradlew.bat :app:compileDebugKotlin
.\gradlew.bat assembleDebug
.\gradlew.bat test
```

Vérifier également le backend :

```powershell
node --check backend/src/server.mjs
node --check backend/src/db.mjs
npm install --prefix backend
```

Ouvrir ensuite le projet dans Android Studio et choisir un émulateur ou un appareil Android. La carte Leaflet charge ses tuiles depuis CARTO/OpenStreetMap lorsqu'une connexion Internet est disponible.

## Neon et Vercel

1. Importer le dépôt dans Vercel et choisir `backend` comme **Root Directory**.
2. Créer une base Neon depuis Vercel ou la console Neon.
3. Exécuter [backend/db/schema.sql](backend/db/schema.sql) dans le SQL Editor Neon.
4. Ajouter dans Vercel, pour Preview et Production :

```env
POSTGRES_URL=<url-neon>
JWT_SECRET=<secret-aleatoire-de-32-caracteres-minimum>
CORS_ORIGIN=https://<votre-projet>.vercel.app
PAYMENTS_MODE=disabled
```

5. Redéployer et vérifier `https://<votre-projet>.vercel.app/api/health`.

La réponse attendue lorsque la base est disponible contient `"status":"ok"` et `"database":"ready"`.

Ne jamais committer `POSTGRES_URL`, `JWT_SECRET`, des tokens Mobile Money ou un fichier `.env`.

## Sécurité et production

Avant une publication :

- remplacer les stockages mémoire du backend par PostgreSQL/Redis pour les véhicules, alertes et sessions ;
- connecter les API officielles Wave, Orange Money et Free Money avec webhooks signés ;
- utiliser une signature serveur pour les QR vérifiables par plusieurs contrôleurs ;
- ajouter rate limiting, rotation des secrets, logs structurés et monitoring ;
- configurer `google-services.json`, les clés de signature et Firebase Cloud Messaging ;
- ajouter caméra, NFC carte réel, géofencing serveur et calculateur intermodal ;
- publier une politique de confidentialité conforme à la CDP et valider les traductions avec des locuteurs natifs.

Le dossier `backend/node_modules/` est ignoré par Git. Les secrets doivent être configurés uniquement dans Vercel ou dans l’environnement local.
