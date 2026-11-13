# YUVA Intern — Task Management System (Weeks 1–5)

Integrated Java 17 / Spring Boot / MySQL reference project for the five-week Junior Spring Boot Developer internship.

| Week | Task | Included content |
|---|---|---|
| 1 | Project design | `documentation/Week1_Project_Design.md` |
| 2 | REST API and CRUD | Task/User entities, repositories, service, controller, API guide |
| 3 | API integration and automated testing | DummyJSON integration, DTOs, exception handling, JUnit/Mockito tests |
| 4 | Debugging and performance optimization | Pagination, sorting, status filtering, indexes, regression tests |
| 5 | Security and data protection | Registration/login, BCrypt, JWT, USER/ADMIN access, task ownership checks |

## Technology
Java 17, Spring Boot 3.5.7, Spring Web, Spring Data JPA, Spring Security, Bean Validation, MySQL, JUnit 5, Mockito, MockMvc, H2 and JJWT 0.12.6.

## Prerequisites
- JDK 17+
- Maven 3.9+
- MySQL 8+

## Configure database and secrets
Set environment variables `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` and `JWT_SECRET` (at least 32 UTF-8 bytes; use a strong random value). Defaults in `application.properties` are development-only. Never commit production secrets.

## Run automated tests
```bash
mvn clean test
```
Tests use an H2 in-memory database profile. External API calls in unit tests are mocked, so tests do not depend on the live provider.

## Run application
```bash
mvn spring-boot:run
```

See `documentation/Week2_REST_API_and_CRUD.md` for CRUD examples.

## Important integration warning
This ZIP is a standalone reference implementation using package `com.yuvaintern.taskmanagement`. The existing GitHub repository may use different package names, class names, dependency versions or schema. Compare and merge rather than blindly overwriting the existing repository.

## Submission evidence
Capture genuine local evidence after running the project: Maven test output, MySQL persistence, Postman CRUD results, external API response and failure handling, pagination/sorting, measured before/after performance metrics, registration/login, 401 and 403 responses, and task ownership checks. Do not claim test success or performance improvement until measured locally.
