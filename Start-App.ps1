$ErrorActionPreference = "Stop"

$root = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location $root

Write-Host "Starting Employee Work System..."
docker compose up -d --build

Write-Host ""
Write-Host "System started. Open:"
Write-Host "http://localhost:5173"
Write-Host ""
Write-Host "Service status:"
docker compose ps
