# Build
FROM maven:3.8.6-openjdk-17 AS builder

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -Pci

# Runtime
FROM openjdk:17-jre-slim

RUN apt-get update && \
    apt-get install -y ffmpeg && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY --from=builder /app/target/hackathon-*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]