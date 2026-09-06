# Production Task Manager

[![Java](https://img.shields.io/badge/Java-21-orange)]()
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.0-brightgreen)]()
[![Status](https://img.shields.io/badge/status-in%20development-yellow)]()

A production-grade task management REST API, built with Java and Spring Boot — designed around the same architectural discipline you'd expect in a real backend service: clean separation of concerns, dependency injection, and a clear path toward containerization, CI/CD, and observability.

## Overview

This project began as a Task Management REST API built directly on `com.sun.net.httpserver`, with a layered architecture (HTTP handler → service → in-memory repository) and Jackson for JSON serialization. That raw implementation proved out the core domain logic and API design.

It has since been **migrated to Spring Boot**, replacing manual request routing and hand-rolled dependency wiring with Spring's container — while preserving the original layered design. The project is now moving toward full production readiness: persistent storage, containerization, CI/CD, and monitoring.

## Current Status

| Phase | Status |
|---|---|
| Raw `HttpServer` → Spring Boot migration | ✅ Complete |
| `TaskController` + core CRUD | ✅ Complete |
| Service layer (Spring-managed) | ✅ Complete |
| Validation & error handling | ✅ Complete |
| PostgreSQL persistence | 🔄 In progress |
| Testing (unit + integration) | ⏳ Planned |
| Containerization (Docker) | ⏳ Planned |
| CI/CD (GitHub Actions) | ⏳ Planned |
| Observability (Prometheus + Grafana) | ⏳ Planned |
| Deployment (AWS) | ⏳ Planned |

## Architecture

The layered structure carried over from the original raw-`HttpServer` implementation. What changed in the Spring Boot migration is *how* those layers are wired together — Spring's IoC container now manages object lifecycles and dependencies instead of manual instantiation, and routing is declarative (`@RequestMapping`) instead of handler-based.

## Tech Stack

**Implemented**
- Java 21
- Spring Boot 4.1.0

**Planned**
- PostgreSQL — persistent storage
- Docker / Docker Compose — containerization
- Nginx — reverse proxy
- GitHub Actions — CI/CD
- Prometheus + Grafana — metrics & dashboards
- AWS — deployment

## Roadmap

1. ✅ Migrate off raw `com.sun.net.httpserver` to Spring Boot
2. ✅ Implement `TaskController` and core CRUD endpoints
3. 🔄 Wire up PostgreSQL persistence (JPA/Hibernate)
4. ⏳ Add request validation and centralized error handling
5. ⏳ Write unit and integration tests
6. ⏳ Containerize with Docker + Docker Compose
7. ⏳ Set up CI/CD via GitHub Actions
8. ⏳ Add Prometheus metrics and Grafana dashboards
9. ⏳ Deploy to AWS

## Getting Started

```bash
# Clone the repo
git clone <repo-url>
cd production-task-manager

# Run the application
./mvnw spring-boot:run
```

> Full setup instructions (including database configuration) coming soon as PostgreSQL persistence lands.

## License

_Add license here._