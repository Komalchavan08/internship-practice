# Contract Management System

Legal Contract Document Modification System — internship project.

Allows authorized users to upload, review, modify, version-control, and approve
legal contract documents, with full audit/history tracking.

## Tech Stack
- Java 17
- Spring Boot 3.3.4
- Spring Web (REST APIs)
- Spring Data JPA / Hibernate
- Spring Security (added, wired up in a later task)
- PostgreSQL
- Maven
- Lombok
- Springdoc OpenAPI (Swagger UI)

## Project Structure
```
src/main/java/com/internship/contractmanagement/
 ├── controller/   → REST API endpoints
 ├── service/      → Business logic
 ├── repository/   → Spring Data JPA repositories
 ├── entity/       → JPA entities (DB tables)
 ├── dto/          → Request/response objects
 ├── config/       → Security & app configuration
 └── exception/    → Custom exceptions & global error handling
```

## Prerequisites
- Java 17+
- Maven 3.6+
- PostgreSQL 14+ installed and running

## Database Setup

1. Open `psql` or a GUI tool (pgAdmin) and create the database:
   ```sql
   CREATE DATABASE contract_management_db;
   ```
2. Update `src/main/resources/application.properties` with your local
   PostgreSQL username and password:
   ```
   spring.datasource.username=postgres
   spring.datasource.password=your_postgres_password
   ```

## Running the Application

```bash
mvn clean install
mvn spring-boot:run
```

The app starts on **http://localhost:8081**

Swagger UI (API docs): **http://localhost:8081/swagger-ui.html**

## Status
- [x] Project setup & folder structure
- [x] Database connected
- [ ] User & Auth module
- [ ] Contract upload/CRUD
- [ ] Version control
- [ ] Approval workflow
- [ ] Audit logging
