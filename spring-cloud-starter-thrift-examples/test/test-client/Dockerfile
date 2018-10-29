FROM openjdk:8-jdk-alpine
ADD target/test-client-1.0-SNAPSHOT.jar test-client.jar
ENTRYPOINT ["java", "-jar", "test-client.jar"]