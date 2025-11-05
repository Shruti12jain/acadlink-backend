# 🧠 AcadLink Backend — Project Structure Guide

This document describes the **folder structure**, **conventions**, and **purpose** of each component in the backend codebase.  
The project follows a **modular, domain-driven design (DDD)** approach to ensure scalability, testability, and maintainability.

---

## 📂 Folder Structure

```
src/main/
├── java/
│   └── com/
│       └── acadlink/
│           └── backend/
│               ├── AcadLinkBackendApplication.java
│               ├── config/
│               │   └── SecurityConfig.java
│               ├── health/
│               │   ├── controller/
│               │   │   └── HealthController.java
│               │   ├── model/
│               │   │   ├── HealthResponse.java
│               │   │   └── HealthStatus.java
│               │   └── service/
│               │       └── HealthService.java
│               ├── university/
│               │   ├── controller/
│               │   ├── dto/
│               │   ├── entity/
│               │   ├── exception/
│               │   ├── model/
│               │   ├── repository/
│               │   ├── security/
│               │   └── service/
│               └── utils/
└── resources/
    ├── application.yml
    ├── db/
    │   └── migration/
    │       └── V1__init_schema.sql
    ├── static/
    └── templates/
```

---

## 🏗️ Root Components

### `AcadLinkBackendApplication.java`
- The **entry point** of the Spring Boot application.
- Bootstraps the context and scans all sub-packages for components.

---

## ⚙️ Configuration Layer — `config/`
Contains **global configuration** classes for the entire application.

| File | Purpose |
|------|----------|
| `SecurityConfig.java` | Defines Spring Security rules, role-based access, and filters. |
| *(Future)* `WebConfig.java` | Add CORS rules, interceptors, or custom web configurations. |
| *(Future)* `SwaggerConfig.java` | API documentation (Swagger/OpenAPI). |
| *(Future)* `AppConfig.java` | Common beans (e.g., `PasswordEncoder`, `ModelMapper`, etc.). |

🧩 **Note:** Configuration classes should be *stateless* and contain no business logic.

---

## ❤️ Health Module — `health/`
Provides system-level monitoring routes, e.g., `/health/ping` or `/health/db`.

| Folder | Description |
|---------|--------------|
| `controller/` | Defines REST endpoints for health checks. |
| `service/` | Contains logic for verifying service and DB health. |
| `model/` | Defines data structures like `HealthResponse` and enums like `HealthStatus`. |

📘 Health routes are **public** and **unauthenticated** — used by uptime monitors and CI/CD probes.

---

## 🏛️ University Module — `university/`
Encapsulates all logic related to the University domain.  
Every feature domain (like `professor`, `student`, `project`) will follow the same internal structure.

| Folder | Purpose |
|--------|----------|
| `controller/` | REST API layer (receives HTTP requests, returns DTOs). |
| `dto/` | Data Transfer Objects (Request/Response classes). |
| `entity/` | JPA entities representing database tables. |
| `exception/` | Custom domain-specific exceptions. |
| `model/` | Non-persistent domain models and enums. |
| `repository/` | Spring Data repositories (interfaces extending `JpaRepository`). |
| `security/` | Security logic specific to the domain (if needed). |
| `service/` | Business logic, validation, and orchestration layer. |

📘 Each domain is self-contained — this helps with modularity and future microservice extraction.

---

## 🧰 Utilities — `utils/`
Contains **generic helper functions** or **utility classes** that can be used across modules.

Examples:
- `JwtUtils.java` — for token generation/validation.
- `DateUtils.java` — for date formatting and parsing.
- `ResponseUtils.java` — for standard API responses.

📘 Avoid putting business logic here — only reusable helpers.

---

## 🗂️ Resources — `src/main/resources/`
Contains configuration and static resources used at runtime.

| Folder/File | Purpose |
|--------------|----------|
| `application.yml` | Centralized configuration (database, Flyway, security, etc.). |
| `db/migration/` | Flyway SQL migrations (e.g. `V1__init_schema.sql`, `V2__add_table.sql`). |
| `static/` | Static assets (CSS, JS, images) — rarely used in API projects. |
| `templates/` | Thymeleaf or Freemarker templates (optional). |

---

## 🧩 Future Domains
As the system grows, new feature modules (like `professor/`, `student/`, `project/`) should mirror the same structure as `university/`:

```
professor/
  ├── controller/
  ├── dto/
  ├── entity/
  ├── service/
  ├── repository/
  ├── model/
  ├── exception/
```

This ensures consistency and easy onboarding for new developers.

---

## ✅ Summary

| Layer | Purpose | Example |
|--------|----------|----------|
| Configuration | Global app settings | `SecurityConfig.java` |
| Domain | Feature-specific logic | `university/`, `student/` |
| Service | Business logic | `HealthService`, `UniversityService` |
| Repository | Data access layer | `UniversityRepository` |
| Model | DTOs, Entities, Enums | `HealthResponse`, `HealthStatus` |
| Utility | Shared helpers | `JwtUtils`, `DateUtils` |
| Resources | App configuration | `application.yml`, `migrations` |

---

### 💡 Design Philosophy
- **Modular**: Each domain is self-contained.
- **Scalable**: Easily extend to new domains or microservices.
- **Testable**: Clear separation between layers simplifies unit and integration testing.
- **Maintainable**: Consistent structure reduces technical debt over time.

---

**Author:** Paras Jain  
**Project:** AcadLink — University Collaboration & Management Platform  
**Framework:** Spring Boot 3.5.6  
**Database:** PostgreSQL 17 + Flyway Migrations
