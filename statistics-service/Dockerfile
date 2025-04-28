FROM gradle:8-jdk21-alpine AS build

WORKDIR /app
COPY settings.gradle.kts .
COPY build.gradle.kts .
COPY src src

RUN gradle --no-daemon clean bootJar -x test

FROM eclipse-temurin:21-jdk-jammy AS run
WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]