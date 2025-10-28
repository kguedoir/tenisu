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

## Dépannage
- Port 8080 déjà utilisé: libérez le port ou changez le port Spring (`server.port`) via `application.properties` ou `-Dserver.port=...`.
- JDK non détecté: vérifiez `java -version` et votre variable d'environnement `PATH`.
- Le script `start.sh` n'ouvre pas le navigateur: ouvrez manuellement http://localhost:8080.
- L'application s'arrête immédiatement: consultez `tenisu-apis/target/app.log`.

## Licence
Usage interne/démonstration. Adapter selon vos besoins.
