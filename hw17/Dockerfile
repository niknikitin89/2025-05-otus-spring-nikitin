
# Базовый образ - Alpine Linux с установленной JDK 21
# Alpine - легковесный дистрибутив Linux, musl - стандартная библиотека C
# Bellsoft Liberica - это готовый JDK образ, оптимизированный для контейнеров
FROM bellsoft/liberica-openjdk-alpine-musl:21.0.1

# Метаданные образа - автор сборки
# LABEL добавляет метаданные к образу, которые можно посмотреть через 'docker inspect'
LABEL authors="NikNikitin"
LABEL version="1.0"
LABEL description="Spring Boot Application"

# Создаем и переключаемся на непривилегированного пользователя
RUN addgroup -S my_user && adduser -S my_user -G my_user
USER my_user

# Создаем рабочую директорию внутри контейнера
WORKDIR /app

# Копируем JAR файл из хоста в контейнер
# Первый путь - исходный файл на хосте (относительно Dockerfile)
# Второй путь - целевое расположение в контейнере
COPY --chown=my_user:my_user /target/hw17-nikitin-0.0.1-SNAPSHOT.jar /app/app.jar

# Открываем порт, который использует приложение
# EXPOSE не открывает порт на хосте, а только документирует, какой порт используется
EXPOSE 8080

# Точка входа - команда, которая выполняется при запуске контейнера
# ENTRYPOINT определяет основную команду, которая не может быть переопределена
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
