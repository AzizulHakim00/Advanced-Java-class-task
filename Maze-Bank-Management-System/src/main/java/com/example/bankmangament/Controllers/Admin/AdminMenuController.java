package com.example.bankmangament.Controllers.Admin;

import com.example.bankmangament.Models.Model;
import com.example.bankmangament.Views.AdminMenuOptions;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminMenuController implements Initializable {
    public Button create_client_btn;
    public Button client_btn;
    public Button deposit_btn;
    public Button logout_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        create_client_btn.setOnAction(event -> select(AdminMenuOptions.CREATECLIENT));
        client_btn.setOnAction(event -> select(AdminMenuOptions.CLIENTS));
        deposit_btn.setOnAction(event -> select(AdminMenuOptions.DEPOSITS));
        logout_btn.setOnAction(event -> onLogout());
    }

    private void select(AdminMenuOptions option) {
        Model.getInstance().getViewfactory().getAdminSelectMenuItem().set(option);
    }

    private void onLogout() {
        Stage stage = (Stage) client_btn.getScene().getWindow();
        Model.getInstance().setAdminLoginSuccessFlag(false);
        Model.getInstance().getViewfactory().closeStage(stage);
        Model.getInstance().getViewfactory().showLoginWindow();
    }
}
