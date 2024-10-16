FROM openjdk:17-jdk-slim-buster
WORKDIR /app
COPY /target/taco-cloud-0.0.1-SNAPSHOT.jar /app/taco.jar
ENTRYPOINT ["java", "-jar", "taco.jar"]
EXPOSE 8080

