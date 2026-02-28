# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Spring AI Quick Start project using Spring Boot 3.4.1 and Java 21. It's a template for building AI-powered applications with Spring AI, integrating with Qianwen (通义千问) via DashScope.

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
```

## Architecture

Standard Spring Boot project with AI capabilities:

- **Entry Point**: `src/main/java/com/example/demo/Application.java` - `@SpringBootApplication` main class
- **Configuration**: `src/main/resources/application.yml` - Server runs on port 9001, AI config for DashScope/Qianwen
- **ChatClient Configuration**: `src/main/java/com/example/demo/config/ChatClientConfig.java` - Defines multiple `ChatClient` beans with different behaviors (default, creative, precise, pirate)
- **REST Controller**: `src/main/java/com/example/demo/controller/AiController.java` - Exposes AI endpoints under `/ai/*`
- **Service**: `src/main/java/com/example/demo/service/MultiModelService.java` - Demonstrates multi-model support using `mutate()` to create derived API clients
- **POJO**: `src/main/java/com/example/demo/pojo/ActorFilms.java` - Record for structured AI output

### API Endpoints

- `GET /ai/chat` - Basic chat with default ChatClient, includes logging advisor
- `GET /ai/chat/creative` - Chat with higher temperature (0.9)
- `GET /ai/chat/precise` - Chat with lower temperature (0.2)
- `GET /ai/chat/simple` - Pirate-style chat response
- `GET /ai/chat/entity` - Structured entity output (ActorFilms) with native structured output
- `GET /ai/chat/entityList` - List of structured entities using ParameterizedTypeReference
- `GET /ai/chat/films` - Streaming response with BeanOutputConverter

## Spring AI Core Concepts

This project demonstrates key Spring AI features:

### 1. ChatClient API
The main fluent API for interacting with AI models. Built using the Builder pattern.

```java
chatClient.prompt()
    .user("message")
    .call()
    .content();
```

### 2. Structured Output
Maps AI responses directly to Java POJOs using `.entity()` method.

```java
chatClient.prompt()
    .user("Generate actor filmography")
    .call()
    .entity(ActorFilms.class);
```

### 3. Streaming
Use `.stream()` for real-time responses with `Flux<String>`.

### 4. ChatClient Configuration
Create different ChatClient beans with varying:
- `temperature` - randomness level (0.0-1.0)
- `system` prompts - define AI behavior/role
- Model options

### 5. Model Mutation
Use `mutate()` to derive new model configurations from existing ones, useful for:
- Switching between different model providers
- Dynamic configuration changes

### 6. Advisors
Enhance ChatClient with additional functionality:
- `SimpleLoggerAdvisor` - logging
- Chat memory advisors
- Custom function calling

## Dependencies

- Spring Boot 3.4.1
- Spring AI 1.1.2 (with `spring-ai-starter-model-openai`)
- Spring Web
- Lombok
- Java 21

## Configuration

AI configuration in `application.yml`:
- Uses DashScope API (`https://dashscope.aliyuncs.com/compatible-mode`)
- Default model: `qwen3.5-plus`
- API key configured via `spring.ai.openai.api-key`

## Development Notes

- Add Spring AI dependencies to `pom.xml` using the `spring-ai-bom` BOM pattern
- Configuration can be managed via `application.yml` or environment variables
- ChatClient beans are created in `ChatClientConfig` with different `OpenAiChatOptions` (temperature, system prompts)
- Use `@Qualifier` to inject specific ChatClient beans
- For structured output, use `BeanOutputConverter` or `.entity()` method with `ParameterizedTypeReference`
- Enable native structured output with `.advisors(a -> a.param("enable_native_structured_output", true))`

## Official Documentation

- Main Docs: https://docs.spring.io/spring-ai/reference/index.html
- ChatClient: https://docs.spring.io/spring-ai/reference/api/chatclient.html
- Structured Output: https://docs.spring.io/spring-ai/reference/api/structured-output.html
- Streaming: https://docs.spring.io/spring-ai/reference/api/streaming.html
- Model Mutation: https://docs.spring.io/spring-ai/reference/model.html#_model_mutation