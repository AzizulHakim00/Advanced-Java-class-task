# PrintPulse – Southeast University Campus Print Queue

A Spring Boot + Thymeleaf + MongoDB Atlas class project for managing campus print requests, queue status, automatic cost calculation, and completed print history.

## Main Features

- New print request with Jakarta Bean Validation
- Automatic cost calculation using pages, copies, print type, and paper size
- Search by request ID, student ID, student name, or document name
- Filter by status and print type
- Status workflow: Waiting → Printing → Ready → Collected
- Dashboard statistics and active print queue
- Request details and editable print jobs
- Completed history, printed pages, color-job count, and revenue
- MongoDB Atlas persistence

## MongoDB Structure

PrintPulse uses the existing Atlas deployment and stores data in:

- Database: `print_db`
- Collection: `print`
- MongoDB `_id`: `requestId` (`Integer`)

`PrintRequest.java` is mapped with `@Document(collection = "print")`, and `PrintRequestInterface` extends `MongoRepository<PrintRequest, Integer>`.

## MongoDB Connection Setup

The application reads the Atlas connection string from the `MONGODB_URI` environment variable. This avoids storing the database password in the project source code.

The URI must point to `print_db`, for example:

```text
mongodb+srv://<USERNAME>:<ENCODED_PASSWORD>@ase-naki-mongo.vcdwgus.mongodb.net/print_db?appName=Ase-Naki-Mongo
```

If the password contains reserved URI characters such as `@`, `#`, `:`, `/`, `?`, `[` or `]`, URL-encode them before placing the password in the URI.

### Windows PowerShell

Set the variable for the current PowerShell session:

```powershell
$env:MONGODB_URI="mongodb+srv://<USERNAME>:<ENCODED_PASSWORD>@ase-naki-mongo.vcdwgus.mongodb.net/print_db?appName=Ase-Naki-Mongo"
```

Then run the application from the same environment/IDE configuration.

### IntelliJ IDEA

1. Open **Run → Edit Configurations**.
2. Select `PrintPulseApplication`.
3. Add an environment variable named `MONGODB_URI` containing the Atlas connection string.
4. Make sure the URI ends with `/print_db` before the query string.
5. Run the application.

Also make sure the current computer's IP address is allowed in MongoDB Atlas **Network Access**.

## Simple Code Structure

- `PrintRequest.java` – MongoDB document, validation, and cost calculation
- `PrintRequestInterface.java` – `MongoRepository`
- `PrintRequestController.java` – CRUD, filters, dashboard, status, and history
- Thymeleaf templates and CSS remain unchanged
- No SQL server or MySQL schema is required

## Run

1. Ensure the Atlas deployment is running.
2. Confirm `print_db` and the `print` collection exist (MongoDB can also create them when the first document is inserted).
3. Add your current IP in Atlas Network Access.
4. Set `MONGODB_URI` as described above.
5. Open the `PrintPulse` folder in IntelliJ IDEA.
6. Select JDK 26 and reload Maven.
7. Run `PrintPulseApplication.java`.
8. Open `http://localhost:8081/printpulse/dashboard`.
