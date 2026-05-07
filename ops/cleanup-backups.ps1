$ErrorActionPreference = "Stop"

param(
  [int]$KeepDays = 30
)

$backupDir = Join-Path $PSScriptRoot "..\backups"
if (-not (Test-Path $backupDir)) {
  Write-Host "Backups directory not found: $backupDir"
  exit 0
}

$cutoff = (Get-Date).AddDays(-$KeepDays)
$oldFiles = Get-ChildItem -Path $backupDir -File | Where-Object { $_.LastWriteTime -lt $cutoff }

foreach ($file in $oldFiles) {
  Write-Host "Removing old backup: $($file.FullName)"
  Remove-Item -Path $file.FullName -Force
}

Write-Host "Backup cleanup complete. KeepDays=$KeepDays"
