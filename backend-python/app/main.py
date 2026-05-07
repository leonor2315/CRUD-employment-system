import os
from contextlib import contextmanager

from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from sqlalchemy import create_engine, text

# FastAPI service that provides dashboard analytics from PostgreSQL.
app = FastAPI(title="Employee Work Analytics API")

# Allow the frontend app to call this API from the browser.
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

DB_URL = os.getenv("DATABASE_URL", "postgresql+psycopg2://postgres:postgres@localhost:5432/employee_db")
# Shared SQLAlchemy engine for all requests.
engine = create_engine(DB_URL, pool_pre_ping=True)


@contextmanager
def db_conn():
    # Simple connection helper to ensure each connection closes cleanly.
    with engine.connect() as conn:
        yield conn


@app.get("/api/analytics/work-summary")
def work_summary():
    # Return counts and grouped summaries used by dashboard metric cards/charts.
    with db_conn() as conn:
        total_employees = conn.execute(text("select count(*) from employee_work")).scalar() or 0
        active_employees = conn.execute(
            text("select count(*) from employee_work where status = 'Engaged'")
        ).scalar() or 0
        on_payroll = conn.execute(
            text("select count(*) from employee_work where payroll_status = 'On payroll'")
        ).scalar() or 0

        status_rows = conn.execute(
            text("select status, count(*) as total from employee_work group by status order by status")
        ).all()
        location_rows = conn.execute(
            text("select location, count(*) as total from employee_work group by location order by location")
        ).all()

    return {
        "totalEmployees": int(total_employees),
        "activeEmployees": int(active_employees),
        "onPayrollEmployees": int(on_payroll),
        "statusBreakdown": {row[0]: int(row[1]) for row in status_rows},
        "locationBreakdown": {row[0]: int(row[1]) for row in location_rows},
    }


@app.get("/health")
def health():
    # Lightweight health check endpoint for container/runtime monitoring.
    return {"status": "ok"}


if __name__ == "__main__":
    import uvicorn

    uvicorn.run(app, host="0.0.0.0", port=8000)
