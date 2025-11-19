FROM openjdk:21
WORKDIR /app
COPY ${JAR_FILE} app.jar
EXPOSE 8084
CMD ["java", "-jar", "service.layer-0.0.1-SNAPSHOT.jar"]