# ClearPass Asset Management Service

ClearPass is a Spring Boot-based Asset Management microservice. It provides CRUD operations for managing organizational assets using a Flyway-managed PostgreSQL database.

## 🧰 Tech Stack

- Java 17
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Flyway (DB migrations)
- Maven

## 🚀 Features

- Create, Read, Update, Delete assets
- Database schema versioning via Flyway
- RESTful API design
- Modular architecture (domain, service, controller, repository layers)

## 📦 Getting Started

### Prerequisites

- Java 17+
- Maven
- PostgreSQL

### Clone the Repository

```bash
git clone https://github.com/Seymour-creates/clearpass_asm.git
cd clearpass_asm
```

### Configure Database

Update `application.properties` or `application.yml`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/clearpass
spring.datasource.username=your_db_username
spring.datasource.password=your_db_password
```

### Run the App

```bash
mvn spring-boot:run
```

### API Endpoints (sample)

| Method | Endpoint         | Description           |
|--------|------------------|-----------------------|
| GET    | /api/assets      | List all assets       |
| POST   | /api/assets      | Create a new asset    |
| PUT    | /api/assets/{id} | Update an existing    |
| DELETE | /api/assets/{id} | Delete an asset       |

## 🛠️ To-Do

- [ ] Global exception handler
- [ ] Authentication & Authorization
- [ ] Input validation and DTOs
- [ ] Unit & integration tests
- [ ] Swagger/OpenAPI documentation
