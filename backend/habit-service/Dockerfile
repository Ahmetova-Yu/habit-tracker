FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Копируем ВСЕ jar в папку /tmp
COPY target/*.jar /tmp/

# Находим единственный JAR, в имени которого НЕТ .original, и копируем его в /app
RUN find /tmp -name "*.jar" ! -name "*.original" -exec cp {} /app/app.jar \;

ENTRYPOINT ["java", "-jar", "/app/app.jar"]