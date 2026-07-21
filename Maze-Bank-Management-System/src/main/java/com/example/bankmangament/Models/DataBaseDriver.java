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

    private boolean authenticate(String table, String identityColumn, String identity, String rawPassword) {
        if (identity == null || identity.isBlank() || rawPassword == null || rawPassword.isBlank()) {
            return false;
        }
        String selectSql = "SELECT Password FROM " + table + " WHERE " + identityColumn + " = ?";
        String storedPassword;
        try (PreparedStatement statement = conn.prepareStatement(selectSql)) {
            statement.setString(1, identity.trim());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return false;
                }
                storedPassword = resultSet.getString("Password");
            }
            boolean valid = PasswordUtils.matches(rawPassword, storedPassword);
            if (valid && !PasswordUtils.isHashed(storedPassword)) {
                upgradePassword(table, identityColumn, identity.trim(), rawPassword);
            }
            return valid;
        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }
    }

    private void upgradePassword(String table, String identityColumn, String identity, String rawPassword) {
        String sql = "UPDATE " + table + " SET Password = ? WHERE " + identityColumn + " = ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, PasswordUtils.hashPassword(rawPassword));
            statement.setString(2, identity);
            statement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public synchronized Optional<Client> findClient(String payeeAddress) {
        if (payeeAddress == null || payeeAddress.isBlank()) {
            return Optional.empty();
        }
        String sql = "SELECT FirstName, LastName, PayeeAddress, Date FROM Clients WHERE PayeeAddress = ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, payeeAddress.trim());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return Optional.empty();
                }
                String address = resultSet.getString("PayeeAddress");
                return Optional.of(new Client(
                        resultSet.getString("FirstName"),
                        resultSet.getString("LastName"),
                        address,
                        getCheckingAccount(address),
                        getSavingsAccount(address),
                        LocalDate.parse(resultSet.getString("Date"))
                ));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            return Optional.empty();
        }
    }

    public synchronized List<Client> getAllClients() {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT FirstName, LastName, PayeeAddress, Date FROM Clients ORDER BY ID DESC";
        try (PreparedStatement statement = conn.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                String address = resultSet.getString("PayeeAddress");
                clients.add(new Client(
                        resultSet.getString("FirstName"),
                        resultSet.getString("LastName"),
                        address,
                        getCheckingAccount(address),
                        getSavingsAccount(address),
                        LocalDate.parse(resultSet.getString("Date"))
                ));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return clients;
    }

    public synchronized CheckingAccount getCheckingAccount(String payeeAddress) {
        String sql = "SELECT AccountNumber, TransactionLimit, Balance FROM CheckingAccounts WHERE Owner = ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, payeeAddress);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new CheckingAccount(
                            payeeAddress,
                            resultSet.getString("AccountNumber"),
                            resultSet.getDouble("Balance"),
                            resultSet.getInt("TransactionLimit")
                    );
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return new CheckingAccount(payeeAddress, "Not created", 0, 0);
    }

    public synchronized SavingsAccount getSavingsAccount(String payeeAddress) {
        String sql = "SELECT AccountNumber, WithdrawalLimit, Balance FROM SavingsAccounts WHERE Owner = ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, payeeAddress);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new SavingsAccount(
                            payeeAddress,
                            resultSet.getString("AccountNumber"),
                            resultSet.getDouble("Balance"),
                            resultSet.getDouble("WithdrawalLimit")
                    );
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return new SavingsAccount(payeeAddress, "Not created", 0, 0);
    }

    public synchronized List<Transaction> getTransactions(String payeeAddress, int limit) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT Sender, Receiver, Amount, Date, Message FROM Transactions "
                + "WHERE Sender = ? OR Receiver = ? ORDER BY ID DESC"
                + (limit > 0 ? " LIMIT ?" : "");
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, payeeAddress);
            statement.setString(2, payeeAddress);
            if (limit > 0) {
                statement.setInt(3, limit);
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    transactions.add(new Transaction(
                            resultSet.getString("Sender"),
                            resultSet.getString("Receiver"),
                            resultSet.getDouble("Amount"),
                            LocalDate.parse(resultSet.getString("Date")),
                            resultSet.getString("Message") == null ? "" : resultSet.getString("Message")
                    ));
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return transactions;
    }

    public synchronized OperationResult transferSavings(String sender, String receiver, double amount, String message) {
        if (sender == null || receiver == null || sender.isBlank() || receiver.isBlank()) {
            return OperationResult.fail("Sender and receiver are required.");
        }
        if (sender.equalsIgnoreCase(receiver)) {
            return OperationResult.fail("You cannot send money to your own payee address.");
        }
        if (!isValidAmount(amount)) {
            return OperationResult.fail("Enter a valid amount greater than zero.");
        }

        try {
            conn.setAutoCommit(false);
            SavingsSnapshot senderAccount = readSavingsSnapshot(sender);
            SavingsSnapshot receiverAccount = readSavingsSnapshot(receiver);
            if (senderAccount == null) {
                rollbackQuietly();
                return OperationResult.fail("Your savings account was not found.");
            }
            if (receiverAccount == null || !clientExists(receiver)) {
                rollbackQuietly();
                return OperationResult.fail("Receiver payee address was not found.");
            }
            if (amount > senderAccount.withdrawalLimit()) {
                rollbackQuietly();
                return OperationResult.fail("Amount exceeds your savings withdrawal limit of $" + money(senderAccount.withdrawalLimit()) + ".");
            }
            if (senderAccount.balance() < amount) {
                rollbackQuietly();
                return OperationResult.fail("Insufficient savings balance.");
            }

            updateAccountBalance("SavingsAccounts", sender, -amount);
            updateAccountBalance("SavingsAccounts", receiver, amount);
            insertTransaction(sender, receiver, amount, message == null ? "" : message.trim());
            conn.commit();
            return OperationResult.ok("Money sent successfully.");
        } catch (SQLException exception) {
            rollbackQuietly();
            exception.printStackTrace();
            return OperationResult.fail("The transfer could not be completed.");
        } finally {
            restoreAutoCommit();
        }
    }

    public synchronized OperationResult transferBetweenAccounts(String owner, boolean checkingToSavings, double amount) {
        if (!isValidAmount(amount)) {
            return OperationResult.fail("Enter a valid amount greater than zero.");
        }
        String fromTable = checkingToSavings ? "CheckingAccounts" : "SavingsAccounts";
        String toTable = checkingToSavings ? "SavingsAccounts" : "CheckingAccounts";
        String fromLabel = checkingToSavings ? "CHECKING" : "SAVINGS";
        String toLabel = checkingToSavings ? "SAVINGS" : "CHECKING";

        try {
            conn.setAutoCommit(false);
            double sourceBalance = readBalance(fromTable, owner);
            if (sourceBalance < amount) {
                rollbackQuietly();
                return OperationResult.fail("Insufficient " + fromLabel.toLowerCase() + " balance.");
            }
            if (!checkingToSavings) {
                SavingsSnapshot savings = readSavingsSnapshot(owner);
                if (savings == null || amount > savings.withdrawalLimit()) {
                    rollbackQuietly();
                    return OperationResult.fail("Amount exceeds your savings withdrawal limit.");
                }
            }
            if (!accountExists(toTable, owner)) {
                rollbackQuietly();
                return OperationResult.fail("Destination account was not found.");
            }

            updateAccountBalance(fromTable, owner, -amount);
            updateAccountBalance(toTable, owner, amount);
            try (PreparedStatement statement = conn.prepareStatement(
                    "INSERT INTO AccountTransfers (Owner, FromAccount, ToAccount, Amount, Date) VALUES (?, ?, ?, ?, ?)")) {
                statement.setString(1, owner);
                statement.setString(2, fromLabel);
                statement.setString(3, toLabel);
                statement.setDouble(4, roundMoney(amount));
                statement.setString(5, LocalDate.now().toString());
                statement.executeUpdate();
            }
            conn.commit();
            return OperationResult.ok("Funds moved successfully.");
        } catch (SQLException exception) {
            rollbackQuietly();
            exception.printStackTrace();
            return OperationResult.fail("The account transfer could not be completed.");
        } finally {
            restoreAutoCommit();
        }
    }

    public synchronized OperationResult depositSavings(String payeeAddress, double amount) {
        if (!isValidAmount(amount)) {
            return OperationResult.fail("Enter a valid deposit amount.");
        }
        if (!clientExists(payeeAddress) || !accountExists("SavingsAccounts", payeeAddress)) {
            return OperationResult.fail("Client savings account was not found.");
        }
        try {
            updateAccountBalance("SavingsAccounts", payeeAddress, amount);
            return OperationResult.ok("Deposit completed successfully.");
        } catch (SQLException exception) {
            exception.printStackTrace();
            return OperationResult.fail("Deposit failed.");
        }
    }

    public synchronized OperationResult createClientWithAccounts(
            String firstName,
            String lastName,
            String payeeAddress,
            String password,
            double checkingBalance,
            double savingsBalance,
            String checkingAccountNumber,
            String savingsAccountNumber
    ) {
        firstName = clean(firstName);
        lastName = clean(lastName);
        payeeAddress = clean(payeeAddress);
        if (firstName.isBlank() || lastName.isBlank()) {
            return OperationResult.fail("First name and last name are required.");
        }
        if (payeeAddress.isBlank() || !payeeAddress.matches("@[A-Za-z0-9._-]{3,30}")) {
            return OperationResult.fail("Generate a valid payee address first.");
        }
        if (password == null || password.length() < 6) {
            return OperationResult.fail("Password must contain at least 6 characters.");
        }
        if (checkingBalance < 0 || savingsBalance < 0
                || !Double.isFinite(checkingBalance) || !Double.isFinite(savingsBalance)) {
            return OperationResult.fail("Opening balances cannot be negative.");
        }
        if (checkingAccountNumber == null || checkingAccountNumber.isBlank()
                || savingsAccountNumber == null || savingsAccountNumber.isBlank()
                || checkingAccountNumber.equals(savingsAccountNumber)) {
            return OperationResult.fail("Checking and savings account numbers must be unique.");
        }
        if (clientExists(payeeAddress)) {
            return OperationResult.fail("That payee address already exists.");
        }
        if (accountNumberExists(checkingAccountNumber) || accountNumberExists(savingsAccountNumber)) {
            return OperationResult.fail("An account number collision occurred. Please try again.");
        }

        try {
            conn.setAutoCommit(false);
            try (PreparedStatement statement = conn.prepareStatement(
                    "INSERT INTO Clients (FirstName, LastName, PayeeAddress, Password, Date) VALUES (?, ?, ?, ?, ?)")) {
                statement.setString(1, firstName);
                statement.setString(2, lastName);
                statement.setString(3, payeeAddress);
                statement.setString(4, PasswordUtils.hashPassword(password));
                statement.setString(5, LocalDate.now().toString());
                statement.executeUpdate();
            }
            createAccount("CheckingAccounts", payeeAddress, checkingAccountNumber, 10, checkingBalance);
            createAccount("SavingsAccounts", payeeAddress, savingsAccountNumber, 2_000, savingsBalance);
            conn.commit();
            return OperationResult.ok("Client created successfully.");
        } catch (SQLException exception) {
            rollbackQuietly();
            exception.printStackTrace();
            return OperationResult.fail("Client creation failed.");
        } finally {
            restoreAutoCommit();
        }
    }

    private void createAccount(String table, String owner, String number, double limit, double balance) throws SQLException {
        String limitColumn = table.equals("CheckingAccounts") ? "TransactionLimit" : "WithdrawalLimit";
        String sql = "INSERT INTO " + table + " (Owner, AccountNumber, " + limitColumn + ", Balance) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, owner);
            statement.setString(2, number);
            statement.setDouble(3, limit);
            statement.setDouble(4, roundMoney(balance));
            statement.executeUpdate();
        }
    }

    public synchronized OperationResult deleteClient(String payeeAddress) {
        if (!clientExists(payeeAddress)) {
            return OperationResult.fail("Client was not found.");
        }
        try {
            conn.setAutoCommit(false);
            deleteByOwner("CheckingAccounts", payeeAddress);
            deleteByOwner("SavingsAccounts", payeeAddress);
            try (PreparedStatement statement = conn.prepareStatement(
                    "DELETE FROM Transactions WHERE Sender = ? OR Receiver = ?")) {
                statement.setString(1, payeeAddress);
                statement.setString(2, payeeAddress);
                statement.executeUpdate();
            }
            deleteByOwner("AccountTransfers", payeeAddress);
            try (PreparedStatement statement = conn.prepareStatement("DELETE FROM BugReports WHERE PayeeAddress = ?")) {
                statement.setString(1, payeeAddress);
                statement.executeUpdate();
            }
            try (PreparedStatement statement = conn.prepareStatement("DELETE FROM Clients WHERE PayeeAddress = ?")) {
                statement.setString(1, payeeAddress);
                statement.executeUpdate();
            }
            conn.commit();
            return OperationResult.ok("Client deleted successfully.");
        } catch (SQLException exception) {
            rollbackQuietly();
            exception.printStackTrace();
            return OperationResult.fail("Client could not be deleted.");
        } finally {
            restoreAutoCommit();
        }
    }

    private void deleteByOwner(String table, String owner) throws SQLException {
        try (PreparedStatement statement = conn.prepareStatement("DELETE FROM " + table + " WHERE Owner = ?")) {
            statement.setString(1, owner);
            statement.executeUpdate();
        }
    }

    public synchronized OperationResult submitBugReport(String payeeAddress, String description) {
        if (description == null || description.isBlank()) {
            return OperationResult.fail("Please describe the problem.");
        }
        try (PreparedStatement statement = conn.prepareStatement(
                "INSERT INTO BugReports (PayeeAddress, Description, Date, Status) VALUES (?, ?, ?, 'OPEN')")) {
            statement.setString(1, payeeAddress);
            statement.setString(2, description.trim());
            statement.setString(3, LocalDate.now().toString());
            statement.executeUpdate();
            return OperationResult.ok("Your report was submitted.");
        } catch (SQLException exception) {
            exception.printStackTrace();
            return OperationResult.fail("The report could not be submitted.");
        }
    }

    public synchronized boolean clientExists(String payeeAddress) {
        return exists("SELECT 1 FROM Clients WHERE PayeeAddress = ?", payeeAddress);
    }

    private boolean accountExists(String table, String owner) {
        return exists("SELECT 1 FROM " + table + " WHERE Owner = ?", owner);
    }

    public synchronized boolean accountNumberExists(String accountNumber) {
        return exists("SELECT 1 FROM CheckingAccounts WHERE AccountNumber = ?", accountNumber)
                || exists("SELECT 1 FROM SavingsAccounts WHERE AccountNumber = ?", accountNumber);
    }

    private boolean exists(String sql, String value) {
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, value);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public synchronized int getLastClientID() {
        try (PreparedStatement statement = conn.prepareStatement("SELECT COALESCE(MAX(ID), 0) AS ID FROM Clients");
             ResultSet resultSet = statement.executeQuery()) {
            return resultSet.next() ? resultSet.getInt("ID") : 0;
        } catch (SQLException exception) {
            exception.printStackTrace();
            return 0;
        }
    }

    private SavingsSnapshot readSavingsSnapshot(String owner) throws SQLException {
        String sql = "SELECT Balance, WithdrawalLimit FROM SavingsAccounts WHERE Owner = ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, owner);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new SavingsSnapshot(resultSet.getDouble("Balance"), resultSet.getDouble("WithdrawalLimit"));
                }
                return null;
            }
        }
    }

    private double readBalance(String table, String owner) throws SQLException {
        String sql = "SELECT Balance FROM " + table + " WHERE Owner = ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, owner);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? resultSet.getDouble("Balance") : -1;
            }
        }
    }

    private void updateAccountBalance(String table, String owner, double delta) throws SQLException {
        String sql = "UPDATE " + table + " SET Balance = ROUND(Balance + ?, 2) WHERE Owner = ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setDouble(1, roundMoney(delta));
            statement.setString(2, owner);
            if (statement.executeUpdate() != 1) {
                throw new SQLException("Account update failed for " + owner);
            }
        }
    }

    private void insertTransaction(String sender, String receiver, double amount, String message) throws SQLException {
        try (PreparedStatement statement = conn.prepareStatement(
                "INSERT INTO Transactions (Sender, Receiver, Amount, Date, Message) VALUES (?, ?, ?, ?, ?)")) {
            statement.setString(1, sender);
            statement.setString(2, receiver);
            statement.setDouble(3, roundMoney(amount));
            statement.setString(4, LocalDate.now().toString());
            statement.setString(5, message);
            statement.executeUpdate();
        }
    }

    private void rollbackQuietly() {
        try {
            conn.rollback();
        } catch (SQLException ignored) {
        }
    }

    private void restoreAutoCommit() {
        try {
            conn.setAutoCommit(true);
        } catch (SQLException ignored) {
        }
    }

    private boolean isValidAmount(double amount) {
        return Double.isFinite(amount) && amount > 0;
    }

    private double roundMoney(double amount) {
        return Math.round(amount * 100.0) / 100.0;
    }

    private String money(double amount) {
        return String.format("%.2f", amount);
    }

    private String clean(String value) {
        return value == null ? "" : value.trim();
    }

    @Override
    public synchronized void close() {
        try {
            conn.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    private record SavingsSnapshot(double balance, double withdrawalLimit) {
    }
}
