# FocusForge – Smart Study Decision Assistant

FocusForge is a Spring Boot, Thymeleaf, and MySQL class project. It manages study tasks and recommends a suitable task from the student's available time, energy, mood, deadline, difficulty, and importance.

## Reorganized Code Structure

The project now follows the same layered Maven/Spring Boot format used by the provided Product Shop project:

```text
src/main/java/bd/edu/seu/classproject/
├── FocusForgeApplication.java
├── controller/
│   ├── FocusForgeController.java
│   └── HomeController.java
├── model/
│   ├── RecommendationResult.java
│   ├── StudyCheckIn.java
│   └── StudyTask.java
├── repository/
│   └── StudyTaskRepository.java
└── service/
    ├── RecommendationService.java
    └── StudyTaskService.java
```

### Layer Responsibilities

- `model` contains the JPA entity, validation rules, and form/result models.
- `repository` handles database access through `JpaRepository`.
- `service` contains CRUD, filtering, dashboard, history, and recommendation logic.
- `controller` handles routes, form validation, model attributes, and page navigation.
- `templates` and `static/css` contain the existing Thymeleaf interface and styles.

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

## MySQL Configuration

Update `src/main/resources/application.properties` when your MySQL credentials are different:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/focusforge_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Dhaka
spring.datasource.username=root
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=update
```

## Database Setup

The existing SQL file can be run in MySQL Workbench or phpMyAdmin:

```text
database/focusforge_db.sql
```

It creates the database, table, and demo records used by the dashboard.

## Run

1. Start MySQL Server on port `3306`.
2. Run `database/focusforge_db.sql`, or allow JPA to create/update the table.
3. Open the `FocusForge` folder in IntelliJ IDEA.
4. Select JDK 21 or newer.
5. Reload Maven.
6. Run `FocusForgeApplication.java`.
7. Open `http://localhost:8080/focusforge/dashboard`.
