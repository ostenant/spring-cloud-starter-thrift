FROM openjdk:8-jdk-alpine
ADD target/test-server-1.0-SNAPSHOT.jar test-server.jar
ENTRYPOINT ["java", "-jar", "test-server.jar"]