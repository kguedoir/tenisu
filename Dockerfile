# Build multi-étapes pour Tenisu (Front Angular + Backend Spring Boot)

# --- Étape de build du front (Node) ---
FROM node:20.19.0-alpine AS front-build
WORKDIR /front

# Copier uniquement package.json pour éviter les références à un registre privé dans l'ancien fichier lock
COPY tenisu-front/package.json ./
RUN npm install --no-audit --no-fund

# Copier le reste du code source du front
COPY tenisu-front/ .
RUN npm run build -- --configuration production

# --- Étape de build du backend (Maven) ---
FROM maven:3.9-eclipse-temurin-21 AS backend-build
WORKDIR /workspace

# Copier le POM parent et ceux des modules (requis pour la résolution du reactor)
COPY pom.xml ./
COPY tenisu-apis/pom.xml tenisu-apis/
COPY tenisu-front/pom.xml tenisu-front/

# Copier les sources du backend
COPY tenisu-apis/ tenisu-apis/
# Injecter le dist du front construit à l'emplacement attendu pour la copie des ressources
COPY --from=front-build /front/dist/tenisu-front tenisu-front/dist/tenisu-front

# Packager uniquement le module backend (tests ignorés pour accélérer le build de l'image)
RUN mvn -q -B -DskipTests clean package -pl tenisu-apis -am

# --- Étape d'exécution (runtime) ---
FROM eclipse-temurin:21-jre
WORKDIR /app
RUN useradd -u 1001 -ms /bin/bash appuser
COPY --from=backend-build /workspace/tenisu-apis/target/tenisu-apis-*.jar /app/app.jar

# Script de démarrage pour mapper $PORT (Render) vers server.port si défini
RUN echo '#!/bin/sh' > /app/start.sh \
    && echo 'set -e' >> /app/start.sh \
    && echo 'if [ -n "${PORT}" ]; then EXTRA_OPTS="-Dserver.port=${PORT}"; fi' >> /app/start.sh \
    && echo 'exec java ${JAVA_TOOL_OPTIONS} ${JAVA_OPTS} ${EXTRA_OPTS} -jar /app/app.jar' >> /app/start.sh \
    && chmod +x /app/start.sh

EXPOSE 8080
USER appuser
ENV JAVA_TOOL_OPTIONS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"
ENTRYPOINT ["/app/start.sh"]
