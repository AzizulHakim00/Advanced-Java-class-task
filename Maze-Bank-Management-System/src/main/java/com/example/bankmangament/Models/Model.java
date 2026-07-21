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
