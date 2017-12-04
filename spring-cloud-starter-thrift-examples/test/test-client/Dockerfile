FROM registry.icekredit.com:443/base/jre8:122
ADD target/test-client-1.0-SNAPSHOT.jar test-client.jar
ENTRYPOINT ["java", "-jar", "test-client.jar"]