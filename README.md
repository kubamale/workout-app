# Workout Management Application

The Workout Application is a backend application designed to manage
workout routines, exercises, and progress tracking.
Built using Spring Boot, the microservice follows a clean microservices
architecture, enabling scalability, modularity, and easy integration
with other services. This project supports essential CRUD operations for
managing workouts and exercises while providing a robust and maintainable codebase.

## System Architecture

![System architecture](/.images/architecture.png)
Application is composed of multiple services. It is following the API Gateway pattern.
There for the business services can stay in private network and the only component that will be exposed to public
network,
will be API Gateway.

### Api Gateway

Its main responsibility is redirecting requests to correct services and authorizing requests. When authenticated user
is making request there will be a JWT token present in HTTP Authorization header.
This token contains necessary data to determine who is making the request. JWT also caries the information of
users preferred weight units. API Gateway extracts this data and passes it in HTTP X-Weight-Units header,
to the correct service.

### Kafka

To decouple notification service from the rest of the system kafka is used as a message broker. This way
system is completely independent of notification service. All notifications to user such as account activation
or password reset go through kafka.

#### Run Kafka broker

```shell
  docker run -d --name broker -p 9092:9092  apache/kafka:latest
```

## Technologies used
 - Kotlin
 - Spring Boot
 - Kafka
 - H2 Databases