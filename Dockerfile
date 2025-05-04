FROM openjdk:21-jdk-slim

WORKDIR /bcb

COPY build/libs/*.jar bcb.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/bcb/bcb.jar"]