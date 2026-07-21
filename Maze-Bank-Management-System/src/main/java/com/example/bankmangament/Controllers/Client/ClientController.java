package com.example.bankmangament.Controllers.Client;

import com.example.bankmangament.Models.Model;
import com.example.bankmangament.Views.ClientMenuOptions;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientController implements Initializable {
    @FXML
    public BorderPane client_parent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().getViewfactory().getClientSelectMenuItem().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }
            switch (newValue) {
                case TRANSACTION -> client_parent.setCenter(Model.getInstance().getViewfactory().getTransaction_view());
                case ACCOUNTS -> client_parent.setCenter(Model.getInstance().getViewfactory().getAccountView());
                default -> client_parent.setCenter(Model.getInstance().getViewfactory().getDashboardView());
            }
        });
        Model.getInstance().getViewfactory().getClientSelectMenuItem().set(ClientMenuOptions.DASHBOARD);
    }
}
