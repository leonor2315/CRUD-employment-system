$ErrorActionPreference = "Stop"

$timestamp = Get-Date -Format "yyyyMMdd-HHmmss"
$backupDir = Join-Path $PSScriptRoot "..\backups"
New-Item -ItemType Directory -Path $backupDir -Force | Out-Null

$dbName = if ($env:POSTGRES_DB) { $env:POSTGRES_DB } else { "employee_db" }
$dbUser = if ($env:POSTGRES_USER) { $env:POSTGRES_USER } else { "postgres" }
$backupFile = Join-Path $backupDir "employee_db_$timestamp.sql"

Write-Host "Checking postgres service status..."
$postgresStatus = docker compose ps --status running postgres
if (-not $postgresStatus) {
  throw "Postgres container is not running. Start the app first: docker compose up -d"
}

Write-Host "Creating backup to $backupFile"
# Use docker compose service exec so this works even if container names differ by machine.
$dump = docker compose exec -T postgres pg_dump -U $dbUser -d $dbName
if ($LASTEXITCODE -ne 0 -or -not $dump) {
  throw "Backup failed. Check database credentials in .env and ensure postgres is healthy."
}

$dump | Out-File -FilePath $backupFile -Encoding utf8

Write-Host "Backup complete."
