FROM maven:3.8.6-eclipse-temurin-17 AS build
WORKDIR /app

COPY app/ ./

RUN chmod +x mvnw || true

RUN mvn clean package

FROM eclipse-temurin:17-jre

RUN apt-get update && \
    apt-get install -y ffmpeg && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8000

CMD ["java", "-jar", "app.jar"]