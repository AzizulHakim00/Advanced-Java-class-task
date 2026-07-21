package com.example.bankmangament.Controllers.Admin;

import com.example.bankmangament.Models.Client;
import com.example.bankmangament.Models.Model;
import com.example.bankmangament.Models.OperationResult;
import com.example.bankmangament.Views.AlertManager;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientCellContainer implements Initializable {
    public Label fName_lbl;
    public Label lName_lbl;
    public Label pAddress_lbl;
    public Label ch_acc_lbl;
    public Label sv_acc_lbl;
    public Label date_lbl;
    public Button delete_btn;

    private final Client client;

    public ClientCellContainer(Client client) {
        this.client = client;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fName_lbl.textProperty().bind(client.firstNameProperty());
        lName_lbl.textProperty().bind(client.lastNameProperty());
        pAddress_lbl.textProperty().bind(client.payeeAddressProperty());
        ch_acc_lbl.textProperty().bind(client.checkingAccountProperty().get().accountNumberProperty());
        sv_acc_lbl.textProperty().bind(client.savingsAccountProperty().get().accountNumberProperty());
        date_lbl.textProperty().bind(client.dateCreatedProperty().asString());
        delete_btn.setOnAction(event -> deleteClient());
    }

    private void deleteClient() {
        String payeeAddress = client.payeeAddressProperty().get();
        boolean confirmed = AlertManager.confirm(
                "Delete client",
                "Delete " + client.firstNameProperty().get() + " " + client.lastNameProperty().get()
                        + " (" + payeeAddress + ") and all account data?"
        );
        if (!confirmed) {
            return;
        }
        OperationResult result = Model.getInstance().getDataBaseDriver().deleteClient(payeeAddress);
        if (!result.success()) {
            AlertManager.showError("Delete failed", result.message());
            return;
        }
        Model.getInstance().getClients().remove(client);
        AlertManager.showInfo("Client deleted", result.message());
    }
}
