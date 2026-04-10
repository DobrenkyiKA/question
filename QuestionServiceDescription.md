# 📖 Question Microservice Description

The **Question Microservice** is the core component of the Interview Preparation & Knowledge System. It manages the central knowledge model, including topics, questions, and their various formats.

---

## 🎯 Business Perspective & Responsibilities

The microservice is responsible for the **Content Lifecycle and Organization**. It provides the foundation for both content creation (via Admin Backoffice) and content consumption (via Learn and Quiz modes).

### Key Responsibilities:
- **Knowledge Organization**: Managing a hierarchical tree of topics (e.g., Java -> Collections -> List).
- **Question Management**: Handling the "Single Question = One Concept" model.
- **Multi-Format Support**: Supporting both **Interview** (Short/Long Answers) and **Quiz** (Multiple Choice) formats within the same question entity.
- **Categorization**: Managing difficulty levels (EASY, MEDIUM, HARD) and flexible labels (tags).
- **Filtering & Selection**: Providing advanced filtering logic based on topics, difficulty, formats, and labels to power different user flows.

---

## 🛠 Technology Stack

The microservice is built using modern, robust technologies:

- **Language**: [Kotlin](https://kotlinlang.org/) (JVM 25)
- **Framework**: [Spring Boot 4.0.3](https://spring.io/projects/spring-boot)
- **Persistence**: 
    - [Spring Data JPA](https://spring.io/projects/spring-data-jpa) (Hibernate)
    - [PostgreSQL](https://www.postgresql.org/) (Database)
    - [Liquibase](https://www.liquibase.org/) (Database Migrations)
- **Security**: 
    - [Spring Security](https://spring.io/projects/spring-security)
    - OAuth2 Resource Server (JWT validation)
- **API**: Spring Web (MVC)
- **Build Tool**: Gradle (Kotlin DSL)

---

## 🏗 Architecture

The microservice follows a **layered architecture** with elements of **Domain-Driven Design (DDD)** and **Hexagonal (Ports and Adapters)** principles.

### Package Structure:
- `com.kdob.piq.content.domain`: Contains core business logic, entities (`Question`, `Topic`), and repository interfaces (Ports). It is free from infrastructure dependencies.
- `com.kdob.piq.content.application`: Contains application services that orchestrate business logic and handle use cases (e.g., `QuestionQueryService`, `TopicService`).
- `com.kdob.piq.content.infrastructure`: Contains technical details (Adapters):
    - `persistence`: JPA implementations of repositories, entity mappings, and Liquibase scripts.
    - `web`: REST controllers, DTOs, and request/response mapping.
    - `config`: Spring Boot configurations (Security, JPA, JWT).

### Key Design Patterns:
- **Repository Pattern**: Abstracting data access behind interfaces in the domain layer.
- **DTO Pattern**: Using dedicated Data Transfer Objects for API requests and responses to decouple the internal domain from the external API.
- **Mapper Pattern**: Explicit mapping between Domain Entities, Persistence Entities, and API DTOs.
- **Specification Pattern**: Used for flexible and reusable database queries (filtering).
