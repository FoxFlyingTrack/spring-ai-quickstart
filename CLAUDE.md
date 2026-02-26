# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a minimal Spring AI Quick Start project using Spring Boot 3.4.1 and Java 21. It's a starter template for building AI-powered applications with Spring AI.

## Build Commands

```bash
# Build the project
./mvnw clean package

# Run the application
./mvnw spring-boot:run

# Run tests
./mvnw test

# Run a single test class
./mvnw test -Dtest=ApplicationTests

# Run with specific profile
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

## Architecture

This is a standard Spring Boot project with the following structure:
- **Entry Point**: `src/main/java/com/example/demo/Application.java` - `@SpringBootApplication` main class
- **Configuration**: `src/main/resources/application.yml` - Spring configuration (server port 8080)
- **Tests**: `src/test/java/com/example/demo/ApplicationTests.java` - Spring Boot context test

## Dependencies

- Spring Boot 3.4.1 (starter)
- Spring Boot Test
- Java 21

## Development Notes

- When adding Spring AI dependencies, add them to `pom.xml` following the Spring Boot BOM pattern
- Configuration can be managed via `application.yml` or environment variables
- The project follows standard Spring Boot conventions for controllers, services, and repositories