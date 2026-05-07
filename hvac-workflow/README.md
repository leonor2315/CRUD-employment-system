# HVAC Shop Workflow

A narrow workflow tool for small HVAC shops that replaces spreadsheets, email chains, and custom databases with a job, inventory, customer, and scheduling manager.

## Project structure

- `backend-java/`: Spring Boot REST API
- `frontend-react/`: React dashboard and job form
- `docker-compose.yml`: local development with Java backend and React frontend

## Run locally

From `hvac-workflow`:

1. Start the backend with Docker Compose:
   ```powershell
   docker compose up --build
   ```
2. Open the frontend at `http://localhost:4173`
3. Use the REST API at `http://localhost:8082/api`

## Features

- Job tracking and service scheduling
- Customer record management
- Inventory item tracking for service jobs
- Simple status and assignment workflow
- H2 database for local development
