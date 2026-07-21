# Maze Bank Management System

Maze Bank is a JavaFX desktop banking simulation with separate client and administrator dashboards. It uses SQLite for local persistence and Maven for builds.

> This is an academic/demo application. It is not intended to process real money or store real banking information.

## Completed features

### Client

- Secure client login with PBKDF2 password hashing
- Legacy plain-text password migration after successful login
- Checking and savings account overview
- Live balance updates
- Send money to another registered client
- Atomic transfers with rollback on failure
- Receiver validation, self-transfer protection and insufficient-balance checks
- Savings withdrawal-limit validation
- Checking-to-savings and savings-to-checking transfers
- Latest and complete transaction history, ordered newest first
- Income and expense summary
- Client profile dialog
- Database-backed bug reports
- Logout and session cleanup

### Administrator

- Secure administrator login
- Create clients with checking and savings accounts
- Independent opening balances for both accounts
- Unique payee addresses and account numbers
- Password validation and hashing
- Browse all clients
- Search clients by payee address
- Additive deposits into savings accounts
- Delete clients and their related account data
- Form validation and clear error messages

### Engineering improvements

- Prepared statements instead of SQL string concatenation
- Atomic database transactions for money movement
- PBKDF2-HMAC-SHA256 password storage
- Database indexes and audit tables
- Automated JUnit tests for passwords and core banking operations
- GitHub Actions Maven build
- Cleaned FXML labels and fixed incomplete controllers

## Technology stack

- Java 21
- JavaFX 21.0.6
- FXML and CSS
- SQLite
- Maven Wrapper
- JUnit 5

## Demo login

On first launch, the automatically created database contains these demo accounts:

| Role | Username / Payee address | Password |
|---|---|---|
| Administrator | `Admin` | `Admin@123` |
| Client | `@bBaker1` | `Client@123` |

Change demo credentials before sharing the database publicly with real information.

## Run in IntelliJ IDEA

1. Open the folder containing `pom.xml`.
2. Select JDK 21 as the project SDK.
3. Allow Maven to download dependencies.
4. Run `com.example.bankmangament.App`.

## Run from a terminal

Run with Maven:

```bash
mvn clean javafx:run
```

The application creates `mazebank.db` automatically beside `pom.xml` on first launch and seeds the demo accounts.

## Run tests

```bash
mvn clean test
```

## Project structure

```text
src/main/java/com/example/bankmangament/
├── Controllers/
│   ├── Admin/
│   └── Client/
├── Models/
├── Views/
└── App.java

src/main/resources/
├── com/example/bankmangament/
│   ├── Admin/
│   ├── Client/
│   └── Login.fxml
├── Images/
└── Style/
```

## Database tables

- `Admins`
- `Clients`
- `CheckingAccounts`
- `SavingsAccounts`
- `Transactions`
- `AccountTransfers`
- `BugReports`

## Important note

Money values use SQLite `REAL` fields because this project inherited that schema. A production-grade financial system should use fixed-precision decimal values, comprehensive authorization, encrypted backups, immutable audit logging, multi-factor authentication and an audited server-side architecture.
