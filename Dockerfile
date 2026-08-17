# ---- Build stage ----
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /build

# Cache dependencies separately from source so code edits don't re-download the world
COPY pom.xml .
RUN mvn -B dependency:go-offline

COPY src ./src
RUN mvn -B clean package -DskipTests

# ---- Runtime stage ----
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Run as a non-root user
RUN addgroup -S blog && adduser -S blog -G blog
USER blog

COPY --from=build /build/target/*.jar app.jar

EXPOSE 8080

ENV JAVA_OPTS="-XX:MaxRAMPercentage=75.0"

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar app.jar"]
