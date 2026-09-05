# Téranga Moov

<p align="center">
  <img src="app/src/main/res/drawable/img_app_icon.jpg" alt="Logo Téranga Moov" width="120">
</p>

<h3 align="center">La mobilité sénégalaise, plus simple, plus fiable et plus inclusive.</h3>

<p align="center">
  Application Android de mobilité citoyenne avec suivi des transports, alertes, tickets numériques et signalements communautaires.
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Android-24%2B-3DDC84?logo=android&logoColor=white" alt="Android 24+">
  <img src="https://img.shields.io/badge/Kotlin-2.2.10-7F52FF?logo=kotlin&logoColor=white" alt="Kotlin 2.2.10">
  <img src="https://img.shields.io/badge/Jetpack%20Compose-UI-4285F4?logo=jetpackcompose&logoColor=white" alt="Jetpack Compose">
  <img src="https://img.shields.io/badge/Node.js-API-339933?logo=node.js&logoColor=white" alt="Node.js API">
  <img src="https://img.shields.io/badge/License-MIT-yellow.svg" alt="Licence MIT">
</p>

## 🌍 À propos

Téranga Moov est une super-app conçue pour améliorer les déplacements quotidiens au Sénégal. Elle rassemble dans une même expérience les informations de transport, les itinéraires, les paiements, les tickets et la participation citoyenne.

Le projet est organisé en deux modules :

- **Application Android** : interface Jetpack Compose, carte interactive, données locales Room et prise en charge multilingue.
- **API backend** : routes Node.js déployables sur Vercel, authentification, tickets, paiements et données temps réel.

## 📱 Aperçu de l’application

Les captures suivantes présentent les principaux écrans de Téranga Moov :

<table>
  <tr>
    <td align="center"><strong>Onboarding</strong><br><img src="app/src/main/res/drawable/imageapp/WhatsApp%20Image%202026-09-04%20at%2021.59.35%20%281%29.jpeg" alt="Écran d’onboarding" width="220"></td>
    <td align="center"><strong>Création de compte</strong><br><img src="app/src/main/res/drawable/imageapp/WhatsApp%20Image%202026-09-04%20at%2021.59.36%20%281%29.jpeg" alt="Écran de création de compte" width="220"></td>
    <td align="center"><strong>Accueil</strong><br><img src="app/src/main/res/drawable/imageapp/WhatsApp%20Image%202026-09-04%20at%2021.59.36%20%282%29.jpeg" alt="Écran d’accueil" width="220"></td>
  </tr>
  <tr>
    <td align="center"><strong>Transports à proximité</strong><br><img src="app/src/main/res/drawable/imageapp/WhatsApp%20Image%202026-09-04%20at%2021.59.36%20%283%29.jpeg" alt="Écran des transports à proximité" width="220"></td>
    <td align="center"><strong>Itinéraires</strong><br><img src="app/src/main/res/drawable/imageapp/WhatsApp%20Image%202026-09-04%20at%2021.59.36%20%284%29.jpeg" alt="Écran des itinéraires" width="220"></td>
    <td align="center"><strong>Onboarding final</strong><br><img src="app/src/main/res/drawable/imageapp/WhatsApp%20Image%202026-09-04%20at%2021.59.36.jpeg" alt="Écran final de l’onboarding" width="220"></td>
  </tr>
  <tr>
    <td align="center"><strong>Mon espace</strong><br><img src="app/src/main/res/drawable/imageapp/WhatsApp%20Image%202026-09-04%20at%2021.59.38%20%281%29.jpeg" alt="Écran Mon espace" width="220"></td>
    <td align="center"><strong>Alertes citoyennes</strong><br><img src="app/src/main/res/drawable/imageapp/WhatsApp%20Image%202026-09-04%20at%2021.59.38%20%282%29.jpeg" alt="Écran des alertes citoyennes" width="220"></td>
    <td align="center"><strong>Tickets et Pass</strong><br><img src="app/src/main/res/drawable/imageapp/WhatsApp%20Image%202026-09-04%20at%2021.59.38.jpeg" alt="Écran des tickets et pass" width="220"></td>
  </tr>
</table>

## 📌 État du projet

| Domaine | État |
| --- | --- |
| Application Android | ✅ Fonctionnelle en développement |
| Carte et itinéraires | ✅ Intégrés |
| Authentification | ✅ Disponible côté API |
| Tickets QR | ✅ Disponible avec vérification locale |
| Paiements Mobile Money | 🚧 Mode simulation / intégration opérateur à finaliser |
| Notifications push | 🚧 Prévu |
| Publication Google Play | 🚧 Prévue |

## ✨ Fonctionnalités

| Icône | Fonction | Description |
| --- | --- | --- |
| 🗺️ | Carte en temps réel | Véhicules, arrêts, itinéraires et transports disponibles autour de Dakar. |
| 🚌 | Mobilité multimodale | Suivi des bus, BRT, TER, cars rapides et taxis. |
| 🔔 | Alertes utiles | Retards, incidents, embouteillages et itinéraires alternatifs. |
| 🎫 | Tickets numériques | QR chiffrés, fonctionnement hors ligne, expiration et protection contre la réutilisation locale. |
| 💳 | Paiements | Préparation pour Wave, Orange Money et Free Money via une API sécurisée. |
| 📣 | Signalements citoyens | Déclaration d’incidents et partage des informations avec la communauté. |
| 📍 | Autour de moi | Recentrage GPS, bouton SOS et mode économie de données. |
| 🌐 | Accessibilité | Interface multilingue et lecture vocale. |
| 📊 | Statistiques | Suivi des trajets et des économies réalisées. |

## 🧱 Architecture

```text
teranga/
├── app/                       # Application Android Kotlin / Jetpack Compose
│   └── src/main/              # Écrans, thème, données locales et composants
├── backend/                   # API Node.js pour Vercel
│   ├── api/                   # Routes serverless
│   ├── src/                   # Serveur HTTP, base de données et sécurité
│   └── db/schema.sql          # Schéma PostgreSQL / Neon
├── gradle/                    # Version catalog et wrapper Gradle
└── README.md
```

### 🛠️ Technologies

- **Android** : Kotlin, Jetpack Compose, Material 3, AndroidX, Room, KSP.
- **Réseau** : Retrofit, OkHttp, Moshi et API REST.
- **Cartographie** : WebView Leaflet avec tuiles OpenStreetMap, centrage GPS, véhicules et zones de trafic à Dakar.
- **Backend** : Node.js, modules ES, Vercel Functions et PostgreSQL/Neon.
- **Sécurité** : Android Keystore, JWT, clés d’idempotence et variables d’environnement.

## 🚀 Installation locale

### Prérequis

- Android Studio récent
- JDK 17
- Node.js 20 ou version supérieure
- Un émulateur Android ou un appareil physique

### Construire l’application Android

Depuis la racine du dépôt :

```powershell
.\gradlew.bat assembleDebug
```

L’APK est généré ici :

```text
app/build/outputs/apk/debug/app-debug.apk
```

Commandes utiles :

```powershell
.\gradlew.bat :app:compileDebugKotlin
.\gradlew.bat test
.\gradlew.bat assembleRelease
```

Pour un build local sans clé de publication, `debug.keystore` est utilisé automatiquement. Pour signer une version destinée à la publication, définir `KEYSTORE_PATH`, `STORE_PASSWORD` et `KEY_PASSWORD` dans l’environnement.

### Démarrer l’API

```powershell
cd backend
npm install
$env:JWT_SECRET = 'remplacer-par-une-cle-aleatoire-d-au-moins-32-caracteres'
npm start
```

Vérification syntaxique :

```powershell
node --check src/server.mjs
node --check src/db.mjs
```

## 🔌 API disponible

Les routes principales sont :

| Méthode | Route | Usage |
| --- | --- | --- |
| `GET` | `/api/health` | Vérifier l’état de l’API et de la base de données. |
| `POST` | `/api/auth/register` | Créer un compte. |
| `POST` | `/api/auth/login` | Ouvrir une session. |
| `GET` | `/api/vehicles` | Récupérer les véhicules disponibles. |
| `GET` | `/api/alerts` | Récupérer les alertes de mobilité. |
| `POST` | `/api/payments/charge` | Initialiser un paiement authentifié. |
| `POST` | `/api/tickets/verify` | Vérifier un ticket authentifié. |

Le mode paiement est désactivé par défaut avec `PAYMENTS_MODE=disabled`.

### Exemple de vérification de santé

Avec l’API lancée localement sur le port `3000` :

```powershell
Invoke-RestMethod -Uri http://localhost:3000/api/health -Method Get
```

Réponse attendue lorsque la base de données est disponible :

```json
{
  "status": "ok",
  "database": "ready"
}
```

## ☁️ Déploiement backend

1. Importer le dossier `backend` dans Vercel en tant que **Root Directory**.
2. Créer une base Neon ou Vercel Postgres.
3. Exécuter [backend/db/schema.sql](backend/db/schema.sql) dans la console SQL.
4. Configurer les variables suivantes dans Vercel :

```env
POSTGRES_URL=<url-neon>
JWT_SECRET=<secret-aleatoire-d-au-moins-32-caracteres>
CORS_ORIGIN=https://<votre-projet>.vercel.app
PAYMENTS_MODE=disabled
```

5. Redéployer et vérifier `https://<votre-projet>.vercel.app/api/health`.

Pour compiler l’application avec l’URL de l’API déployée :

```powershell
.\gradlew.bat :app:assembleDebug -PterangaApiUrl=https://votre-api.vercel.app/
```

## 🔐 Sécurité et production

Avant toute mise en production :

- remplacer les stockages mémoire par PostgreSQL/Redis ;
- activer TLS, le rate limiting distribué et des logs structurés ;
- faire tourner les secrets et signer les webhooks de paiement ;
- utiliser une signature serveur pour les QR vérifiables par plusieurs contrôleurs ;
- configurer `google-services.json`, Firebase Cloud Messaging et la clé de signature Android ;
- valider les traductions et les voix avec des locuteurs natifs ;
- publier une politique de confidentialité conforme à la réglementation sénégalaise.

⚠️ Ne jamais versionner `POSTGRES_URL`, `JWT_SECRET`, les clés Mobile Money, `google-services.json` ou un fichier `.env` contenant des secrets.

## 🗺️ Roadmap

- [x] Application Android initiale
- [x] Carte interactive et suivi des transports
- [x] Authentification et tickets QR
- [x] API serverless déployable sur Vercel
- [ ] Connecter les paiements Wave, Orange Money et Free Money en production
- [ ] Ajouter les notifications push et les alertes personnalisées
- [ ] Remplacer les stockages mémoire par PostgreSQL/Redis sur tous les flux
- [ ] Ajouter les tests d’intégration backend et les tests UI Android
- [ ] Publier l’application sur Google Play

## 🌐 Langues ciblées

L’application prévoit une base multilingue à enrichir avec des linguistes et locuteurs natifs : français, wolof, pulaar, sérère, mandinka, soninké, diola, balante, mancagne, noon, manjaque, saafi, bassari, bayot, bédik, ndut, palor, léhar, badiaranké, baïnouk et jalonké.

## 🤝 Contribution

1. Créer une branche dédiée : `git switch -c feature/ma-fonctionnalite`.
2. Effectuer les changements et ajouter les tests nécessaires.
3. Vérifier `./gradlew.bat test` et `./gradlew.bat assembleDebug`.
4. Ouvrir une Pull Request avec le contexte, les captures utiles et les étapes de vérification.

### 🧩 Dépannage rapide

- **Le build release demande une clé** : définir `KEYSTORE_PATH`, `STORE_PASSWORD` et `KEY_PASSWORD`, ou utiliser le `debug.keystore` prévu pour les builds locaux.
- **L’API refuse les requêtes authentifiées** : vérifier que `JWT_SECRET` contient au moins 32 caractères.
- **La base est indisponible** : vérifier `POSTGRES_URL`, le schéma `backend/db/schema.sql` et la réponse de `/api/health`.
- **La carte reste vide** : vérifier la connexion Internet et l’accès aux tuiles OpenStreetMap.

## 📄 Licence

Ce projet est distribué sous licence MIT. Voir [LICENSE](LICENSE).

---

<p align="center">Fait avec soin pour une mobilité plus accessible au Sénégal 🇸🇳</p>
