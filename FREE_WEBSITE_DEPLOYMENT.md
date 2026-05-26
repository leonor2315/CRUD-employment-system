# Free Website Deployment Guide

This guide explains the cheapest practical way to run the Employee Work Information System as a private website without buying a physical server.

## Recommended Free Setup

- **GitHub**: Stores the project code.
- **GitHub Pages**: Hosts the React frontend for free.
- **Render Free Web Service**: Hosts the Java Spring Boot backend.
- **Neon Free PostgreSQL**: Hosts the shared database.
- **Optional Cloudflare Access**: Adds a staff-only gate in front of the website if you have a domain.

GitHub alone cannot host the full system because GitHub Pages only serves static frontend files. The backend and database still need cloud services.

## Target Architecture

```text
Staff browser
  -> GitHub Pages frontend
  -> Render Java backend
  -> Neon PostgreSQL database
```

For the cheapest deployment, host only one backend service. The hosted frontend now calls the Java API for both employee records and analytics, so the Python service is not required for the free cloud setup.

## What Has Been Prepared In This Project

These project changes are included:

- Frontend API URLs can be configured with `VITE_API_BASE_URL` and `VITE_ANALYTICS_API_BASE_URL`.
- Java now provides `GET /api/analytics/work-summary`, so the hosted setup only needs the Java backend.
- GitHub Actions workflow exists at `.github/workflows/deploy-pages.yml`.
- Frontend production example exists at `frontend-react/.env.production.example`.
- Render backend example exists at `backend-java/.env.render.example`.

## What You Must Do Outside The Project

### 1. Create A GitHub Repository

1. Create a new GitHub repository.
2. Push this project to that repository.
3. Keep the repository private because this is company HR software.

### 2. Create A Free Neon Database

1. Go to Neon and create a free PostgreSQL project.
2. Copy the connection details.
3. Use the pooled connection if Neon provides one.
4. Keep the database password private.

You will need these values for Render:

```text
SPRING_DATASOURCE_URL=jdbc:postgresql://<neon-host>/<database>?sslmode=require
SPRING_DATASOURCE_USERNAME=<neon-user>
SPRING_DATASOURCE_PASSWORD=<neon-password>
```

### 3. Deploy The Java Backend To Render

1. Create a Render account.
2. Create a new Web Service from the GitHub repository.
3. Use the backend Dockerfile:

```text
Root Directory: backend-java
Dockerfile Path: Dockerfile
```

4. Add the Neon database environment variables from `backend-java/.env.render.example`.
5. Add the frontend origin to CORS. CORS uses only the domain, not the `/repo-name` path:

```text
APP_CORS_ALLOWED_ORIGINS=https://<your-github-username>.github.io
```

6. Deploy and copy the Render backend URL.

Example backend URL:

```text
https://employee-work-api.onrender.com
```

### 4. Deploy The Frontend To GitHub Pages

1. Go to the GitHub repository settings.
2. Open **Pages** and set the source to **GitHub Actions**.
3. Open **Actions variables** and add:

```text
VITE_API_BASE_URL=https://<your-render-backend>.onrender.com
VITE_ANALYTICS_API_BASE_URL=https://<your-render-backend>.onrender.com
```

4. Leave `VITE_BASE_PATH` empty unless you use a custom domain. The Vite config automatically uses `/<repo-name>/` on GitHub Pages.
5. Push to the `main` branch.
6. Open the GitHub Pages URL after deployment completes.

Example frontend URL:

```text
https://<your-github-username>.github.io/<repo-name>
```

### 5. First Login And Security

1. Login with the seeded HR account.
2. Change all default passwords immediately.
3. Create only the user accounts needed by HR and the Managing Director.
4. Disable any unused accounts.
5. Keep the app link private.

## Free Hosting Limitations

- Free backend services can sleep after inactivity, so first login may take 30-60 seconds.
- Free database tiers have storage limits.
- Free services usually have limited support.
- Backups may not be as strong as paid plans.
- If this becomes important for daily company operations, move backend and database to paid plans.

## Safer Upgrade Later

When funds are available, the best upgrade path is:

- Paid Render or DigitalOcean backend.
- Paid managed PostgreSQL with automatic backups.
- Custom domain with HTTPS.
- Cloudflare Access or another staff-only access layer.

This keeps the same website idea but improves reliability and support.
