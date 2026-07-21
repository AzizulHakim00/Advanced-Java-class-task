package com.example.bankmangament.Controllers.Client;

import com.example.bankmangament.Models.Client;
import com.example.bankmangament.Models.Model;
import com.example.bankmangament.Models.OperationResult;
import com.example.bankmangament.Views.AlertManager;
import com.example.bankmangament.Views.ClientMenuOptions;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientMenuController implements Initializable {
    public Button dassboard_btn;
    public Button transaction_btn;
    public Button account_btn;
    public Button profile_btn;
    public Button logout_btn;
    public Button report_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dassboard_btn.setOnAction(event -> select(ClientMenuOptions.DASHBOARD));
        transaction_btn.setOnAction(event -> select(ClientMenuOptions.TRANSACTION));
        account_btn.setOnAction(event -> select(ClientMenuOptions.ACCOUNTS));
        profile_btn.setOnAction(event -> showProfile());
        report_btn.setOnAction(event -> reportBug());
        logout_btn.setOnAction(event -> onLogout());
    }

    private void select(ClientMenuOptions option) {
        Model.getInstance().getViewfactory().getClientSelectMenuItem().set(option);
    }

    private void showProfile() {
        Client client = Model.getInstance().getClient();
        String message = "Name: " + client.firstNameProperty().get() + " " + client.lastNameProperty().get()
                + "\nPayee address: " + client.payeeAddressProperty().get()
                + "\nMember since: " + client.dateCreatedProperty().get()
                + "\nChecking account: " + client.checkingAccountProperty().get().accountNumberProperty().get()
                + "\nSavings account: " + client.savingsAccountProperty().get().accountNumberProperty().get();
        AlertManager.showInfo("Client profile", message);
    }

    private void reportBug() {
        AlertManager.prompt("Report a problem", "Describe the error you experienced", "Problem details:")
                .ifPresent(description -> {
                    Model model = Model.getInstance();
                    OperationResult result = model.getDataBaseDriver().submitBugReport(
                            model.getClient().payeeAddressProperty().get(),
                            description
                    );
                    if (result.success()) {
                        AlertManager.showInfo("Report submitted", result.message());
                    } else {
                        AlertManager.showError("Report failed", result.message());
                    }
                });
    }

    private void onLogout() {
        Stage stage = (Stage) dassboard_btn.getScene().getWindow();
        Model.getInstance().clearClientSession();
        Model.getInstance().getViewfactory().closeStage(stage);
        Model.getInstance().getViewfactory().showLoginWindow();
    }
}
