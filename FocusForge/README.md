# FocusForge – Smart Study Decision Assistant

A Spring Boot + Thymeleaf + MySQL class project with five screens:

1. Dashboard
2. Add/Edit Study Task
3. Task List
4. Smart Study Check-in
5. Recommendation Result

## Main Features

- MySQL database-backed CRUD
- Spring Data JPA repository
- MySQL Connector/J driver
- Jakarta Bean Validation
- Search and filter
- Edit, delete and quick status update
- Dashboard statistics
- Overdue, due today and upcoming deadline labels
- Mood, energy, time, deadline, difficulty and importance-based recommendation
- Recommendation match percentage and label
- Responsive Thymeleaf interface

## MySQL Configuration

The project is configured with:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/focusforge_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Dhaka
spring.datasource.username=root
spring.datasource.password=password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
```

Change the username or password in `src/main/resources/application.properties` when your local MySQL credentials are different.

## Database Setup

### Automatic method

Start MySQL and run the application. The JDBC URL contains `createDatabaseIfNotExist=true`, and Hibernate creates or updates the `study_tasks` table automatically.

### Manual method

Open MySQL Workbench or phpMyAdmin and run:

```text
database/focusforge_db.sql
```

The script creates:

- Database: `focusforge_db`
- Table: `study_tasks`
- Primary key: `task_id`

## Run

1. Start MySQL Server.
2. Confirm the MySQL port is `3306`.
3. Open the `FocusForge` project in IntelliJ IDEA.
4. Select JDK 21 or newer.
5. Reload Maven so it downloads JPA and MySQL Connector/J.
6. Update the MySQL username/password when needed.
7. Run `FocusForgeApplication.java`.
8. Open `http://localhost:8080/focusforge/dashboard`.

Tasks are stored permanently in MySQL and remain available after restarting the application.
