# tenisu
Web application Java 21 Spring boot / angular 20

# Tenisu — Guide de démarrage et de test

Ce projet contient une application Spring Boot (module `tenisu-apis`) et un front Angular (module `tenisu-front`). Cette notice explique comment construire, démarrer et tester rapidement l'API en local.

## Prérequis
- JDK 17+ (Java) installé et présent dans le PATH
- Accès Internet pour télécharger les dépendances Maven au premier build
- Optionnel: `curl` pour valider la disponibilité de l'API depuis la ligne de commande

Remarque: Maven n'a pas besoin d'être installé séparément, le projet inclut le wrapper `mvnw`.

## Démarrage rapide (recommandé)
Un script shell est fourni pour automatiser la construction, le démarrage de l'API et l'ouverture du navigateur.

1) Rendre le script exécutable (à faire une fois):
- macOS/Linux:
  - `chmod +x ./start.sh`

2) Lancer:
- macOS/Linux:
  - `./start.sh`

Ce script va:
- exécuter `mvn clean install` à la racine du projet,
- empaqueter et démarrer l'application Spring Boot du module `tenisu-apis`,
- attendre que l'application soit disponible sur http://localhost:8080,
- tenter d'ouvrir automatiquement votre navigateur sur cette URL.

Pour arrêter l'application:
- `kill $(cat tenisu-apis/target/app.pid)`
- ou, en dernier recours: `pkill -f "tenisu-apis/.*/.*\.jar"`

Les logs d'exécution sont consultables dans `tenisu-apis/target/app.log`.

## Démarrage manuel (alternative)
Si vous préférez démarrer sans le script:

1) Construire le projet complet:
- `./mvnw clean install`

2) Démarrer l'API Spring Boot:
- `java -jar tenisu-apis/target/*.jar`

L'application écoute par défaut sur: http://localhost:8080

## Tester l'application
- Depuis un navigateur: ouvrez http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui/index.html (raccourcis: /swagger-ui.html, /swagger, /docs)
- OpenAPI JSON: http://localhost:8080/v3/api-docs
- Avec `curl` (exemples):
  - Santé (si Actuator exposé): `curl -fsS http://localhost:8080/actuator/health | jq .`
  - Joueurs (exemples d'API, ajustez selon vos routes existantes):
    - `curl -fsS http://localhost:8080/apis/players | jq .`

Selon le code existant, les endpoints disponibles incluent typiquement `GET /apis/players` via `PlayerController`.

## Front-end (optionnel)
Le module `tenisu-front` contient l'application Angular. Cette notice se concentre sur l'API back-end. Pour lancer le front (si nécessaire):
- `cd tenisu-front`
- `npm install`
- `npm start` (ou `ng serve`)

Consultez les scripts dans `tenisu-front/package.json` pour les commandes exactes.

## Déploiement Docker + Render + CI/CD

### Image Docker locale
Construire l'image multi-stage (Angular + Spring):
```bash
docker build -t tenisu:local .
```
Lancer le conteneur:
```bash
docker run --rm -p 8080:8080 tenisu:local
```
Swagger: http://localhost:8080/swagger-ui/index.html

### Spécificités image
- Multi-stage: Maven (build) puis JRE léger (runtime)
- Angular est compilé via le cycle Maven et copié dans `public/` par le plugin resources.
- Utilisateur non-root `appuser` (UID 1001).

### Render (déploiement source ou image)
Deux approches:
1. Source + Dockerfile: Render lit `render.yaml` et construit l'image.
2. Image pré-construite (GHCR/Docker Hub) + Hook de déploiement.

Fichier `render.yaml` fourni:
- Service web `tenisu-api` (plan free) utilisant la commande de démarrage injectant `$PORT`.
- Chemin de health check (Swagger UI) `/swagger-ui/index.html`.

Étapes recommandées:
1. Créer un service Web sur Render (New + Blueprint) en pointant sur le repo (branch `main`).
2. Vérifier les variables d'environnement (facultatif) dans le dashboard Render.
3. Déploiements automatiques activés (par défaut).

### Variables / Secrets Render
Aucune base de données externe nécessaire (H2 en mémoire). Possibles variables à ajouter plus tard:
- `SPRING_PROFILES_ACTIVE=prod`
- `JAVA_TOOL_OPTIONS=-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0`

### GitHub Actions (workflow CI/CD)
Workflow: `.github/workflows/ci-cd.yml`
Jobs:
- build-and-test: Compile Java + Angular, exécute tests backend & front (headless).
- docker-image: Construit et pousse l'image vers GHCR (`ghcr.io/<owner>/tenisu:latest` + tags version + commit).
- render-deploy-from-source: Déclenche un déploiement Render via API (si secrets fournis).

Secrets requis (Settings > Secrets and variables > Actions):
- `DOCKERHUB_USERNAME` (optionnel)
- `DOCKERHUB_TOKEN` (optionnel, PAT Docker Hub)
- `RENDER_API_KEY` (optionnel, pour déclenchement API)
- `RENDER_SERVICE_ID` (optionnel, ID du service Render)
- `RENDER_DEPLOY_HOOK_URL` (optionnel, alternative simple via hook)

### Récupérer Service ID Render
`curl -H "Authorization: Bearer <API_KEY>" https://api.render.com/v1/services | jq` et localiser `id`.

### Déploiement manuel via Hook
Si vous configurez un Deploy Hook (Settings > Deploy Hooks) sur Render:
```bash
curl -X POST "$RENDER_DEPLOY_HOOK_URL"
```

### Mises à jour
- Pousser sur `main` déclenche build + nouvelle image + (optionnel) déploiement Render.
- Tags de version gérés à partir du `<version>` Maven parent.

## Dépannage
- Port dynamique Render: Le `startCommand` dans `render.yaml` force `-Dserver.port=$PORT`.
- Erreur front dans build CI: Vérifier la version Node définie dans `tenisu-front/pom.xml` (frontend-maven-plugin).
- Image trop lourde: Envisager distroless ou JLink personnalisé.

## Licence
Usage interne/démonstration. Adapter selon vos besoins.
