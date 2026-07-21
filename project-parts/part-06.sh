#!/usr/bin/env bash
set -euo pipefail
ROOT=${ROOT:-Maze-Bank-Management-System}
cat >> "$ROOT/src/main/java/com/example/bankmangament/Models/DataBaseDriver.java" <<'__MAZE_21_1__'
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
__MAZE_21_1__
