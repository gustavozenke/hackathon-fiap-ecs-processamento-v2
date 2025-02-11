FROM maven:3.8.6-eclipse-temurin-17 AS build

WORKDIR /app

COPY app/ ./

RUN chmod +x mvnw || true

ARG AWS_ACCESS_KEY_ID
ARG AWS_SECRET_ACCESS_KEY

ENV AWS_ACCESS_KEY_ID=$AWS_ACCESS_KEY_ID
ENV AWS_SECRET_ACCESS_KEY=$AWS_SECRET_ACCESS_KEY

RUN mvn clean package

FROM eclipse-temurin:17-jre

RUN apt-get update && \
    apt-get install -y ffmpeg && \
    rm -rf /var/lib/apt/lists/*


COPY --from=build /app/target/hackathon-*.jar app.jar

# Expor a porta que a aplicação vai rodar
EXPOSE 8000

# Comando para rodar a aplicação
CMD ["java", "-jar", "app.jar"]