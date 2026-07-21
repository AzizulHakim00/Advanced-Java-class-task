package com.example.bankmangament.Views;

import com.example.bankmangament.Controllers.Admin.AdminController;
import com.example.bankmangament.Controllers.Client.ClientController;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Viewfactory {
    private AccountType loginAccountType;
    private final ObjectProperty<ClientMenuOptions> clientSelectMenuItem;
    private final ObjectProperty<AdminMenuOptions> adminSelectMenuItem;

    private AnchorPane dashboardView;
    private AnchorPane transactionView;
    private AnchorPane accountView;
    private AnchorPane clientsView;
    private AnchorPane createClientView;
    private AnchorPane depositView;

    public Viewfactory() {
        loginAccountType = AccountType.CLIENT;
        clientSelectMenuItem = new SimpleObjectProperty<>();
        adminSelectMenuItem = new SimpleObjectProperty<>();
    }

    public AccountType getLoginAccountType() {
        return loginAccountType;
    }

    public void setLoginAccountType(AccountType loginAccountType) {
        this.loginAccountType = loginAccountType;
    }

    public ObjectProperty<ClientMenuOptions> getClientSelectMenuItem() {
        return clientSelectMenuItem;
    }

    public ObjectProperty<AdminMenuOptions> getAdminSelectMenuItem() {
        return adminSelectMenuItem;
    }

    public AnchorPane getDashboardView() {
        if (dashboardView == null) {
            dashboardView = loadAnchorPane("/com/example/bankmangament/Client/Dashboard.fxml");
        }
        return dashboardView;
    }

    public AnchorPane getTransaction_view() {
        if (transactionView == null) {
            transactionView = loadAnchorPane("/com/example/bankmangament/Client/Transaction.fxml");
        }
        return transactionView;
    }

    public AnchorPane getAccountView() {
        if (accountView == null) {
            accountView = loadAnchorPane("/com/example/bankmangament/Client/Accounts.fxml");
        }
        return accountView;
    }

    public AnchorPane getCreate_client_view() {
        if (createClientView == null) {
            createClientView = loadAnchorPane("/com/example/bankmangament/Admin/CreateClient.fxml");
        }
        return createClientView;
    }

    public AnchorPane getClients_view() {
        if (clientsView == null) {
            clientsView = loadAnchorPane("/com/example/bankmangament/Admin/Clients.fxml");
        }
        return clientsView;
    }

    public AnchorPane getDeposit_view() {
        if (depositView == null) {
            depositView = loadAnchorPane("/com/example/bankmangament/Admin/Deposit.fxml");
        }
        return depositView;
    }

    public void showClientWindow() {
        resetClientViews();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/bankmangament/Client/Client.fxml"));
        loader.setController(new ClientController());
        createStage(loader, "Maze Bank - Client");
    }

    public void showAdminwindow() {
        resetAdminViews();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/bankmangament/Admin/Admin.fxml"));
        loader.setController(new AdminController());
        createStage(loader, "Maze Bank - Administration");
    }

    public void showLoginWindow() {
        createStage(new FXMLLoader(getClass().getResource("/com/example/bankmangament/Login.fxml")), "Maze Bank - Login");
    }

    public void showLolginWindow() {
        showLoginWindow();
    }

    public void showMessageWindow(String payeeAddress, String messageText) {
        StackPane pane = new StackPane();
        HBox hBox = new HBox(8);
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().addAll(new Label(payeeAddress + ":"), new Label(messageText == null ? "" : messageText));
        pane.getChildren().add(hBox);

        Stage stage = new Stage();
        setIcon(stage);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Transaction message");
        stage.setScene(new Scene(pane, 420, 120));
        stage.show();
    }

    public void closeStage(Stage stage) {
        if (stage != null) {
            stage.close();
        }
    }

    private AnchorPane loadAnchorPane(String resource) {
        try {
            return new FXMLLoader(getClass().getResource(resource)).load();
        } catch (IOException exception) {
            throw new IllegalStateException("Could not load view: " + resource, exception);
        }
    }

    private void createStage(FXMLLoader loader, String title) {
        try {
            Parent root = loader.load();
            Stage stage = new Stage();
            setIcon(stage);
            stage.setResizable(false);
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException exception) {
            throw new IllegalStateException("Could not open application window.", exception);
        }
    }

    private void setIcon(Stage stage) {
        URL iconUrl = getClass().getResource("/Images/download.png");
        if (iconUrl != null) {
            stage.getIcons().add(new Image(iconUrl.toExternalForm()));
        }
    }

    private void resetClientViews() {
        dashboardView = null;
        transactionView = null;
        accountView = null;
        clientSelectMenuItem.set(null);
    }

    private void resetAdminViews() {
        clientsView = null;
        createClientView = null;
        depositView = null;
        adminSelectMenuItem.set(null);
    }
}
