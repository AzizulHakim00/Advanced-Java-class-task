# PrintPulse – Southeast University Campus Print Queue

A small Spring Boot + Thymeleaf + MySQL class project for managing campus print requests, queue status, automatic cost calculation, and completed print history.

## Main Features

- New print request with Jakarta Bean Validation
- Automatic cost calculation using pages, copies, print type, and paper size
- Search by request ID, student ID, student name, or document name
- Filter by status and print type
- Status workflow: Waiting → Printing → Ready → Collected
- Dashboard statistics and active print queue
- Request details and editable print jobs
- Completed history, printed pages, color-job count, and revenue
- MySQL database persistence

## Simple Code Structure

- `PrintRequest.java` – JPA entity, validation, and cost calculation
- `PrintRequestInterface.java` – JpaRepository
- `PrintRequestController.java` – CRUD, filters, dashboard, status, and history
- Plain Thymeleaf templates with inline CSS
- No login, DTO, REST API, JavaScript framework, or service layer

## MySQL Settings

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/printpulse_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Dhaka
spring.datasource.username=root
spring.datasource.password=password
```

Change the password in `src/main/resources/application.properties` when needed.

## Run

1. Start MySQL Server.
2. Run `database/printpulse_db.sql` in MySQL Workbench.
3. Open the `PrintPulse` folder in IntelliJ IDEA.
4. Select JDK 26 and reload Maven.
5. Run `PrintPulseApplication.java`.
6. Open `http://localhost:8081/printpulse/dashboard`.
