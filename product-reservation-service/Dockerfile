FROM amazoncorretto:17-alpine

WORKDIR /app

ARG JAR_FILE=./*.jar
ARG ENV_FILE=./.env
COPY ${JAR_FILE} app.jar
COPY ${ENV_FILE} .env

EXPOSE 8085

ENTRYPOINT ["java", "-jar", "app.jar"]