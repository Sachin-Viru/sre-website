FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/sre-website-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 2020
ENTRYPOINT ["java", "-jar", "app.jar"]

