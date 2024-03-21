FROM openjdk:21
USER root
LABEL authors="antonkrz"
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]