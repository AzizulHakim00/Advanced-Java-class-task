# FocusForge – Smart Study Decision Assistant

A small Spring Boot + Thymeleaf class project with five screens:

1. Dashboard
2. Add/Edit Study Task
3. Task List
4. Smart Study Check-in
5. Recommendation Result

## Main Features

- ArrayList-based CRUD
- Jakarta Bean Validation
- Search and filter
- Edit, delete and mark completed
- Dashboard statistics
- Overdue, due today and upcoming deadline labels
- Quick task status controls: Start, Done and Reopen
- Mood, energy, time, deadline, difficulty and importance based recommendation
- Normalized recommendation match percentage and match label
- Responsive Thymeleaf interface

## Run

1. Open the project in IntelliJ IDEA.
2. Select JDK 21 or newer.
3. Let Maven download dependencies.
4. Run `FocusForgeApplication.java`.
5. Open `http://localhost:8080`.

Data is stored in memory, so it resets when the application restarts.
