# ---------- Build (uses Maven inside the container) ----------
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /src
COPY pom.xml .
RUN --mount=type=cache,target=/root/.m2 mvn -q -e -DskipTests dependency:go-offline
COPY src ./src
RUN --mount=type=cache,target=/root/.m2 mvn -q -e -DskipTests package

# ---------- Runtime ----------
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
# set this to your actual jar name if different
ARG JAR_FILE=/src/target/users-service-0.0.1-SNAPSHOT.jar
COPY --from=build ${JAR_FILE} /app/app.jar
EXPOSE 8080
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75.0"
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]
