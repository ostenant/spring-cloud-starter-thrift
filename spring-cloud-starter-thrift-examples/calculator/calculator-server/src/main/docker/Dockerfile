FROM openjdk:8-jdk-alpine
ADD target/spring-boot-thrift-server-0.0.1-SNAPSHOT.jar calculator-server.jar
ENTRYPOINT ["java", "-jar", "calculator-server.jar"]