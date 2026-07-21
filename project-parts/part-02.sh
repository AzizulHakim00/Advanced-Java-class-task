#!/usr/bin/env bash
set -euo pipefail
ROOT=${ROOT:-Maze-Bank-Management-System}
cat > "$ROOT/src/main/java/com/example/bankmangament/Controllers/Admin/ClientsController.java" <<'__MAZE_8_0__'
package com.example.bankmangament.Controllers.Admin;

import com.example.bankmangament.Models.Client;
import com.example.bankmangament.Models.Model;
import com.example.bankmangament.Views.ClientCellFactory;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientsController implements Initializable {
    public ListView<Client> clients_listview;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().setClients();
        clients_listview.setItems(Model.getInstance().getClients());
        clients_listview.setCellFactory(cell -> new ClientCellFactory());
    }
}
__MAZE_8_0__
mkdir -p "$ROOT/src/main/java/com/example/bankmangament/Controllers/Admin"
cat > "$ROOT/src/main/java/com/example/bankmangament/Controllers/Admin/CreateClientController.java" <<'__MAZE_9_0__'
package com.example.bankmangament.Controllers.Admin;

import com.example.bankmangament.Models.Model;
import com.example.bankmangament.Models.OperationResult;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.security.SecureRandom;
import java.util.Locale;
import java.util.ResourceBundle;

public class CreateClientController implements Initializable {
    public TextField fName_fid;
    public TextField LName_fid;
    public TextField passsword_fid;
    public CheckBox pAddress_box;
    public Label pAddress_lbl;
    public CheckBox ch_acc_box;
    public TextField ch_amount_fid;
    public CheckBox sv_acc_box;
    public Button create_client_btn;
    public TextField sv_amount_fid;
    public Label error_lbl;

    private final SecureRandom random = new SecureRandom();
    private String payeeAddress = "";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        create_client_btn.setOnAction(event -> createClient());
        pAddress_box.selectedProperty().addListener((observable, oldValue, selected) -> {
            if (selected) {
                generatePayeeAddress();
            } else {
                payeeAddress = "";
                pAddress_lbl.setText("");
            }
        });
        fName_fid.textProperty().addListener((observable, oldValue, newValue) -> regenerateIfSelected());
        LName_fid.textProperty().addListener((observable, oldValue, newValue) -> regenerateIfSelected());
        ch_acc_box.setSelected(true);
        sv_acc_box.setSelected(true);
    }

    private void regenerateIfSelected() {
        if (pAddress_box.isSelected()) {
            generatePayeeAddress();
        }
    }

    private void generatePayeeAddress() {
        String firstName = clean(fName_fid.getText());
        String lastName = clean(LName_fid.getText());
        if (firstName.isBlank() || lastName.isBlank()) {
            payeeAddress = "";
            pAddress_lbl.setText("Enter first and last name first");
            return;
        }
        String safeLastName = lastName.replaceAll("[^A-Za-z0-9]", "");
        int id = Model.getInstance().getDataBaseDriver().getLastClientID() + 1;
        payeeAddress = ("@" + Character.toLowerCase(firstName.charAt(0)) + safeLastName + id).toLowerCase(Locale.ROOT);
        pAddress_lbl.setText(payeeAddress);
    }

    private void createClient() {
        error_lbl.setStyle("-fx-text-fill: red; -fx-font-size: 1.1em; -fx-font-weight: bold;");
        if (!ch_acc_box.isSelected() || !sv_acc_box.isSelected()) {
            error_lbl.setText("Both checking and savings accounts are required.");
            return;
        }
        if (payeeAddress.isBlank()) {
            generatePayeeAddress();
        }
        if (payeeAddress.isBlank()) {
            error_lbl.setText("Enter a valid name and generate a payee address.");
            return;
        }

        double checkingBalance;
        double savingsBalance;
        try {
            checkingBalance = parseOpeningBalance(ch_amount_fid.getText());
            savingsBalance = parseOpeningBalance(sv_amount_fid.getText());
        } catch (IllegalArgumentException exception) {
            error_lbl.setText(exception.getMessage());
            return;
        }

        String checkingNumber = generateUniqueAccountNumber();
        String savingsNumber;
        do {
            savingsNumber = generateUniqueAccountNumber();
        } while (savingsNumber.equals(checkingNumber));
        OperationResult result = Model.getInstance().getDataBaseDriver().createClientWithAccounts(
                fName_fid.getText(),
                LName_fid.getText(),
                payeeAddress,
                passsword_fid.getText(),
                checkingBalance,
                savingsBalance,
                checkingNumber,
                savingsNumber
        );

        if (!result.success()) {
            error_lbl.setText(result.message());
            return;
        }
        Model.getInstance().setClients();
        error_lbl.setStyle("-fx-text-fill: #167d35; -fx-font-size: 1.1em; -fx-font-weight: bold;");
        error_lbl.setText(result.message() + " Payee: " + payeeAddress);
        clearForm();
    }

    private double parseOpeningBalance(String value) {
        if (value == null || value.isBlank()) {
            return 0;
        }
        try {
            double amount = Double.parseDouble(value.trim());
            if (!Double.isFinite(amount) || amount < 0) {
                throw new NumberFormatException();
            }
            return amount;
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Opening balances must be valid non-negative numbers.");
        }
    }

    private String generateUniqueAccountNumber() {
        String number;
        do {
            number = String.format("3201 %06d", random.nextInt(1_000_000));
        } while (Model.getInstance().getDataBaseDriver().accountNumberExists(number));
        return number;
    }

    private void clearForm() {
        fName_fid.clear();
        LName_fid.clear();
        passsword_fid.clear();
        pAddress_box.setSelected(false);
        pAddress_lbl.setText("");
        ch_amount_fid.clear();
        sv_amount_fid.clear();
        ch_acc_box.setSelected(true);
        sv_acc_box.setSelected(true);
        payeeAddress = "";
    }

    private String clean(String value) {
        return value == null ? "" : value.trim();
    }
}
__MAZE_9_0__
mkdir -p "$ROOT/src/main/java/com/example/bankmangament/Controllers/Admin"
cat > "$ROOT/src/main/java/com/example/bankmangament/Controllers/Admin/DepositController.java" <<'__MAZE_10_0__'
package com.example.bankmangament.Controllers.Admin;

import com.example.bankmangament.Models.Client;
import com.example.bankmangament.Models.Model;
import com.example.bankmangament.Models.OperationResult;
import com.example.bankmangament.Views.AlertManager;
import com.example.bankmangament.Views.ClientCellFactory;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class DepositController implements Initializable {
    public TextField payeeAdress_fid;
    public ListView<Client> result_listview;
    public Button search_btn;
    public TextField amount_fid;
    public Button deposit_btn;

    private Client client;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        search_btn.setOnAction(event -> onClientSearch());
        deposit_btn.setOnAction(event -> onDeposit());
        result_listview.setCellFactory(cell -> new ClientCellFactory());
        result_listview.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> client = newValue
        );
    }

    private void onClientSearch() {
        String payeeAddress = payeeAdress_fid.getText() == null ? "" : payeeAdress_fid.getText().trim();
        if (payeeAddress.isBlank()) {
            AlertManager.showWarning("Search client", "Enter a payee address.");
            return;
        }
        ObservableList<Client> searchResults = Model.getInstance().searchClient(payeeAddress);
        result_listview.setItems(searchResults);
        if (searchResults.isEmpty()) {
            client = null;
            AlertManager.showWarning("Client not found", "No client matches that payee address.");
        } else {
            client = searchResults.getFirst();
            result_listview.getSelectionModel().selectFirst();
        }
    }

    private void onDeposit() {
        if (client == null) {
            AlertManager.showWarning("Select client", "Search for and select a client first.");
            return;
        }
        double amount;
        try {
            amount = Double.parseDouble(amount_fid.getText().trim());
        } catch (Exception exception) {
            AlertManager.showError("Invalid amount", "Enter a numeric amount greater than zero.");
            return;
        }

        OperationResult result = Model.getInstance().getDataBaseDriver().depositSavings(
                client.payeeAddressProperty().get(),
                amount
        );
        if (!result.success()) {
            AlertManager.showError("Deposit failed", result.message());
            return;
        }
        AlertManager.showInfo("Deposit complete", result.message());
        String address = client.payeeAddressProperty().get();
        result_listview.setItems(Model.getInstance().searchClient(address));
        Model.getInstance().setClients();
        amount_fid.clear();
    }
}
__MAZE_10_0__
mkdir -p "$ROOT/src/main/java/com/example/bankmangament/Controllers/Client"
cat > "$ROOT/src/main/java/com/example/bankmangament/Controllers/Client/AccountsController.java" <<'__MAZE_11_0__'
package com.example.bankmangament.Controllers.Client;

import com.example.bankmangament.Models.CheckingAccount;
import com.example.bankmangament.Models.Model;
import com.example.bankmangament.Models.OperationResult;
import com.example.bankmangament.Models.SavingsAccount;
import com.example.bankmangament.Views.AlertManager;
import javafx.beans.binding.Bindings;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class AccountsController implements Initializable {
    public Label checkingt_acc_num;
    public Label transaction_limit;
    public Label ch_acc_date;
    public Label ch_acc_bal;
    public Label savings_acc_num;
    public Label withdrawal_limit;
    public Label sv_acc_date;
    public Label sv_acc_bal;
    public TextField amount_to_sv;
    public Button trans_sv_btn;
    public TextField amount_to_ch;
    public Button trans_to_ch_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bindData();
        trans_sv_btn.setOnAction(event -> transfer(true));
        trans_to_ch_btn.setOnAction(event -> transfer(false));
    }

    private void bindData() {
        Model model = Model.getInstance();
        CheckingAccount checking = (CheckingAccount) model.getClient().checkingAccountProperty().get();
        SavingsAccount savings = (SavingsAccount) model.getClient().savingsAccountProperty().get();

        checkingt_acc_num.textProperty().bind(checking.accountNumberProperty());
        transaction_limit.textProperty().bind(Bindings.format("%d transactions/day", checking.transactionLimitProperty()));
        ch_acc_date.textProperty().bind(model.getClient().dateCreatedProperty().asString());
        ch_acc_bal.textProperty().bind(Bindings.format("$%,.2f", checking.balanceProperty()));

        savings_acc_num.textProperty().bind(savings.accountNumberProperty());
        withdrawal_limit.textProperty().bind(Bindings.format("$%,.2f", savings.withdrawalLimitProperty()));
        sv_acc_date.textProperty().bind(model.getClient().dateCreatedProperty().asString());
        sv_acc_bal.textProperty().bind(Bindings.format("$%,.2f", savings.balanceProperty()));
    }

    private void transfer(boolean checkingToSavings) {
        TextField field = checkingToSavings ? amount_to_sv : amount_to_ch;
        double amount;
        try {
            amount = Double.parseDouble(field.getText().trim());
        } catch (Exception exception) {
            AlertManager.showError("Invalid amount", "Enter a numeric amount greater than zero.");
            return;
        }

        Model model = Model.getInstance();
        OperationResult result = model.getDataBaseDriver().transferBetweenAccounts(
                model.getClient().payeeAddressProperty().get(),
                checkingToSavings,
                amount
        );
        if (!result.success()) {
            AlertManager.showError("Transfer failed", result.message());
            return;
        }
        model.refreshCurrentClient();
        field.clear();
        AlertManager.showInfo("Transfer complete", result.message());
    }
}
__MAZE_11_0__
mkdir -p "$ROOT/src/main/java/com/example/bankmangament/Controllers/Client"
