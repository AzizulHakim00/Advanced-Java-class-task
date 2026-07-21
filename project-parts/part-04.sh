#!/usr/bin/env bash
set -euo pipefail
ROOT=${ROOT:-Maze-Bank-Management-System}
cat > "$ROOT/src/main/java/com/example/bankmangament/Controllers/LoginController.java" <<'__MAZE_17_0__'
package com.example.bankmangament.Controllers;

import com.example.bankmangament.Models.Model;
import com.example.bankmangament.Views.AccountType;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    public ChoiceBox<AccountType> account_selector;
    public Label payee_adress;
    public TextField payee_address_field;
    public TextField password_field;
    public Button login_btn;
    public Label error_lbl;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        account_selector.setItems(FXCollections.observableArrayList(AccountType.CLIENT, AccountType.ADMIN));
        account_selector.setValue(Model.getInstance().getViewfactory().getLoginAccountType());
        account_selector.valueProperty().addListener((observable, oldValue, newValue) -> setAccountSelector());
        login_btn.setOnAction(event -> onLogin());
        password_field.setOnAction(event -> onLogin());
        setAccountSelector();
    }

    private void onLogin() {
        String identity = payee_address_field.getText() == null ? "" : payee_address_field.getText().trim();
        String password = password_field.getText() == null ? "" : password_field.getText();
        if (identity.isBlank() || password.isBlank()) {
            error_lbl.setText("Username/payee address and password are required.");
            return;
        }

        Stage stage = (Stage) error_lbl.getScene().getWindow();
        if (Model.getInstance().getViewfactory().getLoginAccountType() == AccountType.CLIENT) {
            Model.getInstance().evaluateClientCred(identity, password);
            if (Model.getInstance().getClientLoginSuccessFlag()) {
                Model.getInstance().getViewfactory().showClientWindow();
                Model.getInstance().getViewfactory().closeStage(stage);
                return;
            }
        } else {
            Model.getInstance().evaluateAdminCred(identity, password);
            if (Model.getInstance().getAdminLoginSuccessFlag()) {
                Model.getInstance().getViewfactory().showAdminwindow();
                Model.getInstance().getViewfactory().closeStage(stage);
                return;
            }
        }

        password_field.clear();
        error_lbl.setText("Invalid login credentials.");
    }

    private void setAccountSelector() {
        AccountType selected = account_selector.getValue() == null ? AccountType.CLIENT : account_selector.getValue();
        Model.getInstance().getViewfactory().setLoginAccountType(selected);
        payee_adress.setText(selected == AccountType.ADMIN ? "Username" : "Payee Address");
        error_lbl.setText("");
    }
}
__MAZE_17_0__
mkdir -p "$ROOT/src/main/java/com/example/bankmangament/Models"
cat > "$ROOT/src/main/java/com/example/bankmangament/Models/Account.java" <<'__MAZE_18_0__'
package com.example.bankmangament.Models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public abstract class Account {
    private final StringProperty owner ;
    private final StringProperty accountNumber ;
    private final DoubleProperty balance ;
    public Account(String owner, String accountNumber, Double balance) {

        this.owner = new SimpleStringProperty(this,"Owner" , owner);
        this.accountNumber = new SimpleStringProperty(this,"Account Number" , accountNumber);
        this.balance = new SimpleDoubleProperty(this,"Balance" , balance);
    }

    public StringProperty ownerProperty() {
        return this.owner;
    }
    public StringProperty accountNumberProperty() {
        return this.accountNumber;

    }
    public DoubleProperty balanceProperty() {
        return this.balance;
    }

    public void setBalance(Double balance) {
        this.balance.set(balance);
    }

}
__MAZE_18_0__
mkdir -p "$ROOT/src/main/java/com/example/bankmangament/Models"
cat > "$ROOT/src/main/java/com/example/bankmangament/Models/CheckingAccount.java" <<'__MAZE_19_0__'
package com.example.bankmangament.Models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class CheckingAccount extends Account {
    private final IntegerProperty transactionLimit;

    public CheckingAccount(String owner, String accountNumber, double balance, int transactionLimit) {
        super(owner, accountNumber, balance);
        this.transactionLimit = new SimpleIntegerProperty(this, "Transaction Limit", transactionLimit);
    }

    public IntegerProperty transactionLimitProperty() {
        return transactionLimit;
    }

    @Override
    public String toString() {
        return accountNumberProperty().get();
    }
}
__MAZE_19_0__
mkdir -p "$ROOT/src/main/java/com/example/bankmangament/Models"
cat > "$ROOT/src/main/java/com/example/bankmangament/Models/Client.java" <<'__MAZE_20_0__'
package com.example.bankmangament.Models;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;

public class Client {
    private final StringProperty firstName ;
    private final StringProperty lastName ;
    private final StringProperty payeeAddress ;
    private final ObjectProperty<Account> checkingAccount ;
    private final ObjectProperty<Account> savingsAccount ;
    private final ObjectProperty<LocalDate> dateCreated ;

    public Client(String fName , String lName , String pAddress , Account cAccount , Account sAccount , LocalDate date) {
        this.firstName = new SimpleStringProperty(this , "FirstName", fName ) ;
        this.lastName = new SimpleStringProperty(this , "LastName", lName ) ;
        this.payeeAddress = new SimpleStringProperty(this , "Payee Address", pAddress ) ;
        this.checkingAccount = new SimpleObjectProperty(this , "Checking Account", cAccount ) ;
        this.savingsAccount = new SimpleObjectProperty(this , "Saving Account", sAccount ) ;
        this.dateCreated = new SimpleObjectProperty(this , "Date", date ) ;
    }
    public StringProperty firstNameProperty() {
        return this.firstName;

    }
    public StringProperty lastNameProperty() {
        return this.lastName;

    }
    public StringProperty payeeAddressProperty() {
        return this.payeeAddress;
    }
    public ObjectProperty<Account> checkingAccountProperty() {
        return this.checkingAccount;
    }
    public ObjectProperty<Account> savingsAccountProperty() {
        return this.savingsAccount;
    }
    public ObjectProperty<LocalDate> dateCreatedProperty() {
        return this.dateCreated;
    }

}
__MAZE_20_0__
mkdir -p "$ROOT/src/main/java/com/example/bankmangament/Models"
