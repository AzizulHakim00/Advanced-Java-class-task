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
