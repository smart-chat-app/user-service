FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY . .
RUN mvn -B -DskipTests clean package

FROM eclipse-temurin:21-jre
RUN useradd -ms /bin/bash app
USER app
WORKDIR /app
COPY --from=build /workspace/target/users-service-*.jar app.jar
EXPOSE 8090
ENTRYPOINT ["java","-jar","/app/app.jar"]
