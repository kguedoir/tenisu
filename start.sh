#!/usr/bin/env sh
set -e

# Script de démarrage pour construire le projet, lancer l'API Spring Boot
# et ouvrir automatiquement le navigateur sur http://localhost:8080
#
# Utilisation:
#   chmod +x ./start.sh
#   ./start.sh
#
# Pour arrêter l'application:
#   if [ -f tenisu-apis/target/app.pid ]; then kill "$(cat tenisu-apis/target/app.pid)"; fi
#   # ou, en dernier recours: pkill -f "tenisu-apis/.*/.*\.jar"

ROOT_DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$ROOT_DIR"

# Vérifications de base
if ! command -v java >/dev/null 2>&1; then
  echo "[ERREUR] Java (JDK) est requis dans le PATH." >&2
  exit 1
fi

# Détermination de la commande Maven (mvnw si présent, sinon mvn système)
MVNW="$ROOT_DIR/mvnw"
if [ -f "$MVNW" ]; then
  if [ ! -x "$MVNW" ]; then
    echo "[INFO] Donne les droits d'exécution au wrapper Maven (mvnw)";
    chmod +x "$MVNW" || true
  fi
  MVN_CMD="$MVNW"
else
  echo "[INFO] Wrapper Maven (mvnw) introuvable. Utilisation de Maven du système (mvn)."
  if ! command -v mvn >/dev/null 2>&1; then
    echo "[ERREUR] Maven n'est pas installé ou indisponible dans le PATH, et mvnw est absent." >&2
    exit 1
  fi
  MVN_CMD="mvn"
fi

# 1) Build complet du projet
echo "[INFO] Construction du projet (mvn clean install) ..."
"$MVN_CMD" -q clean install

# 2) Déterminer/produire le JAR de l'API
JAR_GLOB="$ROOT_DIR/tenisu-apis/target/*.jar"
JAR_PATH="$(ls $JAR_GLOB 2>/dev/null | head -n1 || true)"
if [ ! -f "$JAR_PATH" ]; then
  echo "[INFO] Aucun JAR trouvé, empaquetage du module tenisu-apis ..."
  "$MVNW" -q -pl tenisu-apis -am package
  JAR_PATH="$(ls $JAR_GLOB 2>/dev/null | head -n1 || true)"
fi

if [ ! -f "$JAR_PATH" ]; then
  echo "[ERREUR] Impossible de trouver le JAR à exécuter dans tenisu-apis/target." >&2
  exit 1
fi

echo "[INFO] Démarrage de l'application Spring Boot: $JAR_PATH"
LOG_FILE="$ROOT_DIR/tenisu-apis/target/app.log"
PID_FILE="$ROOT_DIR/tenisu-apis/target/app.pid"

# Lancer en arrière-plan
nohup java -jar "$JAR_PATH" > "$LOG_FILE" 2>&1 &
APP_PID=$!
echo "$APP_PID" > "$PID_FILE"
echo "[INFO] PID: $APP_PID (logs: $LOG_FILE)"

# 3) Attendre que l'application réponde
URL="http://localhost:8080"
echo "[INFO] Attente du démarrage de l'API sur $URL ..."
TRIES=60
SLEEP=1
READY=0
while [ $TRIES -gt 0 ]; do
  if command -v curl >/dev/null 2>&1; then
    if curl -fsS "$URL/actuator/health" >/dev/null 2>&1 || curl -fsS "$URL" >/dev/null 2>&1; then
      READY=1
      break
    fi
  else
    # Si curl n'est pas dispo, attendre simplement quelques secondes
    if [ $TRIES -le 55 ]; then
      READY=1
      break
    fi
  fi
  sleep $SLEEP
  TRIES=$((TRIES-1))
  # Si le process s'arrête prématurément
  if ! kill -0 "$APP_PID" >/dev/null 2>&1; then
    echo "[ERREUR] Le process de l'application s'est terminé de manière inattendue. Consultez $LOG_FILE" >&2
    exit 1
  fi
done

if [ "$READY" -ne 1 ]; then
  echo "[AVERTISSEMENT] Impossible de confirmer que l'application écoute sur $URL. Essayez d'ouvrir l'URL manuellement."
else
  echo "[INFO] L'application est démarrée. Ouverture du navigateur ..."
  if command -v xdg-open >/dev/null 2>&1; then
    xdg-open "$URL" >/dev/null 2>&1 || true
  elif command -v open >/dev/null 2>&1; then
    open "$URL" >/dev/null 2>&1 || true
  elif command -v start >/dev/null 2>&1; then
    start "$URL" >/dev/null 2>&1 || true
  else
    echo "[INFO] Ouvrez votre navigateur sur: $URL"
  fi
fi

echo "[INFO] Pour arrêter: kill $(cat "$PID_FILE" 2>/dev/null || echo $APP_PID)"
