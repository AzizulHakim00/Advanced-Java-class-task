@echo off
setlocal
cd /d "%~dp0"

echo [FocusForge] Removing stale Maven build output...
if exist target rmdir /s /q target

echo [FocusForge] Starting a clean Spring Boot build...
call mvnw.cmd clean spring-boot:run

if errorlevel 1 (
    echo.
    echo [FocusForge] Clean run failed. Check the MySQL server and application.properties credentials.
    pause
    exit /b 1
)

endlocal
