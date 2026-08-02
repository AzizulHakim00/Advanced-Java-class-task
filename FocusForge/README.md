# FocusForge – Smart Study Decision Assistant

FocusForge is a Spring Boot, Thymeleaf, JPA, and MySQL class project. It manages study tasks and recommends a suitable task from the student's available time, energy, mood, deadline, difficulty, and importance.

## Product Shop Style Code Structure

```text
src/main/java/bd/edu/seu/classproject/
├── FocusForgeApplication.java
└── focusforge/
    ├── FocusForgeController.java
    ├── HomeController.java
    ├── StudyTask.java
    ├── StudyCheckIn.java
    ├── RecommendationResult.java
    ├── StudyTaskRepository.java
    ├── StudyTaskService.java
    └── RecommendationService.java
```

The application explicitly scans only `bd.edu.seu.classproject.focusforge`. Legacy classes left in an older root, controller, model, repository, or service package are therefore not registered as Spring beans, JPA entities, or repositories.

## Main Features

- MySQL database-backed CRUD
- Add, edit, delete, and update task status
- Jakarta Bean Validation
- Search and filter by status, difficulty, and importance
- Dashboard statistics and deadline status
- Mood, energy, time, deadline, difficulty, and importance based recommendation
- Completed-task history, total study time, completion rate, and streak
- Responsive Thymeleaf interface

## MySQL Configuration

Update `src/main/resources/application.properties` when your MySQL credentials are different:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/focusforge_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Dhaka
spring.datasource.username=root
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=update
```

## Normal Run

1. Start MySQL Server on port `3306`.
2. Open the `FocusForge` folder in IntelliJ IDEA.
3. Select JDK 21.
4. Reload the Maven project.
5. Run `FocusForgeApplication.java`.
6. Open `http://localhost:8080/focusforge/dashboard`.

## Complete Windows Recovery

Use this after pulling a package-refactoring update or after seeing `ConflictingBeanDefinitionException`.

1. Stop the application in IntelliJ.
2. Pull the latest `main` branch.
3. Run `FocusForge/clean-run.cmd`.

The script automatically removes:

- `target` and `out` compiled output
- legacy root-package Java files
- legacy `controller`, `model`, `repository`, and `service` directories

It then performs a fresh Maven download check, clean build, and Spring Boot startup.

```bat
cd "C:\Github\Advanced Java class task"
git pull origin main
cd FocusForge
clean-run.cmd
```

## Startup Hardening

- Component scanning is restricted to the final `focusforge` feature package.
- Entity scanning is restricted to the final `focusforge` feature package.
- JPA repository scanning is restricted to the final `focusforge` feature package.
- Spring Boot DevTools was removed to prevent restart-classloader reuse of deleted classes.
- Lombok and its annotation-processor configuration were removed because the project uses explicit constructors and accessors.
