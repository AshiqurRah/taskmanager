#!/bin/bash

set -e  # Exit on error

echo "======================================"
echo "Task Manager API - Starting Application"
echo "======================================"

# Check if Docker is installed
if ! command -v docker &> /dev/null; then
    echo "Error: Docker is not installed. Please install Docker first."
    exit 1
fi

# Determine which docker compose command to use
DOCKER_COMPOSE_CMD=""
if command -v docker-compose &> /dev/null; then
    DOCKER_COMPOSE_CMD="docker-compose"
elif docker compose version &> /dev/null 2>&1; then
    DOCKER_COMPOSE_CMD="docker compose"
else
    echo "Error: Docker Compose is not installed. Please install Docker Compose first."
    exit 1
fi

# Stop and remove existing containers
echo ""
echo "Cleaning up existing containers..."
$DOCKER_COMPOSE_CMD down -v 2>/dev/null || true

# Build and start containers
echo ""
echo "Building and starting containers..."
echo "This may take a few minutes on first run..."
$DOCKER_COMPOSE_CMD up --build -d

# Wait for MySQL to be ready
echo ""
echo "Waiting for MySQL database to be ready..."
max_attempts=30
attempt=0
while [ $attempt -lt $max_attempts ]; do
    if docker exec taskmanager-mysql mysqladmin ping -h localhost -u root -proot_password --silent 2>/dev/null; then
        echo "MySQL is ready!"
        break
    fi
    attempt=$((attempt + 1))
    echo "Waiting for MySQL... (attempt $attempt/$max_attempts)"
    sleep 2
done

if [ $attempt -eq $max_attempts ]; then
    echo "Error: MySQL failed to start within expected time"
    docker-compose logs mysql
    exit 1
fi

# Wait for application to be ready
echo ""
echo "Waiting for application to be ready..."
max_attempts=60
attempt=0
while [ $attempt -lt $max_attempts ]; do
    if curl -s http://localhost:8080/actuator/health > /dev/null 2>&1 || curl -s http://localhost:8080/tasks > /dev/null 2>&1; then
        echo "Application is ready!"
        break
    fi
    attempt=$((attempt + 1))
    echo "Waiting for application... (attempt $attempt/$max_attempts)"
    sleep 2
done

if [ $attempt -eq $max_attempts ]; then
    echo "Warning: Application may still be starting. Check logs with: docker-compose logs app"
fi

echo ""
echo "======================================"
echo "Task Manager API is running!"
echo "======================================"
echo ""
echo "API Endpoint:      http://localhost:8080/tasks"
echo "Swagger UI:        http://localhost:8080/swagger-ui.html"
echo "API Documentation: http://localhost:8080/api-docs"
echo ""
echo "To view logs:      $DOCKER_COMPOSE_CMD logs -f app"
echo "To stop:           $DOCKER_COMPOSE_CMD down"
echo ""
echo "======================================"
