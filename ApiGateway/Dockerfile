# Лёгкое окружение с JDK 17
FROM openjdk:17-jdk-slim
LABEL name="api-gateway"

WORKDIR /app

COPY build/libs/ApiGateway-0.0.1-SNAPSHOT.jar app.jar

# Запускаем приложение
ENTRYPOINT ["java","-jar","/app/app.jar"]