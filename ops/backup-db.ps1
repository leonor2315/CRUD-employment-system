$ErrorActionPreference = "Stop"

$timestamp = Get-Date -Format "yyyyMMdd-HHmmss"
$backupDir = Join-Path $PSScriptRoot "..\backups"
New-Item -ItemType Directory -Path $backupDir -Force | Out-Null

$dbName = if ($env:POSTGRES_DB) { $env:POSTGRES_DB } else { "employee_db" }
$dbUser = if ($env:POSTGRES_USER) { $env:POSTGRES_USER } else { "postgres" }
$backupFile = Join-Path $backupDir "employee_db_$timestamp.sql"

Write-Host "Creating backup to $backupFile"
docker exec employee-postgres pg_dump -U $dbUser -d $dbName | Out-File -FilePath $backupFile -Encoding utf8

Write-Host "Backup complete."
