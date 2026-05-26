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
- `GET /api/director/employee-work` -> all employee work records for Managing Director read-only view
- `GET /api/director/employee-work/{employeeId}` -> single employee work record for Managing Director read-only view
- `GET /api/employee/work/{employeeId}` -> employee/manager/admin read endpoint
- `GET /api/auth/me` -> authenticated username and roles used by the frontend
- `GET /api/admin/users` -> HR-only user account list
- `POST /api/admin/users` -> create database-backed user account
- `PUT /api/admin/users/{id}` -> update user role/status
- `POST /api/admin/users/{id}/reset-password` -> reset user password
- `GET /api/admin/employee-work/search` -> paged employee search/filter
- `GET /api/admin/employee-work/archived` -> archived employee records
- `POST /api/admin/employee-work/{employeeId}/restore` -> restore archived employee
- `GET /api/admin/audit` -> recent audit trail
- `GET /api/admin/audit/employee/{employeeId}` -> audit trail for one employee
- `GET /api/admin/system/status` -> database/user/audit health summary
- `GET /api/analytics/work-summary` -> secured dashboard summary for HR/Director

### Auth (HTTP Basic)

- `humanresource / HRMI056` -> `ROLE_ADMIN`
- `MD / MD056` -> `ROLE_DIRECTOR`
- `manager / manager123` -> `ROLE_MANAGER`
- `employee / employee123` -> `ROLE_EMPLOYEE`

The browser does not persist passwords; users sign in again after a refresh/reopen.
User accounts are stored in PostgreSQL with BCrypt-hashed passwords and are seeded only when the user table is empty.
After first setup, change the seeded passwords in the User Management screen. New and reset passwords must be at least 8 characters and include letters and numbers.

Authorization rules:
- `/api/admin/**` -> ADMIN
- `/api/director/**` -> DIRECTOR or ADMIN
- `/api/employee/**` -> EMPLOYEE, MANAGER, ADMIN

### Python service (`http://localhost:8001`)

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

Before production use, set a strong `POSTGRES_PASSWORD` in `.env` and keep `APP_CORS_ALLOWED_ORIGINS` limited to the frontend URLs users actually open.

Services:
- React: `http://localhost:5173`
- Java API: `http://localhost:8080`
- Python API: `http://localhost:8001`
- PostgreSQL: `localhost:5432` only on the host machine

## Website Deployment

For a no-physical-server setup, use the free website deployment guide:

- [Free Website Deployment Guide](FREE_WEBSITE_DEPLOYMENT.md)

## Stability Defaults (already configured)

- Health checks for all services
- Auto-restart (`unless-stopped`)
- Resource limits (`cpus`, `mem_limit`, `pids_limit`)
- Log rotation (10 MB x 5 files per container)
- Dependency startup ordering via `condition: service_healthy`
- Soft archive/restore instead of permanent employee deletes
- Audit trail for employee create/update/archive/restore/export actions
- Login throttling after repeated failed password attempts
- Restricted CORS origins and no-store API security headers

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
