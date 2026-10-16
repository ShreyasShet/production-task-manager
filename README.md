# Production Task Manager

[![Java](https://img.shields.io/badge/Java-17-orange)]()
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.0-brightgreen)]()
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-persistent-blue)]()
[![Status](https://img.shields.io/badge/status-in%20development-yellow)]()

A task management REST API built with Java, Spring Boot, and PostgreSQL. The project is a deliberate exercise in backend engineering: each layer (HTTP, framework, storage) was built, stress-tested, and understood before the next one was added, with the goal of ending up with a service that is containerized, tested, deployed, and observable.

## How it got here

The project started as a bare HTTP server on `com.sun.net.httpserver`, with manual routing, hand-wired dependencies, and an in-memory list of tasks. That version proved out the domain and the API shape. It was then migrated to Spring Boot, which replaced manual routing and wiring with declarative controllers and dependency injection while keeping the original layered design intact.

Storage went through the same progression. Tasks lived in memory first, then in a file, and the file-backed version was stress-tested until its limits were clear: concurrent writes, slow lookups, no atomicity on crashes, no enforced consistency, and no safe way for multiple clients to share it. Each of those failures is addressed by a specific PostgreSQL mechanism, which is why tasks now live in a real database.

## Architecture

Requests flow through a controller, then the business logic, then a Spring Data JPA repository backed by PostgreSQL. Spring's IoC container manages object lifecycles, routing is declarative through `@RequestMapping`, and validation errors are handled centrally with `@RestControllerAdvice`.

Persistence is handled by Hibernate on top of a HikariCP connection pool. The `Task` entity uses an identity-generated integer key, with `NOT NULL` constraints on `title` and `createdAt` and a `CHECK` constraint limiting `status` to `planned`, `in-progress`, or `done`. These database-level rules mirror the `@Valid` checks in the API layer, so bad data is rejected at both boundaries. The `createdAt` timestamp is set automatically on insert.

## Tech Stack

**Implemented**
- Java 17
- Spring Boot 4.1.0

**Planned**
- PostgreSQL — persistent storage
- Docker / Docker Compose — containerization
- Nginx — reverse proxy
- GitHub Actions — CI/CD
- Prometheus + Grafana — metrics & dashboards
- AWS — deployment

## API

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/tasks` | List all tasks |
| `GET` | `/tasks/{id}` | Fetch a single task |
| `POST` | `/tasks` | Create a task |
| `PUT` | `/tasks/{id}` | Update a task |
| `DELETE` | `/tasks/{id}` | Delete a task |

A task has a `title` (required), an optional `description`, a `status` (`planned`, `in-progress`, or `done`), and a server-generated `id` and `createdAt`. Clients cannot supply the generated fields.

## Getting started

### Prerequisites

You will need JDK 17, Maven 3.9+ and a PostgreSQL instance running locally.

### 1. Clone the repository

```bash
git clone <repo-url>
cd production-task-manager
```

### 2. Create the database

```bash
createdb taskmanager
```

On first startup, Hibernate creates the `task` table automatically, including its constraints, so there is no schema to apply by hand. Data persists across restarts.

### 3. Configure the connection

The application reads its datasource settings from the standard Spring environment variables. If your local PostgreSQL runs on the default port with a passwordless user, the defaults may work as they are. Otherwise, set these before starting:

```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/taskmanager
export SPRING_DATASOURCE_USERNAME=your_user
export SPRING_DATASOURCE_PASSWORD=your_password
```

### 4. Run the application

```bash
./mvnw spring-boot:run
```

The API starts on `http://localhost:8080`.

### 5. Try it out

```bash
# Create a task
curl -X POST http://localhost:8080/tasks \
  -H "Content-Type: application/json" \
  -d '{"title": "Write the README", "description": "Cover Postgres setup", "status": "planned"}'

# List all tasks
curl http://localhost:8080/tasks
```

Restart the application and list the tasks again to confirm they survived.

## Project status

The core service is complete: Spring Boot migration, CRUD endpoints, validation, centralized error handling, and PostgreSQL persistence through JPA and Hibernate. What remains is the work that turns a working service into a production one.

| Phase | Status |
|---|---|
| Raw `HttpServer` → Spring Boot migration | ✅ Complete |
| `TaskController` + core CRUD | ✅ Complete |
| Service layer (Spring-managed) | ✅ Complete |
| Validation & error handling | ✅ Complete |
| PostgreSQL persistence | ✅ Complete |
| Testing (unit + integration) | ⏳ Planned |
| Containerization (Docker) | ⏳ Planned |
| CI/CD (GitHub Actions) | ⏳ Planned |
| Observability (Prometheus + Grafana) | ⏳ Planned |
| Deployment (AWS) | ⏳ Planned |

## Roadmap

The next milestone is a proper test suite, covering the service logic with unit tests and the HTTP-to-database path with integration tests against a real PostgreSQL instance. After that, the application and its database will be containerized with Docker Compose, which also replaces the manual local Postgres setup above. A GitHub Actions pipeline will then build and test every change, Prometheus and Grafana will add metrics and dashboards, and the final step is deploying the whole stack to AWS behind an Nginx reverse proxy.

1. ✅ Migrate off raw `com.sun.net.httpserver` to Spring Boot
2. ✅ Implement `TaskController` and core CRUD endpoints
3. ✅ Wire up PostgreSQL persistence (JPA/Hibernate)
4. ⏳ Add request validation and centralized error handling
5. ⏳ Write unit and integration tests
6. ⏳ Containerize with Docker + Docker Compose
7. ⏳ Set up CI/CD via GitHub Actions
8. ⏳ Add Prometheus metrics and Grafana dashboards
9. ⏳ Deploy to AWS