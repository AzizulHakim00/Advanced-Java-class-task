# FocusForge – Smart Study Decision Assistant

FocusForge is a Spring Boot, Thymeleaf, JPA, and MySQL class project. It manages study tasks and recommends a suitable task from the student's available time, energy, mood, deadline, difficulty, and importance.

## Product Shop Style Code Structure

The uploaded Product Shop project keeps the application class in the root package and keeps each feature's entity, controller, repository, and service together inside one feature package. FocusForge now follows that same format:

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

The Thymeleaf templates, static CSS, page URLs, form actions, database table, and MySQL configuration remain unchanged.

## Main Features

- MySQL database-backed CRUD
- Add, edit, delete, and update task status
- Jakarta Bean Validation
- Search and filter by status, difficulty, and importance
- Dashboard statistics and deadline status
- Mood, energy, time, deadline, difficulty, and importance based recommendation
- Recommendation match score and explanation
- Completed-task history, total study time, completion rate, and streak
- Responsive Thymeleaf interface

## Safety and Bug Fixes

- Null-safe task search and filters
- Case-insensitive status handling with allowed-value validation
- Invalid task statuses are rejected
- Overdue tasks are not counted again as due-within-two-days urgent tasks
- Completed date is assigned when a task is completed and cleared when reopened
- History chart denominator is always at least `1`, preventing division by zero
- Recommendation percentage is always kept between `0` and `100`
- Explicit constructors and accessors avoid hidden generated-code dependency in the project classes

## MySQL Configuration

Update `src/main/resources/application.properties` when your MySQL credentials are different:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/focusforge_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Dhaka
spring.datasource.username=root
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=update
```

## Database Setup

Run this file in MySQL Workbench or phpMyAdmin:

```text
database/focusforge_db.sql
```

## Run

1. Start MySQL Server on port `3306`.
2. Run `database/focusforge_db.sql`, or allow JPA to create/update the table.
3. Open the `FocusForge` folder in IntelliJ IDEA.
4. Select JDK 21 or newer.
5. Reload Maven.
6. Run `FocusForgeApplication.java`.
7. Open `http://localhost:8080/focusforge/dashboard`.

## Verification Performed

- All reorganized Java sources passed a Java 21 compiler check using framework-compatible stubs.
- In-memory execution tests passed for save, update, delete, status transition, completed date, search, filtering, urgent/overdue counts, recommendation, and history denominator behavior.
- Existing template routes and model property names were preserved.
