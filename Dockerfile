FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app
COPY target/*.jar app.jar

EXPOSE 2020

CMD ["java", "-XX:+IgnoreUnrecognizedVMOptions", "-XX:+UseContainerSupport", "-Dmanagement.metrics.enable.processor=false", "-jar", "app.jar"]

