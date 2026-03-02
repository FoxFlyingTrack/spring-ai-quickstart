# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Spring AI Quick Start project using Spring Boot 3.4.1 and Java 21. Integrates with Qianwen (通义千问) via DashScope.

## Build Commands

```bash
mvn clean package    # Build
mvn spring-boot:run # Run (port 9001)
mvn test             # Run tests
```

## Architecture

- `Application.java` - Main class
- `application.yml` - Config (port 9001, DashScope API)
- `ChatClientConfig.java` - Multiple ChatClient beans (default, creative, precise, pirate)
- `AiController.java` - `/ai/*` endpoints
- `ToolController.java` - `/tool/*` endpoints
- `MultiModelService.java` - Multi-model via `mutate()`
- `tools/` - Tool Calling implementations

## API Endpoints

| Endpoint | Description |
|----------|-------------|
| `GET /ai/chat` | Basic chat |
| `GET /ai/chat/creative` | temperature=0.9 |
| `GET /ai/chat/precise` | temperature=0.2 |
| `GET /ai/chat/entity` | Structured output |
| `GET /ai/chat/films` | Streaming |
| `GET /tool/datetime` | @Tool demo |
| `GET /tool/weather` | Function demo |
| `GET /tool/all` | Combined tools |

## Spring AI Core Concepts

### ChatClient
```java
chatClient.prompt().user("msg").call().content();
```

### Structured Output
```java
chatClient.prompt().user("...").call().entity(ActorFilms.class);
```

### Tool Calling
- **@Tool**: Simple methods with `@Tool(description="...")` and `@ToolParam`
- **Function**: Implements `Function<Request, Response>`

```java
// @Tool way
@Tool(description = "Get current date")
String getDate() { ... }

// Function way
class WeatherService implements Function<WeatherRequest, WeatherResponse> {
    record WeatherRequest(String city) {}
    @Override public WeatherResponse apply(WeatherRequest r) { ... }
}
```

### Configuration
- `temperature`: 0.0-1.0 (randomness)
- `system`: Define AI role/behavior
- `mutate()`: Derive new model configs

## Official Docs
- Main: https://docs.spring.io/spring-ai/reference/index.html
- Tools: https://docs.spring.io/spring-ai/reference/api/tools.html