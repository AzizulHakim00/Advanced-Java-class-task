package com.example.bankmangament.Models;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class DataBaseDriver implements AutoCloseable {
    private final Connection conn;

    public DataBaseDriver() { this("jdbc:sqlite:mazebank.db"); }

    public DataBaseDriver(String url) {
        try {
            conn = DriverManager.getConnection(url);
            try (Statement s = conn.createStatement()) {
                s.execute("PRAGMA foreign_keys=ON");
                s.execute("PRAGMA busy_timeout=5000");
            }
            schema();
            seed();
        } catch (SQLException e) {
            throw new IllegalStateException("Could not open Maze Bank database.", e);
        }
    }

    private void schema() throws SQLException {
        try (Statement s = conn.createStatement()) {
            s.executeUpdate("CREATE TABLE IF NOT EXISTS Admins(ID INTEGER PRIMARY KEY AUTOINCREMENT,Username TEXT NOT NULL,Password TEXT NOT NULL)");
            s.executeUpdate("CREATE TABLE IF NOT EXISTS Clients(ID INTEGER PRIMARY KEY AUTOINCREMENT,FirstName TEXT NOT NULL,LastName TEXT NOT NULL,PayeeAddress TEXT NOT NULL,Password TEXT NOT NULL,Date TEXT NOT NULL)");
            s.executeUpdate("CREATE TABLE IF NOT EXISTS CheckingAccounts(ID INTEGER PRIMARY KEY AUTOINCREMENT,Owner TEXT NOT NULL,AccountNumber TEXT NOT NULL,TransactionLimit REAL NOT NULL,Balance REAL NOT NULL)");
            s.executeUpdate("CREATE TABLE IF NOT EXISTS SavingsAccounts(ID INTEGER PRIMARY KEY AUTOINCREMENT,Owner TEXT NOT NULL,AccountNumber TEXT NOT NULL,WithdrawalLimit REAL NOT NULL,Balance REAL NOT NULL)");
            s.executeUpdate("CREATE TABLE IF NOT EXISTS Transactions(ID INTEGER PRIMARY KEY AUTOINCREMENT,Sender TEXT NOT NULL,Receiver TEXT NOT NULL,Amount REAL NOT NULL,Date TEXT NOT NULL,Message TEXT)");
            s.executeUpdate("CREATE TABLE IF NOT EXISTS AccountTransfers(ID INTEGER PRIMARY KEY AUTOINCREMENT,Owner TEXT NOT NULL,FromAccount TEXT NOT NULL,ToAccount TEXT NOT NULL,Amount REAL NOT NULL,Date TEXT NOT NULL)");
            s.executeUpdate("CREATE TABLE IF NOT EXISTS BugReports(ID INTEGER PRIMARY KEY AUTOINCREMENT,PayeeAddress TEXT NOT NULL,Description TEXT NOT NULL,Date TEXT NOT NULL,Status TEXT NOT NULL DEFAULT 'OPEN')");
            index(s, "CREATE UNIQUE INDEX IF NOT EXISTS idx_clients_payee ON Clients(PayeeAddress)");
            index(s, "CREATE UNIQUE INDEX IF NOT EXISTS idx_checking_owner ON CheckingAccounts(Owner)");
            index(s, "CREATE UNIQUE INDEX IF NOT EXISTS idx_savings_owner ON SavingsAccounts(Owner)");
            index(s, "CREATE UNIQUE INDEX IF NOT EXISTS idx_checking_number ON CheckingAccounts(AccountNumber)");
            index(s, "CREATE UNIQUE INDEX IF NOT EXISTS idx_savings_number ON SavingsAccounts(AccountNumber)");
            index(s, "CREATE INDEX IF NOT EXISTS idx_tx_sender ON Transactions(Sender)");
            index(s, "CREATE INDEX IF NOT EXISTS idx_tx_receiver ON Transactions(Receiver)");
        }
    }

    private void index(Statement s, String sql) { try { s.executeUpdate(sql); } catch (SQLException ignored) {} }

    private void seed() throws SQLException {
        if (!hasRows("Admins")) {
            try (PreparedStatement p = conn.prepareStatement("INSERT INTO Admins(Username,Password) VALUES(?,?)")) {
                p.setString(1, "Admin");
                p.setString(2, PasswordUtils.hashPassword("Admin@123"));
                p.executeUpdate();
            }
        }
        if (!hasRows("Clients")) {
            OperationResult r = createClientWithAccounts("Benjamin", "Baker", "@bBaker1", "Client@123", 3000, 12000, "3201 100001", "3201 100002");
            if (!r.success()) throw new SQLException(r.message());
        }
    }

    private boolean hasRows(String table) throws SQLException {
        try (Statement s = conn.createStatement(); ResultSet r = s.executeQuery("SELECT 1 FROM " + table + " LIMIT 1")) { return r.next(); }
    }

    public synchronized boolean authenticateClient(String address, String password) { return authenticate("Clients", "PayeeAddress", address, password); }
    public synchronized boolean authenticateAdmin(String username, String password) { return authenticate("Admins", "Username", username, password); }

    private boolean authenticate(String table, String column, String identity, String raw) {
        if (blank(identity) || blank(raw)) return false;
        try (PreparedStatement p = conn.prepareStatement("SELECT Password FROM " + table + " WHERE " + column + "=?")) {
            p.setString(1, identity.trim());
            try (ResultSet r = p.executeQuery()) {
                if (!r.next()) return false;
                String stored = r.getString(1);
                boolean valid = PasswordUtils.matches(raw, stored);
                if (valid && !PasswordUtils.isHashed(stored)) {
                    try (PreparedStatement u = conn.prepareStatement("UPDATE " + table + " SET Password=? WHERE " + column + "=?")) {
                        u.setString(1, PasswordUtils.hashPassword(raw));
                        u.setString(2, identity.trim());
                        u.executeUpdate();
                    }
                }
                return valid;
            }
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public synchronized Optional<Client> findClient(String address) {
        if (blank(address)) return Optional.empty();
        try (PreparedStatement p = conn.prepareStatement("SELECT FirstName,LastName,PayeeAddress,Date FROM Clients WHERE PayeeAddress=?")) {
            p.setString(1, address.trim());
            try (ResultSet r = p.executeQuery()) {
                if (!r.next()) return Optional.empty();
                String a = r.getString("PayeeAddress");
                return Optional.of(new Client(r.getString("FirstName"), r.getString("LastName"), a, getCheckingAccount(a), getSavingsAccount(a), LocalDate.parse(r.getString("Date"))));
            }
        } catch (SQLException e) { e.printStackTrace(); return Optional.empty(); }
    }

    public synchronized List<Client> getAllClients() {
        List<Client> list = new ArrayList<>();
        try (PreparedStatement p = conn.prepareStatement("SELECT FirstName,LastName,PayeeAddress,Date FROM Clients ORDER BY ID DESC"); ResultSet r = p.executeQuery()) {
            while (r.next()) {
                String a = r.getString("PayeeAddress");
                list.add(new Client(r.getString("FirstName"), r.getString("LastName"), a, getCheckingAccount(a), getSavingsAccount(a), LocalDate.parse(r.getString("Date"))));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public synchronized CheckingAccount getCheckingAccount(String owner) {
        try (PreparedStatement p = conn.prepareStatement("SELECT AccountNumber,TransactionLimit,Balance FROM CheckingAccounts WHERE Owner=?")) {
            p.setString(1, owner);
            try (ResultSet r = p.executeQuery()) {
                if (r.next()) return new CheckingAccount(owner, r.getString(1), r.getDouble(3), r.getInt(2));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return new CheckingAccount(owner, "Not created", 0, 0);
    }

    public synchronized SavingsAccount getSavingsAccount(String owner) {
        try (PreparedStatement p = conn.prepareStatement("SELECT AccountNumber,WithdrawalLimit,Balance FROM SavingsAccounts WHERE Owner=?")) {
            p.setString(1, owner);
            try (ResultSet r = p.executeQuery()) {
                if (r.next()) return new SavingsAccount(owner, r.getString(1), r.getDouble(3), r.getDouble(2));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return new SavingsAccount(owner, "Not created", 0, 0);
    }

    public synchronized List<Transaction> getTransactions(String address, int limit) {
        List<Transaction> list = new ArrayList<>();
        String sql = "SELECT Sender,Receiver,Amount,Date,Message FROM Transactions WHERE Sender=? OR Receiver=? ORDER BY ID DESC" + (limit > 0 ? " LIMIT ?" : "");
        try (PreparedStatement p = conn.prepareStatement(sql)) {
            p.setString(1, address); p.setString(2, address); if (limit > 0) p.setInt(3, limit);
            try (ResultSet r = p.executeQuery()) {
                while (r.next()) list.add(new Transaction(r.getString(1), r.getString(2), r.getDouble(3), LocalDate.parse(r.getString(4)), Objects.toString(r.getString(5), "")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public synchronized OperationResult transferSavings(String sender, String receiver, double amount, String message) {
        if (blank(sender) || blank(receiver)) return OperationResult.fail("Sender and receiver are required.");
        if (sender.equalsIgnoreCase(receiver)) return OperationResult.fail("You cannot send money to your own payee address.");
        if (!validAmount(amount)) return OperationResult.fail("Enter a valid amount greater than zero.");
        try {
            conn.setAutoCommit(false);
            SavingsRow from = savingsRow(sender), to = savingsRow(receiver);
            if (from == null) return rollback("Your savings account was not found.");
            if (to == null || !clientExists(receiver)) return rollback("Receiver payee address was not found.");
            if (amount > from.limit) return rollback("Amount exceeds your savings withdrawal limit.");
            if (amount > from.balance) return rollback("Insufficient savings balance.");
            change("SavingsAccounts", sender, -amount); change("SavingsAccounts", receiver, amount);
            try (PreparedStatement p = conn.prepareStatement("INSERT INTO Transactions(Sender,Receiver,Amount,Date,Message) VALUES(?,?,?,?,?)")) {
                p.setString(1, sender); p.setString(2, receiver); p.setDouble(3, money(amount)); p.setString(4, LocalDate.now().toString()); p.setString(5, Objects.toString(message, "").trim()); p.executeUpdate();
            }
            conn.commit(); return OperationResult.ok("Money sent successfully.");
        } catch (SQLException e) { rollbackQuiet(); e.printStackTrace(); return OperationResult.fail("The transfer could not be completed."); }
        finally { autoCommit(); }
    }

    public synchronized OperationResult transferBetweenAccounts(String owner, boolean checkingToSavings, double amount) {
        if (!validAmount(amount)) return OperationResult.fail("Enter a valid amount greater than zero.");
        String from = checkingToSavings ? "CheckingAccounts" : "SavingsAccounts";
        String to = checkingToSavings ? "SavingsAccounts" : "CheckingAccounts";
        try {
            conn.setAutoCommit(false);
            double balance = balance(from, owner);
            if (balance < amount) return rollback("Insufficient source-account balance.");
            if (!checkingToSavings) {
                SavingsRow row = savingsRow(owner);
                if (row == null || amount > row.limit) return rollback("Amount exceeds your savings withdrawal limit.");
            }
            if (!accountExists(to, owner)) return rollback("Destination account was not found.");
            change(from, owner, -amount); change(to, owner, amount);
            try (PreparedStatement p = conn.prepareStatement("INSERT INTO AccountTransfers(Owner,FromAccount,ToAccount,Amount,Date) VALUES(?,?,?,?,?)")) {
                p.setString(1, owner); p.setString(2, checkingToSavings ? "CHECKING" : "SAVINGS"); p.setString(3, checkingToSavings ? "SAVINGS" : "CHECKING"); p.setDouble(4, money(amount)); p.setString(5, LocalDate.now().toString()); p.executeUpdate();
            }
            conn.commit(); return OperationResult.ok("Funds moved successfully.");
        } catch (SQLException e) { rollbackQuiet(); e.printStackTrace(); return OperationResult.fail("The account transfer could not be completed."); }
        finally { autoCommit(); }
    }

    public synchronized OperationResult depositSavings(String address, double amount) {
        if (!validAmount(amount)) return OperationResult.fail("Enter a valid deposit amount.");
        if (!clientExists(address) || !accountExists("SavingsAccounts", address)) return OperationResult.fail("Client savings account was not found.");
        try { change("SavingsAccounts", address, amount); return OperationResult.ok("Deposit completed successfully."); }
        catch (SQLException e) { e.printStackTrace(); return OperationResult.fail("Deposit failed."); }
    }

    public synchronized OperationResult createClientWithAccounts(String first, String last, String address, String password, double checking, double savings, String checkingNo, String savingsNo) {
        first = clean(first); last = clean(last); address = clean(address);
        if (first.isBlank() || last.isBlank()) return OperationResult.fail("First name and last name are required.");
        if (!address.matches("@[A-Za-z0-9._-]{3,30}")) return OperationResult.fail("Generate a valid payee address first.");
        if (password == null || password.length() < 6) return OperationResult.fail("Password must contain at least 6 characters.");
        if (!Double.isFinite(checking) || !Double.isFinite(savings) || checking < 0 || savings < 0) return OperationResult.fail("Opening balances cannot be negative.");
        if (blank(checkingNo) || blank(savingsNo) || checkingNo.equals(savingsNo)) return OperationResult.fail("Account numbers must be unique.");
        if (clientExists(address)) return OperationResult.fail("That payee address already exists.");
        if (accountNumberExists(checkingNo) || accountNumberExists(savingsNo)) return OperationResult.fail("An account number collision occurred. Please try again.");
        try {
            conn.setAutoCommit(false);
            try (PreparedStatement p = conn.prepareStatement("INSERT INTO Clients(FirstName,LastName,PayeeAddress,Password,Date) VALUES(?,?,?,?,?)")) {
                p.setString(1, first); p.setString(2, last); p.setString(3, address); p.setString(4, PasswordUtils.hashPassword(password)); p.setString(5, LocalDate.now().toString()); p.executeUpdate();
            }
            createAccount("CheckingAccounts", "TransactionLimit", address, checkingNo, 10, checking);
            createAccount("SavingsAccounts", "WithdrawalLimit", address, savingsNo, 2000, savings);
            conn.commit(); return OperationResult.ok("Client created successfully.");
        } catch (SQLException e) { rollbackQuiet(); e.printStackTrace(); return OperationResult.fail("Client creation failed."); }
        finally { autoCommit(); }
    }

    private void createAccount(String table, String limitColumn, String owner, String number, double limit, double amount) throws SQLException {
        try (PreparedStatement p = conn.prepareStatement("INSERT INTO " + table + "(Owner,AccountNumber," + limitColumn + ",Balance) VALUES(?,?,?,?)")) {
            p.setString(1, owner); p.setString(2, number); p.setDouble(3, limit); p.setDouble(4, money(amount)); p.executeUpdate();
        }
    }

    public synchronized OperationResult deleteClient(String address) {
        if (!clientExists(address)) return OperationResult.fail("Client was not found.");
        try {
            conn.setAutoCommit(false);
            delete("CheckingAccounts", "Owner", address); delete("SavingsAccounts", "Owner", address); delete("AccountTransfers", "Owner", address); delete("BugReports", "PayeeAddress", address);
            try (PreparedStatement p = conn.prepareStatement("DELETE FROM Transactions WHERE Sender=? OR Receiver=?")) { p.setString(1, address); p.setString(2, address); p.executeUpdate(); }
            delete("Clients", "PayeeAddress", address);
            conn.commit(); return OperationResult.ok("Client deleted successfully.");
        } catch (SQLException e) { rollbackQuiet(); e.printStackTrace(); return OperationResult.fail("Client could not be deleted."); }
        finally { autoCommit(); }
    }

    private void delete(String table, String column, String value) throws SQLException {
        try (PreparedStatement p = conn.prepareStatement("DELETE FROM " + table + " WHERE " + column + "=?")) { p.setString(1, value); p.executeUpdate(); }
    }

    public synchronized OperationResult submitBugReport(String address, String description) {
        if (blank(description)) return OperationResult.fail("Please describe the problem.");
        try (PreparedStatement p = conn.prepareStatement("INSERT INTO BugReports(PayeeAddress,Description,Date,Status) VALUES(?,?,?,'OPEN')")) {
            p.setString(1, address); p.setString(2, description.trim()); p.setString(3, LocalDate.now().toString()); p.executeUpdate(); return OperationResult.ok("Your report was submitted.");
        } catch (SQLException e) { e.printStackTrace(); return OperationResult.fail("The report could not be submitted."); }
    }

    public synchronized boolean clientExists(String address) { return exists("SELECT 1 FROM Clients WHERE PayeeAddress=?", address); }
    private boolean accountExists(String table, String owner) { return exists("SELECT 1 FROM " + table + " WHERE Owner=?", owner); }
    public synchronized boolean accountNumberExists(String number) { return exists("SELECT 1 FROM CheckingAccounts WHERE AccountNumber=?", number) || exists("SELECT 1 FROM SavingsAccounts WHERE AccountNumber=?", number); }

    private boolean exists(String sql, String value) {
        try (PreparedStatement p = conn.prepareStatement(sql)) { p.setString(1, value); try (ResultSet r = p.executeQuery()) { return r.next(); } }
        catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public synchronized int getLastClientID() {
        try (PreparedStatement p = conn.prepareStatement("SELECT COALESCE(MAX(ID),0) FROM Clients"); ResultSet r = p.executeQuery()) { return r.next() ? r.getInt(1) : 0; }
        catch (SQLException e) { e.printStackTrace(); return 0; }
    }

    private SavingsRow savingsRow(String owner) throws SQLException {
        try (PreparedStatement p = conn.prepareStatement("SELECT Balance,WithdrawalLimit FROM SavingsAccounts WHERE Owner=?")) {
            p.setString(1, owner); try (ResultSet r = p.executeQuery()) { return r.next() ? new SavingsRow(r.getDouble(1), r.getDouble(2)) : null; }
        }
    }

    private double balance(String table, String owner) throws SQLException {
        try (PreparedStatement p = conn.prepareStatement("SELECT Balance FROM " + table + " WHERE Owner=?")) { p.setString(1, owner); try (ResultSet r = p.executeQuery()) { return r.next() ? r.getDouble(1) : -1; } }
    }

    private void change(String table, String owner, double delta) throws SQLException {
        try (PreparedStatement p = conn.prepareStatement("UPDATE " + table + " SET Balance=ROUND(Balance+?,2) WHERE Owner=?")) {
            p.setDouble(1, money(delta)); p.setString(2, owner); if (p.executeUpdate() != 1) throw new SQLException("Account update failed.");
        }
    }

    private OperationResult rollback(String message) { rollbackQuiet(); return OperationResult.fail(message); }
    private void rollbackQuiet() { try { conn.rollback(); } catch (SQLException ignored) {} }
    private void autoCommit() { try { conn.setAutoCommit(true); } catch (SQLException ignored) {} }
    private boolean validAmount(double value) { return Double.isFinite(value) && value > 0; }
    private boolean blank(String value) { return value == null || value.isBlank(); }
    private String clean(String value) { return value == null ? "" : value.trim(); }
    private double money(double value) { return Math.round(value * 100.0) / 100.0; }

    @Override public synchronized void close() { try { conn.close(); } catch (SQLException e) { e.printStackTrace(); } }
    private record SavingsRow(double balance, double limit) {}
}
