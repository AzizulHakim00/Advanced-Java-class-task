#!/usr/bin/env bash
set -euo pipefail
ROOT=${ROOT:-Maze-Bank-Management-System}
cat > "$ROOT/src/main/java/com/example/bankmangament/Models/Model.java" <<'__MAZE_22_0__'
package com.example.bankmangament.Models;

import com.example.bankmangament.Views.Viewfactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Optional;

public class Model {
    private static Model model;

    private final Viewfactory viewfactory;
    private final DataBaseDriver dataBaseDriver;
    private final Client client;
    private final ObservableList<Transaction> latestTransactions;
    private final ObservableList<Transaction> allTransactions;
    private final ObservableList<Client> clients;

    private boolean clientLoginSuccessFlag;
    private boolean adminLoginSuccessFlag;

    private Model() {
        this.dataBaseDriver = new DataBaseDriver();
        this.viewfactory = new Viewfactory();
        this.client = new Client(
                "",
                "",
                "",
                new CheckingAccount("", "Not created", 0, 0),
                new SavingsAccount("", "Not created", 0, 0),
                null
        );
        this.latestTransactions = FXCollections.observableArrayList();
        this.allTransactions = FXCollections.observableArrayList();
        this.clients = FXCollections.observableArrayList();
    }

    public static synchronized Model getInstance() {
        if (model == null) {
            model = new Model();
        }
        return model;
    }

    public Viewfactory getViewfactory() {
        return viewfactory;
    }

    public DataBaseDriver getDataBaseDriver() {
        return dataBaseDriver;
    }

    public Client getClient() {
        return client;
    }

    public boolean getClientLoginSuccessFlag() {
        return clientLoginSuccessFlag;
    }

    public void setClientLoginSuccessFlag(boolean flag) {
        this.clientLoginSuccessFlag = flag;
    }

    public boolean getAdminLoginSuccessFlag() {
        return adminLoginSuccessFlag;
    }

    public void setAdminLoginSuccessFlag(boolean flag) {
        this.adminLoginSuccessFlag = flag;
    }

    public void evaluateClientCred(String payeeAddress, String password) {
        clientLoginSuccessFlag = false;
        if (!dataBaseDriver.authenticateClient(payeeAddress, password)) {
            return;
        }
        Optional<Client> loadedClient = dataBaseDriver.findClient(payeeAddress);
        loadedClient.ifPresent(value -> {
            copyClient(value);
            clientLoginSuccessFlag = true;
            refreshTransactions();
        });
    }

    public void evaluateAdminCred(String username, String password) {
        adminLoginSuccessFlag = dataBaseDriver.authenticateAdmin(username, password);
    }

    public void refreshCurrentClient() {
        String payeeAddress = client.payeeAddressProperty().get();
        if (payeeAddress == null || payeeAddress.isBlank()) {
            return;
        }
        dataBaseDriver.findClient(payeeAddress).ifPresent(this::copyClient);
    }

    private void copyClient(Client source) {
        client.firstNameProperty().set(source.firstNameProperty().get());
        client.lastNameProperty().set(source.lastNameProperty().get());
        client.payeeAddressProperty().set(source.payeeAddressProperty().get());
        client.dateCreatedProperty().set(source.dateCreatedProperty().get());

        Account currentChecking = client.checkingAccountProperty().get();
        Account sourceChecking = source.checkingAccountProperty().get();
        if (currentChecking instanceof CheckingAccount checking && sourceChecking instanceof CheckingAccount sourceAccount) {
            checking.ownerProperty().set(sourceAccount.ownerProperty().get());
            checking.accountNumberProperty().set(sourceAccount.accountNumberProperty().get());
            checking.balanceProperty().set(sourceAccount.balanceProperty().get());
            checking.transactionLimitProperty().set(sourceAccount.transactionLimitProperty().get());
        } else {
            client.checkingAccountProperty().set(sourceChecking);
        }

        Account currentSavings = client.savingsAccountProperty().get();
        Account sourceSavings = source.savingsAccountProperty().get();
        if (currentSavings instanceof SavingsAccount savings && sourceSavings instanceof SavingsAccount sourceAccount) {
            savings.ownerProperty().set(sourceAccount.ownerProperty().get());
            savings.accountNumberProperty().set(sourceAccount.accountNumberProperty().get());
            savings.balanceProperty().set(sourceAccount.balanceProperty().get());
            savings.withdrawalLimitProperty().set(sourceAccount.withdrawalLimitProperty().get());
        } else {
            client.savingsAccountProperty().set(sourceSavings);
        }
    }

    public void setLatestTransactions() {
        latestTransactions.setAll(dataBaseDriver.getTransactions(client.payeeAddressProperty().get(), 4));
    }

    public ObservableList<Transaction> getLatestTransactions() {
        return latestTransactions;
    }

    public void setAllTransactions() {
        allTransactions.setAll(dataBaseDriver.getTransactions(client.payeeAddressProperty().get(), -1));
    }

    public ObservableList<Transaction> getAllTransactions() {
        return allTransactions;
    }

    public void refreshTransactions() {
        setLatestTransactions();
        setAllTransactions();
    }

    public ObservableList<Client> getClients() {
        return clients;
    }

    public void setClients() {
        clients.setAll(dataBaseDriver.getAllClients());
    }

    public ObservableList<Client> searchClient(String payeeAddress) {
        ObservableList<Client> searchResults = FXCollections.observableArrayList();
        dataBaseDriver.findClient(payeeAddress).ifPresent(searchResults::add);
        return searchResults;
    }

    public CheckingAccount getCheckingAccount(String payeeAddress) {
        return dataBaseDriver.getCheckingAccount(payeeAddress);
    }

    public SavingsAccount getSavingsAccount(String payeeAddress) {
        return dataBaseDriver.getSavingsAccount(payeeAddress);
    }

    public void clearClientSession() {
        clientLoginSuccessFlag = false;
        client.firstNameProperty().set("");
        client.lastNameProperty().set("");
        client.payeeAddressProperty().set("");
        client.dateCreatedProperty().set(null);
        latestTransactions.clear();
        allTransactions.clear();
    }
}
__MAZE_22_0__
mkdir -p "$ROOT/src/main/java/com/example/bankmangament/Models"
cat > "$ROOT/src/main/java/com/example/bankmangament/Models/OperationResult.java" <<'__MAZE_23_0__'
package com.example.bankmangament.Models;

public record OperationResult(boolean success, String message) {
    public static OperationResult ok(String message) {
        return new OperationResult(true, message);
    }

    public static OperationResult fail(String message) {
        return new OperationResult(false, message);
    }
}
__MAZE_23_0__
mkdir -p "$ROOT/src/main/java/com/example/bankmangament/Models"
cat > "$ROOT/src/main/java/com/example/bankmangament/Models/PasswordUtils.java" <<'__MAZE_24_0__'
package com.example.bankmangament.Models;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public final class PasswordUtils {
    private static final String PREFIX = "pbkdf2";
    private static final int ITERATIONS = 65_536;
    private static final int KEY_LENGTH = 256;
    private static final int SALT_BYTES = 16;

    private PasswordUtils() {
    }

    public static String hashPassword(String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }
        byte[] salt = new byte[SALT_BYTES];
        new SecureRandom().nextBytes(salt);
        byte[] hash = derive(password.toCharArray(), salt, ITERATIONS);
        return PREFIX + "$" + ITERATIONS + "$"
                + Base64.getEncoder().encodeToString(salt) + "$"
                + Base64.getEncoder().encodeToString(hash);
    }

    public static boolean matches(String rawPassword, String storedPassword) {
        if (rawPassword == null || storedPassword == null) {
            return false;
        }
        if (!isHashed(storedPassword)) {
            return MessageDigest.isEqual(
                    rawPassword.getBytes(java.nio.charset.StandardCharsets.UTF_8),
                    storedPassword.getBytes(java.nio.charset.StandardCharsets.UTF_8)
            );
        }

        try {
            String[] parts = storedPassword.split("\\$");
            int iterations = Integer.parseInt(parts[1]);
            byte[] salt = Base64.getDecoder().decode(parts[2]);
            byte[] expected = Base64.getDecoder().decode(parts[3]);
            byte[] actual = derive(rawPassword.toCharArray(), salt, iterations);
            return MessageDigest.isEqual(expected, actual);
        } catch (RuntimeException exception) {
            return false;
        }
    }

    public static boolean isHashed(String storedPassword) {
        return storedPassword != null && storedPassword.startsWith(PREFIX + "$");
    }

    private static byte[] derive(char[] password, byte[] salt, int iterations) {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, KEY_LENGTH);
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            return factory.generateSecret(spec).getEncoded();
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to protect password.", exception);
        } finally {
            spec.clearPassword();
        }
    }
}
__MAZE_24_0__
mkdir -p "$ROOT/src/main/java/com/example/bankmangament/Models"
cat > "$ROOT/src/main/java/com/example/bankmangament/Models/SavingsAccount.java" <<'__MAZE_25_0__'
package com.example.bankmangament.Models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class SavingsAccount extends Account {
    private final DoubleProperty withdrawalLimit;

    public SavingsAccount(String owner, String accountNumber, double balance, double withdrawalLimit) {
        super(owner, accountNumber, balance);
        this.withdrawalLimit = new SimpleDoubleProperty(this, "Withdrawal Limit", withdrawalLimit);
    }

    public DoubleProperty withdrawalLimitProperty() {
        return withdrawalLimit;
    }

    @Override
    public String toString() {
        return accountNumberProperty().get();
    }
}
__MAZE_25_0__
mkdir -p "$ROOT/src/main/java/com/example/bankmangament/Models"
cat > "$ROOT/src/main/java/com/example/bankmangament/Models/Transaction.java" <<'__MAZE_26_0__'
package com.example.bankmangament.Models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;

public class Transaction {
    private final StringProperty sender;
    private final StringProperty receiver;
    private final DoubleProperty amount;
    private final ObjectProperty<LocalDate> date;
    private final StringProperty message;

    public Transaction(String sender, String receiver, Double amount, LocalDate date, String message) {
        this.sender = new SimpleStringProperty(this, "Sender", sender);
        this.receiver = new SimpleStringProperty(this, "Receiver", receiver);
        this.amount = new SimpleDoubleProperty(this, "Amount", amount);
        this.date = new SimpleObjectProperty<>(this, "Date", date);
        this.message = new SimpleStringProperty(this, "Message", message == null ? "" : message);
    }

    public StringProperty senderProperty() {
        return sender;
    }

    public StringProperty receiverProperty() {
        return receiver;
    }

    // Kept for compatibility with older controller code.
    public StringProperty reciverProperty() {
        return receiverProperty();
    }

    public DoubleProperty amountProperty() {
        return amount;
    }

    public ObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    public StringProperty messageProperty() {
        return message;
    }
}
__MAZE_26_0__
mkdir -p "$ROOT/src/main/java/com/example/bankmangament/Views"
cat > "$ROOT/src/main/java/com/example/bankmangament/Views/AccountType.java" <<'__MAZE_27_0__'
package com.example.bankmangament.Views;

public enum AccountType {
    ADMIN, CLIENT
}
__MAZE_27_0__
mkdir -p "$ROOT/src/main/java/com/example/bankmangament/Views"
cat > "$ROOT/src/main/java/com/example/bankmangament/Views/AdminMenuOptions.java" <<'__MAZE_28_0__'
package com.example.bankmangament.Views;

public enum AdminMenuOptions {
    CREATECLIENT,
    CLIENTS,
    DEPOSITS
}
__MAZE_28_0__
mkdir -p "$ROOT/src/main/java/com/example/bankmangament/Views"
