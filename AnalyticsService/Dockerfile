FROM openjdk:17-slim
WORKDIR /app
# Копируем собранный jar из стадии сборки
COPY build/libs/*.jar app.jar
# Экспортируйте порт, на котором работает приложение
EXPOSE 8004
ENTRYPOINT ["java", "-jar", "app.jar"]