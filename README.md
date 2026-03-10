# Transparence API

REST API developed in Java with Spring Boot for managing users, dependents, contracts, expenses and incomes.

![Status](https://img.shields.io/badge/status-online-success)
![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/SpringBoot-3.x-brightgreen)
![Deploy](https://img.shields.io/badge/deploy-Railway-purple)

## 🌐 Production API

The application is available in a cloud environment and can be tested in real time.

🔗 https://transparence-api-production.up.railway.app

📄 Swagger:  
🔗 https://transparence-api-production.up.railway.app/swagger-ui.html

The API is deployed in a production environment using Railway, with a MySQL cloud database and configuration through environment variables.

---

## 🛠️ Technologies Used

* Java 17+
* Spring Boot
* Spring Web
* Spring Data JPA
* Hibernate
* Spring Security
* JWT Authentication
* Swagger / OpenAPI
* Flyway (Database Migration)
* Maven
* JUnit / Mockito
* Docker
* MySQL
* H2 Database

---

## 🏗️ Architecture

The project follows a layered architecture, separating responsibilities clearly.

### 📦 Layers

#### Controller

* Responsible for receiving and responding to HTTP requests
* Works exclusively with DTOs
* Documented using Swagger/OpenAPI

#### DTO (Data Transfer Object)

* RequestDTO → input data
* ResponseDTO → output data
* Prevents direct exposure of JPA entities

#### Service

* Contains the business rules
* Controls states and validations
* Uses separated interfaces and implementations
* Uses `@Transactional` for transaction control
* Includes logs for operation traceability

#### Repository

* Data access layer using Spring Data JPA

#### Entity

* Represents the domain of the application
* Mapped with JPA/Hibernate

---

## 📌 Features

### 👤 User

* Create user
* Update user
* Find user by ID
* Delete user

### 👥 Dependent

* Create dependent
* Update dependent
* Find dependent by ID
* Delete dependent

### 📄 Contract

* Create contract
* Find contract by ID
* Suspend contract
* Reactivate contract
* Close contract
* Delete contract

The contract has status control (`ACTIVE`, `SUSPENDED`, `CLOSED`) and its transitions are controlled exclusively in the service layer.

### 💰 Expense

* Create expense
* Update expense
* Find expense by ID
* List expenses
* Delete expense

Expenses can only be created, updated or deleted if the contract is active.

### 💵 Income

* Create income
* Update income
* Find income by ID
* List incomes
* Delete income

Incomes also depend on the contract status.

---

## 🔐 Authentication and Security

The API uses **JWT (JSON Web Token)** for authentication.

Flow:

1. User performs login
2. API returns a JWT token
3. The token must be sent in the header of protected requests

Authorization: Bearer {token}

Swagger also supports authentication using a token to test protected endpoints.

---

## 🔄 Transaction Management

The service layer uses `@Transactional` to guarantee integrity of operations.

* Write methods use full transactions
* Read methods use `readOnly = true`

---

## ⚠️ Exception Handling

The application includes a `GlobalExceptionHandler` responsible for:

* Standardizing error responses
* Returning the correct HTTP status codes
* Adding traceability to error responses

---

## 📊 Observability and Logs

* Logs implemented in the service layer
* Correlation ID for request tracing
* Better error traceability

---

## 🗄️ Database

* H2 support for development environment
* MySQL support for production environment
* Database versioning using **Flyway**
* Index creation for query optimization
* Constraints to ensure data integrity

---

## ⚙️ Execution Profiles

The application uses profiles to separate environments:

* `h2` → development
* `mysql` → production

Database switching occurs only through configuration, without the need to change code.

---

## 🐳 Docker

The project includes `docker-compose` to start the full environment:

docker-compose up -d

Services:

* MySQL
* Spring Boot Application

---

## ☁️ Deploy

Project prepared for cloud deployment (Railway), including:

* MySQL driver configuration
* Environment variable configuration
* Production profile adjustments
* Automatic Flyway migrations on startup

---

## 🧪 Tests

Unit tests were implemented for the service layer, ensuring:

* Validation of business rules
* Reliability of operations
* Ease of maintenance

---

## 📄 API Documentation (Swagger)

After starting the application, access:

http://localhost:8080/swagger-ui.html

Or in production:

https://transparence-api-production.up.railway.app/swagger-ui.html

The documentation is organized by API resources:

* Auth
* Users
* Dependents
* Contracts
* Incomes
* Expenses

---

## ▶️ How to Run the Project

### Requirements

* Java 17+
* Maven
* Docker (optional)

### Running with Maven

mvn spring-boot:run

### Running with Docker

docker-compose up -d

---

## 🧠 Applied Concepts

* Layered architecture
* DTO Pattern
* Separation between API contract and domain
* Business rules centralized in the service layer
* Global exception handling
* Logs and traceability
* JWT authentication
* Database versioning with Flyway
* Transaction control with Spring
* Unit testing
* Multiple environment profiles
* Cloud deployment preparation

---

## 📌 Project Status

🚀 Project completed as a backend architecture study using Spring Boot and best development practices.

---

## 👨‍💻 Author

**Gustavo Batista**

Project developed focusing on practical learning, clean architecture and backend development best practices.