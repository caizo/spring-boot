FROM openjdk:17-slim-buster
COPY "./target/springboot-0.0.1-SNAPSHOT.jar" "app.jar"
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]