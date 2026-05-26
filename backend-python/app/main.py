import os
from contextlib import contextmanager

from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from sqlalchemy import create_engine, text

# FastAPI service that provides dashboard analytics from PostgreSQL.
app = FastAPI(title="Employee Work Analytics API")


def allowed_origins():
    raw_origins = os.getenv(
        "APP_CORS_ALLOWED_ORIGINS",
        "http://localhost:5173,http://localhost:5174,http://127.0.0.1:5180,http://127.0.0.1:5173",
    )
    return [origin.strip() for origin in raw_origins.split(",") if origin.strip()]


# Allow the frontend app to call this API from the browser.
app.add_middleware(
    CORSMiddleware,
    allow_origins=allowed_origins(),
    allow_credentials=False,
    allow_methods=["GET"],
    allow_headers=["*"],
)


@app.middleware("http")
async def add_security_headers(request, call_next):
    response = await call_next(request)
    response.headers["Cache-Control"] = "no-store"
    response.headers["Pragma"] = "no-cache"
    response.headers["X-Content-Type-Options"] = "nosniff"
    response.headers["Referrer-Policy"] = "no-referrer"
    response.headers["X-Frame-Options"] = "DENY"
    return response

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
        has_archived_column = conn.execute(
            text(
                "select count(*) from information_schema.columns "
                "where table_name = 'technician' and column_name = 'archived'"
            )
        ).scalar() or 0
        active_where = "where coalesce(archived, false) = false" if has_archived_column else ""
        active_and = "and coalesce(archived, false) = false" if has_archived_column else ""

        total_employees = conn.execute(text(f"select count(*) from technician {active_where}")).scalar() or 0
        active_employees = conn.execute(
            text(
                "select count(*) from technician "
                "where lower(trim(coalesce(status, ''))) = 'engaged' "
                f"{active_and}"
            )
        ).scalar() or 0
        on_payroll = conn.execute(
            text(
                "select count(*) from technician "
                "where lower(trim(coalesce(payroll_status, ''))) = 'on payroll' "
                f"{active_and}"
            )
        ).scalar() or 0

        status_rows = conn.execute(
            text(f"select status, count(*) as total from technician {active_where} group by status order by status")
        ).all()
        location_rows = conn.execute(
            text(f"select location, count(*) as total from technician {active_where} group by location order by location")
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
