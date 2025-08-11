FROM openjdk:26-jdk-slim-bullseye
VOLUME /tmp
COPY target/fluentLift-0.0.1-SNAPSHOT.jar app.jar
# for debbuging expose port only; TODO: remove in production
ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "-jar", "/app.jar"] 
# ENTRYPOINT ["java","-jar","/app.jar"]