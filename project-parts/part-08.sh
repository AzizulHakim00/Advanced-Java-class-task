#!/usr/bin/env bash
set -euo pipefail
ROOT=${ROOT:-Maze-Bank-Management-System}
cat >> "$ROOT/src/main/java/com/example/bankmangament/Models/DataBaseDriver.java" <<'__MAZE_21_3__'
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
__MAZE_21_3__
cat >> "$ROOT/src/main/java/com/example/bankmangament/Models/DataBaseDriver.java" <<'__MAZE_21_4__'
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
__MAZE_21_4__
mkdir -p "$ROOT/src/main/java/com/example/bankmangament/Models"
