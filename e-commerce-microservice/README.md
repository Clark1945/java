# E-commerce Microservices Project

This is a sample e-commerce application built with a microservices architecture using Spring Boot.

## Services

The project consists of the following microservices:

- **e-commerce-api-gateway**: The API gateway for the application. It routes requests to the appropriate microservices.
- **e-commerce-config-server**: The configuration server for the application. It provides centralized configuration for all microservices.
- **e-commerce-eureka-server**: The service discovery server for the application. It allows microservices to find and communicate with each other.
- **e-commerce-product**: The product service for the application. It manages products and categories.

## Getting Started

### Prerequisites

- Java 17
- Maven
- Docker

### Installation

1. Clone the repository.
2. Build each microservice using the following command in each service's directory:

```bash
mvn clean install
```

## Running the application

To start the application, run the following command in the root directory of the project:

```bash
docker-compose up
```

## API Endpoints

The following API endpoints are available:

- `GET /api/products/category/{id}`: Get a category by ID.
- `GET /api/products/{id}`: Get a product by ID.
