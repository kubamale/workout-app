# Workout Management Application

The Workout Application is a backend application designed to manage
workout routines, exercises, and progress tracking.
Built using Spring Boot, the microservice follows a clean microservices
architecture, enabling scalability, modularity, and easy integration
with other services. This project supports essential CRUD operations for
managing workouts and exercises while providing a robust and maintainable codebase.

## Run Kafka broker

```shell
docker run -d --name broker -p 9092:9092  apache/kafka:latest
```