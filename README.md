# Employee Work Information System

This project is a full-stack system where admin/manager can access and manage employee work information.

## Tech Stack

- Frontend: React (`frontend-react`)
- Backend A: Java Spring Boot (`backend-java`)
- Backend B: Python FastAPI (`backend-python`)

## High-Level Design

- **Java (Spring Boot)** provides secured role-based CRUD APIs and persists employee work data in PostgreSQL.
- **Python (FastAPI)** reads from PostgreSQL and returns analytics summary for the admin dashboard.
- **React** UI supports login credentials and calls the secured Java API plus Python analytics API.
- **Docker Compose** runs all services together (PostgreSQL + 3 apps).

## Core APIs

### Java service (`http://localhost:8080`)

- `GET /api/admin/employee-work` -> all employee work records
- `GET /api/admin/employee-work/{employeeId}` -> single employee work record
- `POST /api/admin/employee-work` -> create work record
- `PUT /api/admin/employee-work/{employeeId}` -> update work record
- `DELETE /api/admin/employee-work/{employeeId}` -> delete work record
- `GET /api/employee/work/{employeeId}` -> employee/manager/admin read endpoint

### Auth (HTTP Basic)

- `admin / admin123` -> `ROLE_ADMIN`
- `manager / manager123` -> `ROLE_MANAGER`
- `employee / employee123` -> `ROLE_EMPLOYEE`

Authorization rules:
- `/api/admin/**` -> ADMIN or MANAGER
- `/api/employee/**` -> EMPLOYEE, MANAGER, ADMIN

### Python service (`http://localhost:8000`)

- `GET /api/analytics/work-summary` -> summary by status/team

## Project Structure

```text
employee-work-system/
  docker-compose.yml
  backend-java/
  backend-python/
  frontend-react/
```

## Run with Docker Compose

From project root:

```bash
copy .env.example .env
docker compose up --build
```

Services:
- React: `http://localhost:5173`
- Java API: `http://localhost:8080`
- Python API: `http://localhost:8000`
- PostgreSQL: `localhost:5432`

## Stability Defaults (already configured)

- Health checks for all services
- Auto-restart (`unless-stopped`)
- Resource limits (`cpus`, `mem_limit`, `pids_limit`)
- Log rotation (10 MB x 5 files per container)
- Dependency startup ordering via `condition: service_healthy`

## Backup and Restore

Create backup:

```powershell
powershell -ExecutionPolicy Bypass -File .\ops\backup-db.ps1
```

Cleanup old backups (keeps last 30 days by default):

```powershell
powershell -ExecutionPolicy Bypass -File .\ops\cleanup-backups.ps1 -KeepDays 30
```

Restore from backup:

```powershell
powershell -ExecutionPolicy Bypass -File .\ops\restore-db.ps1 -BackupFile ".\backups\employee_db_YYYYMMDD-HHMMSS.sql"
```

Recommendation: schedule `ops\backup-db.ps1` daily with Windows Task Scheduler.
Recommendation: run `ops\cleanup-backups.ps1` weekly (or after backup) to control disk growth.

### Import ready-made daily backup task (Windows)

1. Open **Task Scheduler** -> **Import Task...**
2. Select `ops\daily-db-backup-task.xml`
3. In the task Actions tab, confirm these paths match your machine:
   - `C:\Users\USER\employee-work-system\ops\backup-db.ps1`
   - Working directory `C:\Users\USER\employee-work-system`
4. Save the task and run it once manually to confirm a file appears in `backups\`.

## One-click Start (client machine)

From project root:

```powershell
powershell -ExecutionPolicy Bypass -File .\Start-App.ps1
```

## Local Run (without Docker)

1) Start PostgreSQL database `employee_db`

2) Java backend:
```bash
cd backend-java
mvn spring-boot:run
```

3) Python backend:
```bash
cd backend-python
pip install -r requirements.txt
uvicorn app.main:app --reload --port 8000
```

4) React frontend:
```bash
cd frontend-react
npm install
npm run dev
```

