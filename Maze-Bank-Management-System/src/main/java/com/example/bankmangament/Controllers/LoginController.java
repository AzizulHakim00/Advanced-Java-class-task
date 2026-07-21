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
