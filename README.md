# Prices Microservice

## Project Overview

A Spring Boot microservice built with hexagonal architecture principles.
This microservice is responsible for managing pricing data within the system. It provides functionality for reading price information, with caching strategies, and resilience patterns to ensure reliable operation.

## Technical Stack

This project leverages a modern Java technology stack including:

Spring Boot 3, JPA/Hibernate, H2 Database, OpenAPI/Swagger UI, Resilience4j, Caffeine Cache, Lombok, MapStruct, Spring Actuator, and JaCoCo for test coverage reporting.

## Module Structure

The application follows a hexagonal architecture pattern, divided into the following modules:

- **domain**: Core domain models and business logic interfaces
- **application**: Use cases and business logic implementation with caching and resilience patterns
- **infrastructure**: Database and external services implementation
- **rest-port-in**: REST API layer with OpenAPI generation
- **boot**: Application bootstrap and configuration

## Features

- Hexagonal Architecture with clear module boundaries
- RESTful API with OpenAPI/Swagger documentation
- Comprehensive test coverage across all modules
- Automated API code generation
- Built-in caching and resilience patterns
- Health monitoring and metrics
- In-memory database for development

## Build and Run

Build the project:
```bash
mvn clean install
```

Run the application:
```bash
mvn -pl boot spring-boot:run
```

Run the application with Docker:
```bash
docker-compose up --build
```

Generate JaCoCo reporting:
```bash
mvn clean verify
```

Access the JaCoCo report:
```bash
open boot/target/site/jacoco-aggregate/index.html
```

Access the API documentation:
```
http://localhost:8080/swagger-ui.html
```

Access actuator metrics:
```
http://localhost:8080/actuator/metrics
```

Access actuator cache:
```
http://localhost:8080/actuator/caches
```

## Reasons of Dockerizing

- **Portability & Consistency**: By using Docker, you encapsulate your application and its dependencies inside a container that will work the same way in any environment.
- **Easy Deployment**: Many platforms like AWS, Azure, and Google Cloud offer native support for Docker, making deployments easier without manual infrastructure setup.

## Next steps

- Deploying in the cloud