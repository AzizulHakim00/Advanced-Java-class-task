@echo off
setlocal
cd /d "%~dp0"

echo [FocusForge] Removing stale build output...
if exist target rmdir /s /q target
if exist out rmdir /s /q out

echo [FocusForge] Removing legacy local source files from older package layouts...
for %%F in (
    "src\main\java\bd\edu\seu\classproject\FocusForgeController.java"
    "src\main\java\bd\edu\seu\classproject\HomeController.java"
    "src\main\java\bd\edu\seu\classproject\StudyTask.java"
    "src\main\java\bd\edu\seu\classproject\StudyCheckIn.java"
    "src\main\java\bd\edu\seu\classproject\RecommendationResult.java"
    "src\main\java\bd\edu\seu\classproject\StudyTaskInterface.java"
) do (
    if exist %%F del /f /q %%F
)

for %%D in (
    "src\main\java\bd\edu\seu\classproject\controller"
    "src\main\java\bd\edu\seu\classproject\model"
    "src\main\java\bd\edu\seu\classproject\repository"
    "src\main\java\bd\edu\seu\classproject\service"
) do (
    if exist %%D rmdir /s /q %%D
)

echo [FocusForge] Running a fresh Maven build and application startup...
call mvnw.cmd -U clean spring-boot:run

if errorlevel 1 (
    echo.
    echo [FocusForge] Startup failed.
    echo Check that MySQL is running and application.properties has the correct username/password.
    pause
    exit /b 1
)

endlocal
