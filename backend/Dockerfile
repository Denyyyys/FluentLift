FROM maven:3.9.12-eclipse-temurin-25-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
COPY src/main/resources/static/data/polish_verbs_conjugation.json ./data/polish_verbs_conjugation.json
COPY src/main/resources/static/data/synsetsWithTranslatedExamples.json ./data/synsetsWithTranslatedExamples.json
RUN mvn clean package

# -------- Run stage --------
FROM openjdk:27-ea-oraclelinux8

WORKDIR /app
COPY --from=build /app/data/synsetsWithTranslatedExamples.json synsetsWithTranslatedExamples.json
COPY --from=build /app/data/polish_verbs_conjugation.json polish_verbs_conjugation.json

COPY --from=build /app/target/fluentLift-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=seedSynsetsNeo4j,seedPolishVerbConjugationNeo4j"]