FROM maven:3.9.12-eclipse-temurin-25-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package

# -------- Run stage --------
FROM openjdk:27-ea-oraclelinux8

WORKDIR /app
COPY --from=build /app/target/fluentLift-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]