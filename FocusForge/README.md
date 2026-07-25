# FocusForge – Smart Study Decision Assistant

A simple Spring Boot + Thymeleaf + MySQL class project built in the same style as the Product Shop CRUD project.

## Screens

1. Dashboard
2. Add/Edit Study Task
3. Task List
4. Smart Study Check-in
5. Recommendation Result
6. History and Progress

## Simple Code Structure

- `StudyTask.java` – JPA entity and validation
- `StudyTaskInterface.java` – `JpaRepository`
- `FocusForgeController.java` – CRUD, search, dashboard, recommendation and history logic
- `StudyCheckIn.java` – check-in form data
- `RecommendationResult.java` – recommendation result data
- Plain Thymeleaf HTML and CSS
- No login, DTO, REST API, JavaScript framework or external chart library

## Main Features

- MySQL database-backed CRUD
- Add, edit and delete tasks
- Search and filter by status, difficulty and importance
- Start, complete and reopen task controls
- Dashboard statistics and deadline status
- Productivity ring and task overview chart using CSS
- Mood, energy, time and deadline-based smart recommendation
- Recommendation match score and explanation
- Completed-task history, total study time, completion rate and streak
- Responsive interface matching the supplied demo design

## MySQL Configuration

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/focusforge_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Dhaka
spring.datasource.username=root
spring.datasource.password=password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
```

Change the username or password in `src/main/resources/application.properties` when your MySQL credentials are different.

## Database Setup

Run this file in MySQL Workbench or phpMyAdmin:

```text
database/focusforge_db.sql
```

The SQL file creates the database, table and demo records needed to preview all dashboard sections.

## Run

1. Start MySQL Server on port `3306`.
2. Run `database/focusforge_db.sql`.
3. Open the `FocusForge` folder in IntelliJ IDEA.
4. Select JDK 21 or newer.
5. Reload Maven.
6. Run `FocusForgeApplication.java`.
7. Open `http://localhost:8080/focusforge/dashboard`.

All task data is stored in MySQL and remains after restarting the application.
