# Task Manager API

## Overview
This is my Task Manager REST API built with Spring Boot. It's a CRUD (Create, Read, Update, Delete) application that manages tasks and stores them in a MySQL database. I built this as my first Spring Boot project!

The API lets you:
- Create new tasks
- Get all tasks or a specific task
- Update existing tasks
- Delete tasks

Everything runs in Docker containers, so you just need to run one command to start it!

## How It Works

I organized the code into different layers.

```
Controller Layer (TaskController.java)
    ↓
Service Layer (TaskService.java)
    ↓
Repository Layer (TaskRepository.java)
    ↓
Database (MySQL in Docker)
```

**What each layer does:**
- **Controller**: Handles HTTP requests (GET, POST, PUT, DELETE) from users
- **Service**: Contains the business logic (what to do with the data)
- **Repository**: Talks to the database 
- **Entity**: The Task model that represents how data is stored in the database

### What I Learned While Building This
- **OpenAPI Specification**: I wrote the API spec in `openapi.yaml` first, then Spring generated a lot of code for me automatically
- **Spring Data JPA**: I don't have to write SQL! Spring gives me methods like `findAll()` and `save()` for free
- **@Autowired**: This is how Spring injects dependencies (connects different parts of the app)
- **Exception Handling**: I use `@RestControllerAdvice` to catch errors and return proper error messages
- **Docker**: Everything runs in containers so it works the same everywhere

### Technologies I Used
- **Java 17**: The programming language
- **Spring Boot 3.5.3**: The framework that makes building REST APIs easier
- **MySQL 8.0**: Database to store the tasks
- **Docker**: To run everything in containers
- **Gradle**: Build tool (compiles and packages the app)
- **OpenAPI Generator**: Auto-generates code from the API spec
- **Testcontainers**: For testing with a real database
- **JaCoCo**: Shows code coverage from tests
- **GitHub Actions**: Automatically runs tests when I push code

## What You Need Before Running

You need to install these on your computer:

- **Docker Desktop**: This runs the application and database in containers


## How to Run the Application

Just run this one command:
```bash
./run.sh
```

This script does everything automatically:
1. Checks if Docker is installed
2. Stops any old containers
3. Builds the app
4. Starts MySQL database
5. Starts the Spring Boot app
6. Waits for everything to be ready

**When it's ready, you'll see:**
```
======================================
Task Manager API is running!
======================================

API Endpoint:      http://localhost:8080/tasks
Swagger UI:        http://localhost:8080/swagger-ui.html
API Documentation: http://localhost:8080/api-docs
```

### Manual Way

```bash

# Start everything
docker compose up --build -d

# See the application logs
docker compose logs -f app

# Stop everything
docker compose down

# Stop and delete all data
docker compose down -v
```

## How to Use the API

### Where to Find the API Documentation

Once the app is running, you can access the API in a few ways:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
  - This is a web page where you can test the API directly in your browser

- **OpenAPI JSON**: http://localhost:8080/api-docs
  - The raw API specification in JSON format

- **Postman**: Import the `postman_collection.json` file
  - Has all the API calls ready to test

### What the API Can Do

Here are all the endpoints (API URLs) you can use:

| What It Does | How to Call It | Returns |
|--------------|----------------|---------|
| Get all tasks | `GET /tasks` | List of all tasks |
| Get one task | `GET /tasks/{id}` | Single task |
| Create a task | `POST /tasks` | The new task created |
| Update a task | `PUT /tasks/{id}` | The updated task |
| Delete a task | `DELETE /tasks/{id}` | Nothing (just deletes it) |

### Examples 

**Create your first task:**
```bash
curl -X POST http://localhost:8080/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Learn Spring Boot",
    "description": "Build my first REST API",
    "completed": false
  }'
```

**See all your tasks:**
```bash
curl http://localhost:8080/tasks
```

**Mark a task as complete:**
```bash
curl -X PUT http://localhost:8080/tasks/1 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Learn Spring Boot",
    "description": "Build my first REST API",
    "completed": true
  }'
```

**Delete a task:**
```bash
curl -X DELETE http://localhost:8080/tasks/1
```

**Easier way**: Just open http://localhost:8080/swagger-ui.html in your browser and test everything there!

## Testing

I wrote two types of tests:

1. **Unit Tests**: These test individual parts of the code (like the TaskService)
   - Mock the database so tests run fast
   - Make sure each method works correctly

2. **Integration Tests**: These test the whole API with a real database
   - Uses Testcontainers (spins up a MySQL container just for testing)
   - Tests that everything works together

### Running the Tests

**Run all tests:**
```bash
./gradlew test
```

**See test coverage (how much code is tested):**
```bash
./gradlew test jacocoTestReport
open build/reports/jacoco/test/html/index.html
```

## Database Structure

### How Tasks Are Stored

The MySQL database has one table called `tasks` that looks like this:

```sql
CREATE TABLE tasks (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,  -- Unique ID for each task
    title       VARCHAR(255) NOT NULL,              -- Task title (max 255 chars)
    description VARCHAR(1000) NOT NULL,             -- Task description (max 1000 chars)
    completed   BOOLEAN NOT NULL DEFAULT FALSE,     -- Is it done? Default: no
    created_at  TIMESTAMP NOT NULL,                 -- When was it created?
    updated_at  TIMESTAMP NOT NULL                  -- When was it last updated?
);
```

**New thing I learned:** Hibernate (part of Spring) creates this table automatically! I just need to write the `Task.java` class and Spring handles the database schema. The `@PrePersist` and `@PreUpdate` annotations automatically set the timestamps.

## Continuous Integration (CI/CD)

### What Happens When I Push Code to GitHub

I set up GitHub Actions to automatically test my code. Every time I push to GitHub:

1. **Build and Test**: GitHub compiles the code and runs all the tests
2. **Build Docker Image**: Makes sure the Docker build works
3. **Code Quality Check**: Checks if the code follows good practices

The workflow file is in `.github/workflows/ci-cd.yml`

**What gets tested:**
- All unit tests must pass
- All integration tests must pass
- Code coverage report is generated
- Docker image builds successfully

**When it runs:**
- Every time I push code to `main` or `develop` branch
- Every time I create a pull request

You can see the results at: `https://github.com/ashiqurrah/taskmanager/actions`

## Project Structure

Here's how I organized the files:

```
taskmanager/
├── src/main/java/.../taskmanager/
│   ├── controller/
│   │   └── TaskController.java       ← Handles HTTP requests
│   ├── service/
│   │   └── TaskService.java          ← Business logic
│   ├── repository/
│   │   └── TaskRepository.java       ← Talks to database
│   ├── entity/
│   │   └── Task.java                 ← Database model
│   ├── mapper/
│   │   └── TaskMapper.java           ← Converts between models
│   └── exception/
│       ├── TaskNotFoundException.java
│       └── GlobalExceptionHandler.java
│
├── src/test/java/.../taskmanager/
│   ├── service/
│   │   └── TaskServiceTest.java      ← Unit tests
│   └── integration/
│       └── TaskApiIntegrationTest.java ← API tests
│
├── build.gradle                       ← Dependencies & build config
├── openapi.yaml                       ← API specification
├── docker-compose.yml                 ← Docker setup
├── Dockerfile                         ← How to build the app
└── run.sh                             ← Easy start script
```

## Things I Assumed While Building

1. **No Login Required**: Anyone can create/edit/delete tasks (I didn't add user authentication)
2. **All Times in UTC**: All timestamps are in UTC timezone
3. **Tasks Start Incomplete**: New tasks have `completed = false` by default
4. **Required Fields**: Every task must have a title and description
5. **Simple IDs**: Task IDs just count up (1, 2, 3, etc.)
6. **English Only**: All error messages are in English

## What Could Be Better (Future Ideas)

Things I didn't have time to add but would be cool:

1. **User Login**: Add authentication so each user has their own tasks
2. **More Tasks Features**:
   - Categories or tags for tasks
   - Due dates and reminders
   - Priority levels (high, medium, low)
3. **Better Performance**:
   - Pagination (only load 10 tasks at a time)
   - Search and filter tasks
   - Caching frequently accessed data
4. **Monitoring**: Add Grafana dashboards to see how the API is performing
5. **Soft Delete**: Keep deleted tasks in the database (just mark them as deleted)

## Common Problems and Solutions

### Problem: "Port 8080 is already in use"

Something else is using port 8080. Here's how to fix it:

```bash
# Find what's using the port
lsof -i :8080

# Kill that process, or stop the app that's using it
```

### Problem: "Docker daemon not running"

Make sure Docker Desktop is running! Look for the Docker icon in your menu bar.

### Problem: App won't start

Try these steps:

```bash
# See what's wrong
docker compose logs app

# Stop everything and start fresh
docker compose down -v
./run.sh
```

### Problem: Tests are failing

```bash
# Clean build and rerun
./gradlew clean test

# Make sure Docker is running (tests need it)
docker ps
```

## About This Project

I built this as my first Spring Boot project to learn how to:
- Build REST APIs with Spring Boot
- Work with databases using JPA
- Write tests
- Use Docker
- Follow OpenAPI specifications

This was a great learning experience!