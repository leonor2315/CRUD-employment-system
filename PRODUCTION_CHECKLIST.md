# Production Readiness Checklist

Use this checklist before publishing and after each major update.

## 1) Runtime Stability

- [ ] `docker compose ps` shows all services `Up` and `healthy`
- [ ] Verify restart policy is active (`restart: unless-stopped`)
- [ ] Run app continuously for 24-72 hours in staging (soak test)
- [ ] Confirm no memory growth trend in containers

## 2) Health and Monitoring

- [ ] Verify each health endpoint/check works:
  - [ ] Postgres: `pg_isready`
  - [ ] Java API: authenticated check to `/api/admin/employee-work`
  - [ ] Python API: `/health`
  - [ ] Frontend: `/`
- [ ] Set host-level alerts for high CPU/memory/disk
- [ ] Capture and retain container logs (e.g., daily rotation)

## 3) Data and Backups

- [ ] Configure automatic database backups (daily minimum)
- [ ] Test restore from backup before go-live
- [ ] Confirm `postgres_data` volume is on reliable storage

## 4) Security and Access

- [ ] Replace default credentials (`admin/admin123`)
- [ ] Move secrets to environment variables or secret manager
- [ ] Restrict exposed ports to only what is required
- [ ] Enable HTTPS at reverse proxy/load balancer

## 5) Performance and Limits

- [ ] Set container memory/CPU limits
- [ ] Set request timeouts and retry strategy
- [ ] Verify app behavior under expected concurrent load

## 6) Operations

- [ ] Document deploy and rollback commands
- [ ] Record current image tags/versions
- [ ] Confirm who receives production alerts
- [ ] Confirm an owner for incident response
