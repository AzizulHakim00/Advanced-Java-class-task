#!/usr/bin/env bash
set -euo pipefail
ROOT=${ROOT:-Maze-Bank-Management-System}
cat >> "$ROOT/src/main/java/com/example/bankmangament/Models/DataBaseDriver.java" <<'__MAZE_21_2__'
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
__MAZE_21_2__
