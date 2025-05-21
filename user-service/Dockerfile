FROM gradle:8.13-jdk17 AS builder

WORKDIR /app

COPY . .

RUN gradle :user-service:clean :user-service:build -x test

FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=builder /app/user-service/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]