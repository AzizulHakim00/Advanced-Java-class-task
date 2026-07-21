package com.example.bankmangament.Controllers.Admin;

import com.example.bankmangament.Models.Model;
import com.example.bankmangament.Views.AdminMenuOptions;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminController implements Initializable {
    public BorderPane admin_parent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().getViewfactory().getAdminSelectMenuItem().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }
            switch (newValue) {
                case CLIENTS -> admin_parent.setCenter(Model.getInstance().getViewfactory().getClients_view());
                case DEPOSITS -> admin_parent.setCenter(Model.getInstance().getViewfactory().getDeposit_view());
                default -> admin_parent.setCenter(Model.getInstance().getViewfactory().getCreate_client_view());
            }
        });
        Model.getInstance().getViewfactory().getAdminSelectMenuItem().set(AdminMenuOptions.CREATECLIENT);
    }
}
