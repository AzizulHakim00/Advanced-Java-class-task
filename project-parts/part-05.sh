#!/usr/bin/env bash
set -euo pipefail
ROOT=${ROOT:-Maze-Bank-Management-System}
cat > "$ROOT/src/main/java/com/example/bankmangament/Models/DataBaseDriver.java" <<'__MAZE_21_0__'
package com.example.bankmangament.Models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DataBaseDriver implements AutoCloseable {
    private static final String DATABASE_URL = "jdbc:sqlite:mazebank.db";
    private final Connection conn;

    public DataBaseDriver() {
        this(DATABASE_URL);
    }

    public DataBaseDriver(String databaseUrl) {
        try {
            this.conn = DriverManager.getConnection(databaseUrl);
            try (Statement statement = conn.createStatement()) {
                statement.execute("PRAGMA foreign_keys = ON");
                statement.execute("PRAGMA busy_timeout = 5000");
            }
            initializeSchema();
            seedDemoDataIfEmpty();
        } catch (SQLException exception) {
            throw new IllegalStateException("Could not open Maze Bank database.", exception);
        }
    }

    private void initializeSchema() throws SQLException {
        try (Statement statement = conn.createStatement()) {
            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS Admins (
                        ID INTEGER PRIMARY KEY AUTOINCREMENT,
                        Username TEXT NOT NULL,
                        Password TEXT NOT NULL
                    )
                    """);
            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS Clients (
                        ID INTEGER PRIMARY KEY AUTOINCREMENT,
                        FirstName TEXT NOT NULL,
                        LastName TEXT NOT NULL,
                        PayeeAddress TEXT NOT NULL,
                        Password TEXT NOT NULL,
                        Date TEXT NOT NULL
                    )
                    """);
            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS CheckingAccounts (
                        ID INTEGER PRIMARY KEY AUTOINCREMENT,
                        Owner TEXT NOT NULL,
                        AccountNumber TEXT NOT NULL,
                        TransactionLimit REAL NOT NULL,
                        Balance REAL NOT NULL
                    )
                    """);
            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS SavingsAccounts (
                        ID INTEGER PRIMARY KEY AUTOINCREMENT,
                        Owner TEXT NOT NULL,
                        AccountNumber TEXT NOT NULL,
                        WithdrawalLimit REAL NOT NULL,
                        Balance REAL NOT NULL
                    )
                    """);
            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS Transactions (
                        ID INTEGER PRIMARY KEY AUTOINCREMENT,
                        Sender TEXT NOT NULL,
                        Receiver TEXT NOT NULL,
                        Amount REAL NOT NULL,
                        Date TEXT NOT NULL,
                        Message TEXT
                    )
                    """);
            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS AccountTransfers (
                        ID INTEGER PRIMARY KEY AUTOINCREMENT,
                        Owner TEXT NOT NULL,
                        FromAccount TEXT NOT NULL,
                        ToAccount TEXT NOT NULL,
                        Amount REAL NOT NULL,
                        Date TEXT NOT NULL
                    )
                    """);
            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS BugReports (
                        ID INTEGER PRIMARY KEY AUTOINCREMENT,
                        PayeeAddress TEXT NOT NULL,
                        Description TEXT NOT NULL,
                        Date TEXT NOT NULL,
                        Status TEXT NOT NULL DEFAULT 'OPEN'
                    )
                    """);
            createIndexQuietly(statement, "CREATE UNIQUE INDEX IF NOT EXISTS idx_clients_payee ON Clients(PayeeAddress)");
            createIndexQuietly(statement, "CREATE UNIQUE INDEX IF NOT EXISTS idx_checking_owner ON CheckingAccounts(Owner)");
            createIndexQuietly(statement, "CREATE UNIQUE INDEX IF NOT EXISTS idx_savings_owner ON SavingsAccounts(Owner)");
            createIndexQuietly(statement, "CREATE UNIQUE INDEX IF NOT EXISTS idx_checking_number ON CheckingAccounts(AccountNumber)");
            createIndexQuietly(statement, "CREATE UNIQUE INDEX IF NOT EXISTS idx_savings_number ON SavingsAccounts(AccountNumber)");
            createIndexQuietly(statement, "CREATE INDEX IF NOT EXISTS idx_transactions_sender ON Transactions(Sender)");
            createIndexQuietly(statement, "CREATE INDEX IF NOT EXISTS idx_transactions_receiver ON Transactions(Receiver)");
        }
    }

    private void seedDemoDataIfEmpty() throws SQLException {
        if (!tableHasRows("Admins")) {
            try (PreparedStatement statement = conn.prepareStatement(
                    "INSERT INTO Admins (Username, Password) VALUES (?, ?)")) {
                statement.setString(1, "Admin");
                statement.setString(2, PasswordUtils.hashPassword("Admin@123"));
                statement.executeUpdate();
            }
        }

        if (!tableHasRows("Clients")) {
            OperationResult result = createClientWithAccounts(
                    "Benjamin",
                    "Baker",
                    "@bBaker1",
                    "Client@123",
                    3_000,
                    12_000,
                    "3201 100001",
                    "3201 100002"
            );
            if (!result.success()) {
                throw new SQLException("Could not create demo client: " + result.message());
            }
        }
    }

    private boolean tableHasRows(String table) throws SQLException {
        try (Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT 1 FROM " + table + " LIMIT 1")) {
            return resultSet.next();
        }
    }

    private void createIndexQuietly(Statement statement, String sql) {
        try {
            statement.executeUpdate(sql);
        } catch (SQLException ignored) {
            // Existing legacy databases may contain duplicate rows. Core operations still work.
        }
    }

    public synchronized boolean authenticateClient(String payeeAddress, String password) {
        return authenticate("Clients", "PayeeAddress", payeeAddress, password);
    }

    public synchronized boolean authenticateAdmin(String username, String password) {
        return authenticate("Admins", "Username", username, password);
    }

__MAZE_21_0__
