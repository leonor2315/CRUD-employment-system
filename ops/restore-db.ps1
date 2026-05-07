$ErrorActionPreference = "Stop"

param(
  [Parameter(Mandatory = $true)]
  [string]$BackupFile
)

if (-not (Test-Path $BackupFile)) {
  throw "Backup file not found: $BackupFile"
}

$dbName = if ($env:POSTGRES_DB) { $env:POSTGRES_DB } else { "employee_db" }
$dbUser = if ($env:POSTGRES_USER) { $env:POSTGRES_USER } else { "postgres" }

Write-Host "Restoring $BackupFile to database $dbName..."
Get-Content -Path $BackupFile | docker exec -i employee-postgres psql -U $dbUser -d $dbName
Write-Host "Restore complete."
