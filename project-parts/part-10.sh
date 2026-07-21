#!/usr/bin/env bash
set -euo pipefail
ROOT=${ROOT:-Maze-Bank-Management-System}
cat > "$ROOT/src/main/java/com/example/bankmangament/Views/AlertManager.java" <<'__MAZE_29_0__'
package com.example.bankmangament.Views;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

import java.util.Optional;

public final class AlertManager {
    private AlertManager() {
    }

    public static void showInfo(String title, String message) {
        show(Alert.AlertType.INFORMATION, title, message);
    }

    public static void showWarning(String title, String message) {
        show(Alert.AlertType.WARNING, title, message);
    }

    public static void showError(String title, String message) {
        show(Alert.AlertType.ERROR, title, message);
    }

    public static boolean confirm(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.OK, ButtonType.CANCEL);
        alert.setTitle(title);
        alert.setHeaderText(null);
        return alert.showAndWait().filter(ButtonType.OK::equals).isPresent();
    }

    public static Optional<String> prompt(String title, String header, String placeholder) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(placeholder);
        return dialog.showAndWait().map(String::trim).filter(value -> !value.isBlank());
    }

    private static void show(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Backward-compatible helpers used by older code.
    public static void showInfoAlert() {
        showInfo("Information", "Operation completed.");
    }

    public static void showWarningAlert() {
        showWarning("Warning", "Please check the entered information.");
    }

    public static void showErrorAlert() {
        showError("Error", "The operation could not be completed.");
    }

    public static void showConfirmationAlert() {
        confirm("Confirmation", "Do you want to proceed?");
    }
}
__MAZE_29_0__
mkdir -p "$ROOT/src/main/java/com/example/bankmangament/Views"
cat > "$ROOT/src/main/java/com/example/bankmangament/Views/ClientCellFactory.java" <<'__MAZE_30_0__'
package com.example.bankmangament.Views;

import com.example.bankmangament.Controllers.Admin.ClientCellContainer;

import com.example.bankmangament.Models.Client;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;



public class ClientCellFactory extends ListCell<Client> {
    @Override
    protected void updateItem(Client client, boolean empty) {

        super.updateItem(client, empty);
        if(empty){
            setText(null);
            setGraphic(null);
            
        }
        else {
           FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/bankmangament/Admin/ClientCell.fxml"));
            ClientCellContainer controller = new ClientCellContainer(client);
            fxmlLoader.setController(controller);
            setText(null);
            try{
                setGraphic(fxmlLoader.load());
            }
            catch(Exception e){
                e.printStackTrace();
            }

        }
    }
}
__MAZE_30_0__
mkdir -p "$ROOT/src/main/java/com/example/bankmangament/Views"
cat > "$ROOT/src/main/java/com/example/bankmangament/Views/ClientMenuOptions.java" <<'__MAZE_31_0__'
package com.example.bankmangament.Views;

public enum ClientMenuOptions {
    DASHBOARD,
    TRANSACTION,
    ACCOUNTS
}
__MAZE_31_0__
mkdir -p "$ROOT/src/main/java/com/example/bankmangament/Views"
cat > "$ROOT/src/main/java/com/example/bankmangament/Views/TransactionCellFactory.java" <<'__MAZE_32_0__'
package com.example.bankmangament.Views;

import com.example.bankmangament.Controllers.Client.TransactionCellController;
import com.example.bankmangament.Models.Transaction;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;

import java.io.IOException;

public class  TransactionCellFactory extends ListCell<Transaction> {
    @Override
    protected void updateItem(Transaction transaction, boolean empty) {
        super.updateItem(transaction, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        }
        else {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/bankmangament/Client/TransactionCell.fxml"));
            TransactionCellController controller = new TransactionCellController(transaction);
            fxmlLoader.setController(controller);
            setText(null);
            try{
                setGraphic(fxmlLoader.load());

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
__MAZE_32_0__
mkdir -p "$ROOT/src/main/java/com/example/bankmangament/Views"
cat > "$ROOT/src/main/java/com/example/bankmangament/Views/Viewfactory.java" <<'__MAZE_33_0__'
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
__MAZE_33_0__
mkdir -p "$ROOT/src/main/java"
cat > "$ROOT/src/main/java/module-info.java" <<'__MAZE_34_0__'
module com.example.bankmangament {
    requires javafx.controls;
    requires javafx.fxml;
    requires de.jensd.fx.glyphs.fontawesome;
    requires java.sql;
    requires org.xerial.sqlitejdbc;

    opens com.example.bankmangament to javafx.fxml;
    opens com.example.bankmangament.Controllers to javafx.fxml;
    opens com.example.bankmangament.Controllers.Client to javafx.fxml;
    opens com.example.bankmangament.Controllers.Admin to javafx.fxml;

    exports com.example.bankmangament;
    exports com.example.bankmangament.Controllers;
    exports com.example.bankmangament.Controllers.Client;
    exports com.example.bankmangament.Controllers.Admin;
    exports com.example.bankmangament.Models;
    exports com.example.bankmangament.Views;
}
__MAZE_34_0__
mkdir -p "$ROOT/src/main/resources/Style"
cat > "$ROOT/src/main/resources/Style/Account.css" <<'__MAZE_35_0__'
.accounts_view_container{
    -fx-background-color: #EEEEEE;

}
.accounts_view_container Text{
    -fx-fill: #888888;
    -fx-font-size: 1.4em;
    -fx-font-family: "Calibri Light";
}
.accounts_view_container VBox{
    -fx-border-width: 0  0 0 1;
    -fx-border-color: #888888;
    -fx-padding: 10;
    -fx-spacing: 15;
}
.accounts_view_container VBox Text{
    -fx-font-size: 1.2em;
}
.accounts_view_container VBox label{
    -fx-font-size: 1.2em;
    -fx-font-weight: bold;
}
.accounts_view_container TextField{
    -fx-pref-width: 230;
    -fx-pref-height: 35;
    -fx-background-color: #FFFFFF;
-fx-effect: dropshadow(three-pass-box , #AAAAAA , 3, 0 ,0 , 3);
}
.accounts_view_container Button{
    -fx-background-color: linear-gradient(to right , #132A13 , #253F25 );
    -fx-effect: dropshadow(three-pass-box , #AAAAAA , 3, 0 ,0 , 3);
    -fx-pref-width: 230;
    -fx-font-size: 1.5em;
    -fx-font-family: "Calibri Light";
    -fx-text-fill: #FFFFFF;
}
.accounts_view_container Button:hover{
    -fx-cursor: hand;
}
__MAZE_35_0__
mkdir -p "$ROOT/src/main/resources/Style"
