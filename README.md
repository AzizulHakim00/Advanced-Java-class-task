# Maze Bank Management System — Completed Project

This branch contains a completed JavaFX and SQLite banking-management project. It is kept on an isolated branch so the repository's existing `main` branch remains untouched.

## Get the source

The source is materialized automatically by GitHub Actions into the `Maze-Bank-Management-System` directory. It can also be recreated manually with:

```bash
bash rebuild-project.sh
```

Then run:

```bash
cd Maze-Bank-Management-System
mvn clean test
mvn javafx:run
```

## Demo accounts

- Administrator: `Admin` / `Admin@123`
- Client: `@bBaker1` / `Client@123`

## Completed work

- Fixed incorrect sender/receiver balance logic.
- Added atomic client-to-client and checking-to-savings transfers.
- Added balance, receiver, self-transfer, amount and withdrawal-limit validation.
- Replaced dynamic login SQL with prepared statements.
- Added PBKDF2 password hashing with legacy-password migration.
- Completed account transfer, profile, report-error and delete-client features.
- Fixed savings opening balance and recent-transaction ordering.
- Added automatic database schema/demo initialization.
- Added JUnit tests and GitHub Actions validation.

The downloadable ZIP checksum is `b3cb2019676bd5b0fcfa61f6f4398fc18c345893ec175707a5c59402f42e4fca`.
