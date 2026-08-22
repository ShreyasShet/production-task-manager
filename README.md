# Production Task Manager

A production task management REST API built with Java and Spring Boot.

## Status
Under Development — actively migrating from a raw Java HTTP server implementation to Spring Boot.

## Overview
This project started as a Task Management REST API built on a raw `com.sun.net.httpserver` implementation, using a layered architecture (HTTP handler → service → in-memory repository) with Jackson for JSON serialization. It is now being migrated to Spring Boot to bring it in line with production-grade practices — dependency injection, a proper web layer, and a path toward containerization and observability.

## Migration Progress
The migration is being carried out in a structured format:

- [ ] Building out `TaskController` (in progress)
- [ ] Service layer refactor for Spring
- [ ] Repository layer (moving off in-memory storage)
- [ ] Validation & error handling
- [ ] Testing (unit + integration)

## Tech Stack

### Implemented
- Java 21
- Spring Boot 4.1.0

### Planned
- PostgreSQL
- Docker
- Docker Compose
- Nginx
- GitHub Actions (CI/CD)
- AWS
- Prometheus
- Grafana

## Architecture
The original raw-HttpServer version followed this same layered pattern; the Spring Boot migration preserves the separation of concerns while replacing manual request routing and DI with Spring's container.

## Roadmap
1. Complete `TaskController` and core CRUD endpoints
2. Wire up PostgreSQL persistence
3. Containerize with Docker + Docker Compose
4. Set up CI/CD via GitHub Actions
5. Add Prometheus metrics and Grafana dashboards
6. Deploy to AWS

## Getting Started
_Instructions coming soon as the Spring Boot migration stabilizes._

## License
_Add license here._
