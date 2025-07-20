FROM openjdk:17-jdk-slim
COPY target/e-commerce-config-server-0.0.1-SNAPSHOT.jar configserver.jar
ENTRYPOINT ["java", "-jar", "configserver.jar"]