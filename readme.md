# CineBook 🎬

CineBook is a production-oriented movie theatre booking backend built with Java and Spring Boot.

The project provides REST APIs for managing movies, theatres, screens, seats, shows, users, authentication, and movie-ticket bookings. It uses JWT-based authentication with database-backed refresh-token rotation, role-based authorization, DTO validation, soft deletion, pagination, and transactional booking logic.

> **Status:** Actively under development  
> **Focus:** Backend engineering, security, transaction management, booking concurrency, testing, and production-oriented architecture.

---

## Features

### Authentication & Security

- User registration and login
- BCrypt password hashing
- JWT access tokens
- Database-backed refresh tokens
- Refresh-token rotation
- Refresh-token revocation
- HttpOnly refresh-token cookie support
- Stateless Spring Security configuration
- Role-based authorization
- Account enabled/disabled support
- Protected admin endpoints
- Swagger/OpenAPI JWT bearer authentication

### Movie Management

- Create movies
- Update movies
- Soft-delete movies
- Retrieve active movies
- Search movies by name
- Pagination
- Sorting
- Bean Validation

### Theatre Management

- Create theatres
- Update theatres
- Soft-delete theatres
- Retrieve active theatres
- Duplicate theatre detection based on name and city

### Screen Management

- Create screens under theatres
- Update screens
- Soft-delete screens
- Retrieve screens
- Screen-name uniqueness within a theatre
- Pagination and sorting

### Seat Management

- Create seats under screens
- Update seats
- Soft-delete seats
- Retrieve seats
- Retrieve seats by screen
- Seat number uniqueness within a screen
- Seat types:
    - Regular
    - Premium
    - Recliner

### Show Management

- Schedule movie shows
- Update scheduled shows
- Soft-delete shows
- Configure ticket prices
- Validate show start/end times
- Prevent overlapping shows on the same screen
- Validate active movie and screen
- Pagination and sorting

### Booking

- Book multiple seats for a show
- Prevent duplicate seats within a booking
- Validate seat ownership by screen
- Check existing booked seats
- Support `PENDING`, `CONFIRMED`, and `CANCELLED` booking states
- Calculate total booking amount
- Retrieve individual bookings
- Retrieve the authenticated user's bookings
- Paginated booking history
- Booking ownership validation
- Transactional booking creation
- Booking cancellation

---

## Architecture

CineBook follows a layered Spring Boot architecture.

```text
                        Client
                          │
                          ▼
                 ┌─────────────────┐
                 │  Spring Security │
                 │   JWT Filter     │
                 └────────┬────────┘
                          │
                          ▼
                 ┌─────────────────┐
                 │   Controllers   │
                 └────────┬────────┘
                          │
                          ▼
                 ┌─────────────────┐
                 │      DTOs       │
                 │ Validation      │
                 └────────┬────────┘
                          │
                          ▼
                 ┌─────────────────┐
                 │    Services     │
                 │ Business Logic  │
                 └────────┬────────┘
                          │
                          ▼
                 ┌─────────────────┐
                 │   Repositories  │
                 │   Spring Data   │
                 └────────┬────────┘
                          │
                          ▼
                 ┌─────────────────┐
                 │   PostgreSQL    │
                 └─────────────────┘
```

### Domain Model

```text
User
 │
 └── Booking
       │
       ├── Show
       │     ├── Movie
       │     └── Screen
       │            ├── Theatre
       │            └── Seat
       │
       └── BookingSeat
              │
              └── Seat
```

---

## Authentication Flow

### Login

```text
POST /api/auth/login
        │
        ▼
AuthenticationManager
        │
        ▼
UserDetailsService
        │
        ▼
UserRepository
        │
        ▼
Password Verification
        │
        ├───────────────┐
        ▼               ▼
 Access JWT       Refresh Token
                       │
                       ▼
                RefreshToken DB
                       │
                       ▼
                HttpOnly Cookie
```

### Refresh Token Rotation

```text
Refresh Token
      │
      ▼
Validate JWT
      │
      ▼
Extract JTI
      │
      ▼
Find persisted token
      │
      ▼
Check expiry/revocation/user
      │
      ▼
Revoke old token
      │
      ▼
Create new refresh token
      │
      ▼
Generate new access token
```

This allows the server to revoke refresh tokens instead of relying exclusively on JWT expiration.

---

## Booking Flow

```text
User
 │
 ▼
Select Show
 │
 ▼
Select Seats
 │
 ▼
POST /api/booking
 │
 ▼
Validate Show
 │
 ├── Movie active?
 ├── Screen active?
 └── Show active?
 │
 ▼
Validate Seats
 │
 ├── Exist?
 ├── Active?
 ├── Belong to Show Screen?
 └── Already booked?
 │
 ▼
Calculate Total
 │
 ▼
Create Booking
 │
 ▼
Create BookingSeat records
 │
 ▼
PENDING
```

The booking domain is intentionally being developed further toward a payment-aware lifecycle.

---

## API Endpoints

### Authentication

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/auth/register` | Register a user |
| POST | `/api/auth/login` | Authenticate and issue tokens |
| POST | `/api/auth/refresh` | Rotate refresh token and issue new access token |
| POST | `/api/auth/logout` | Revoke refresh token and clear cookie |

### Admin

| Method | Endpoint | Access |
|---|---|---|
| GET | `/api/admin` | ADMIN |

### Movies

| Method | Endpoint | Access |
|---|---|---|
| GET | `/api/movie` | Authenticated |
| GET | `/api/movie/{name}` | Authenticated |
| POST | `/api/movie` | ADMIN |
| PUT | `/api/movie/{id}` | ADMIN |
| DELETE | `/api/movie/{id}` | ADMIN |

### Theatres

| Method | Endpoint | Access |
|---|---|---|
| GET | `/api/theatre` | Authenticated |
| GET | `/api/theatre/{id}` | Authenticated |
| POST | `/api/theatre` | ADMIN |
| PUT | `/api/theatre/{id}` | ADMIN |
| DELETE | `/api/theatre/{id}` | ADMIN |

### Screens

| Method | Endpoint | Access |
|---|---|---|
| GET | `/api/screens` | Authenticated |
| GET | `/api/screens/{id}` | Authenticated |
| POST | `/api/screens` | ADMIN |
| PUT | `/api/screens/{id}` | ADMIN |
| DELETE | `/api/screens/{id}` | ADMIN |

### Seats

| Method | Endpoint | Access |
|---|---|---|
| GET | `/api/seats` | Authenticated |
| GET | `/api/seats/screen/{screenId}` | Authenticated |
| GET | `/api/seats/{id}` | Authenticated |
| POST | `/api/seats` | ADMIN |
| PUT | `/api/seats/{id}` | ADMIN |
| DELETE | `/api/seats/{id}` | ADMIN |

### Shows

| Method | Endpoint | Access |
|---|---|---|
| GET | `/api/show` | Authenticated |
| GET | `/api/show/{id}` | Authenticated |
| POST | `/api/show` | ADMIN |
| PUT | `/api/show/{id}` | ADMIN |
| DELETE | `/api/show/{id}` | ADMIN |

### Bookings

| Method | Endpoint | Access |
|---|---|---|
| POST | `/api/booking` | Authenticated |
| GET | `/api/booking/{id}` | Owner |
| GET | `/api/booking/my` | Authenticated |
| DELETE | `/api/booking/{id}` | Owner |

---

## Pagination & Sorting

Paginated endpoints currently support:

```text
?page=0
&size=10
&sortBy=id
&direction=asc
```

Example:

```text
GET /api/movie?page=0&size=10&sortBy=title&direction=asc
```

Pagination is implemented using Spring Data `Pageable`, allowing pagination to be performed at the database query level rather than loading an entire dataset into memory.

---

## Validation

The API uses Jakarta Bean Validation for request-level validation.

Examples include:

- Required movie fields
- Maximum field lengths
- Valid email addresses
- Required show/movie/screen relationships
- Future show times
- Positive ticket prices
- Required seat selections

Business rules are additionally enforced inside the service layer.

---

## Exception Handling

CineBook uses a centralized `@RestControllerAdvice` for API errors.

The project provides a common error response structure containing:

```json
{
  "timeStamp": "...",
  "status": 400,
  "error": "...",
  "message": "...",
  "path": "...",
  "validationErrors": {}
}
```

Validation failures and application-specific exceptions are converted into appropriate HTTP responses.

---

## Database

CineBook uses PostgreSQL with Spring Data JPA / Hibernate.

The main entities are:

```text
users
refresh_token
movies
theatres
screens
seats
shows
bookings
booking_seats
```

Database relationships are represented through JPA `@ManyToOne` associations and explicit join entities where appropriate.

---

## Soft Deletion

Several domain entities use soft deletion through an `isActive` field.

Instead of physically deleting records:

```text
isActive = false
```

This allows historical data to remain available.

This design is particularly relevant for:

- Movies
- Theatres
- Screens
- Seats
- Shows

Booking records are retained because booking history should not disappear when related domain objects are deactivated.

---

## Project Structure

The project follows a domain-oriented layered structure similar to:

```text
src/main/java/com/mickey/cinebook

├── auth
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── repository
│   └── service
│
├── config
│
├── controller
│
├── dto
│
├── entity
│
├── exception
│
├── repository
│
└── service
```

---

## Configuration

Create a local PostgreSQL database and configure the application through `application.properties`.

Example configuration:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/cinebook
spring.datasource.username=postgres
spring.datasource.password=${DB_PASSWORD}

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

jwt.secretKey=${JWT_SECRET}
jwt.accessTokenExpiration=${JWT_ACCESS_EXPIRATION}
jwt.refreshTokenExpiration=${JWT_REFRESH_EXPIRATION}
```

Do not commit:

- Database passwords
- JWT secrets
- Production credentials
- Real API keys

Use environment variables or an external secrets mechanism.

---

## Running Locally

### 1. Clone the repository

```bash
git clone https://github.com/MickeyBro19/CineBook.git
cd CineBook
```

### 2. Create the PostgreSQL database

```sql
CREATE DATABASE cinebook;
```

### 3. Configure environment variables

Set the required database and JWT configuration in your local environment.

### 4. Start the application

Using Maven:

```bash
./mvnw spring-boot:run
```

Or:

```bash
mvn spring-boot:run
```

The application runs on the configured Spring Boot port.

---

## API Documentation

Swagger/OpenAPI is configured for the project.

Once the application is running, use:

```text
/swagger-ui/index.html
```

The OpenAPI configuration exposes JWT bearer authentication for testing protected endpoints.

---

## Testing

The project uses JUnit 5 and Mockito for service-layer unit testing.

Current tests cover important movie and show business rules including:

- Duplicate movie detection
- Movie creation
- Missing movie handling
- Show creation
- Invalid show times
- Inactive movies
- Inactive screens
- Show deletion

The test suite is being expanded toward controller, repository, security, integration, and concurrency testing.

---

## Engineering Decisions

Some of the current design decisions are intentional:

### JWT + persisted refresh tokens

Access tokens remain stateless, while refresh tokens can be revoked server-side.

### DTOs instead of exposing entities

API contracts are separated from persistence models.

### Service-layer business validation

Repositories handle persistence/querying while services enforce business rules.

### Soft deletion

Historical relationships and records are preserved rather than physically deleting domain data.

### Database constraints + service validation

Important uniqueness rules are checked in application logic and, where appropriate, reinforced at the database level.

### Transactional booking

Booking creation is treated as a transaction because multiple related records must be created consistently.

---

## Current Development Status

CineBook currently has the core backend domain implemented, including:

- Authentication
- JWT authorization
- Refresh-token rotation
- User roles
- Movie management
- Theatre management
- Screen management
- Seat management
- Show scheduling
- Booking
- Booking cancellation
- Pagination
- Validation
- Global exception handling
- Swagger/OpenAPI
- Unit tests

The next development stage focuses on **hardening the existing implementation rather than continuously adding CRUD functionality**.

---

## Roadmap

### Phase 1 — Core Backend Hardening

- [ ] Standardize pagination
- [ ] Standardize sorting
- [ ] Add search support where required
- [ ] Standardize active filtering
- [ ] Review transaction boundaries
- [ ] Clean service/controller responsibilities
- [ ] Fix exception-handler inconsistencies
- [ ] Remove leftover project code
- [ ] Improve configuration management

### Phase 2 — Authentication Hardening

- [ ] Extract authentication logic from controller into services
- [ ] Make refresh-token rotation transactional
- [ ] Standardize refresh-token transport
- [ ] Review cookie security configuration
- [ ] Add authentication integration tests
- [ ] Add authorization tests
- [ ] Add token-revocation tests

### Phase 3 — Booking Reliability

- [ ] Solve concurrent seat-booking race conditions
- [ ] Design seat reservation strategy
- [ ] Add booking lifecycle rules
- [ ] Add cancellation rules
- [ ] Prevent invalid booking states
- [ ] Optimize booking-history queries
- [ ] Add integration tests for concurrent booking scenarios

### Phase 4 — Payment

- [ ] Introduce payment abstraction
- [ ] Add payment entity
- [ ] Connect payment state to booking state
- [ ] Handle successful payments
- [ ] Handle failed payments
- [ ] Handle payment cancellation
- [ ] Add idempotency protection
- [ ] Ensure booking confirmation is transactionally consistent

### Phase 5 — Theatre Owner & Admin

- [ ] Define theatre-owner permissions
- [ ] Restrict theatre-owner resources
- [ ] Add theatre-owner management APIs
- [ ] Add admin booking management
- [ ] Add operational reporting endpoints

### Phase 6 — Testing & Production Readiness

- [ ] Controller tests
- [ ] Repository/integration tests
- [ ] Security tests
- [ ] Booking concurrency tests
- [ ] Testcontainers/PostgreSQL integration tests
- [ ] API error-contract tests
- [ ] Test edge cases and invalid state transitions

### Phase 7 — Deployment

- [ ] Dockerize application
- [ ] Externalize configuration
- [ ] Add environment-specific configuration
- [ ] Add CI pipeline
- [ ] Add automated tests to CI
- [ ] Deploy backend
- [ ] Add database deployment strategy
- [ ] Add application monitoring/logging

---

## Future Architecture

The long-term target is:

```text
                    Client
                      │
                      ▼
                Spring Security
                      │
                      ▼
                   REST API
                      │
          ┌───────────┴───────────┐
          │                       │
     Authentication           CineBook
          │                    Services
          │                       │
          │              ┌────────┼─────────┐
          │              │        │         │
          │           Booking   Show      Movie
          │              │
          │           Payment
          │
          ▼
       PostgreSQL
```

The architecture will remain a modular monolith unless there is a concrete reason to introduce distributed services.

---

## Author

**Mickey**

Backend-focused Java/Spring Boot developer.

GitHub:  
https://github.com/MickeyBro19

---

## License

This project is currently intended as a personal portfolio and learning project.