FROM openjdk:26-ea-29-oraclelinux8
VOLUME /tmp
COPY target/fluentLift-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]