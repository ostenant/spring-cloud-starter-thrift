FROM openjdk:8-jdk-alpine
ADD target/deposit-server-1.0-SNAPSHOT.jar deposit-server.jar
ENTRYPOINT ["java", "-jar", "deposit-server.jar"]