# Multi-stage build for Tenisu (Spring Boot + Angular)
# 1) Build stage (Maven + JDK 21)
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace

# Copy root Maven files first to leverage Docker layer caching
COPY pom.xml ./
COPY tenisu-apis/pom.xml tenisu-apis/
COPY tenisu-front/pom.xml tenisu-front/

# Pre-fetch dependencies (offline mode) to speed up incremental builds
RUN mvn -q -B -e -DskipTests dependency:go-offline || true

# Copy the whole project (after deps to maximize cache reuse)
COPY . .

# Build (skip tests inside container to avoid needing browsers)
RUN ./mvnw -q -B -DskipTests clean package

# 2) Runtime stage (lean JRE 21)
FROM eclipse-temurin:21-jre
WORKDIR /app

# Non-root user for security
RUN useradd -u 1001 -ms /bin/bash appuser

# Copy the built Spring Boot executable jar
# Use wildcard to stay resilient to version changes
COPY --from=build /workspace/tenisu-apis/target/tenisu-apis-*.jar /app/app.jar

EXPOSE 8080
USER appuser

# JVM tuning: prefer container awareness; can be overridden at runtime
ENV JAVA_TOOL_OPTIONS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

ENTRYPOINT ["java","-jar","/app/app.jar"]

