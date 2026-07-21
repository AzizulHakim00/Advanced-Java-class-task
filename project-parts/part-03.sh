#!/usr/bin/env bash
set -euo pipefail
ROOT=${ROOT:-Maze-Bank-Management-System}
cat > "$ROOT/src/main/java/com/example/bankmangament/Controllers/Client/ClientController.java" <<'__MAZE_12_0__'
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
__MAZE_12_0__
mkdir -p "$ROOT/src/main/java/com/example/bankmangament/Controllers/Client"
cat > "$ROOT/src/main/java/com/example/bankmangament/Controllers/Client/ClientMenuController.java" <<'__MAZE_13_0__'
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
__MAZE_13_0__
mkdir -p "$ROOT/src/main/java/com/example/bankmangament/Controllers/Client"
cat > "$ROOT/src/main/java/com/example/bankmangament/Controllers/Client/DashboardController.java" <<'__MAZE_14_0__'
package com.example.bankmangament.Controllers.Client;

import com.example.bankmangament.Models.Model;
import com.example.bankmangament.Models.OperationResult;
import com.example.bankmangament.Models.Transaction;
import com.example.bankmangament.Views.AlertManager;
import com.example.bankmangament.Views.TransactionCellFactory;
import javafx.beans.binding.Bindings;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    public Text user_name;
    public Label login_date;
    public Label checking_balance;
    public Label checking_acc_num;
    public Label savings_bal;
    public Label savings_acc_num;
    public Label income_lbl;
    public Label expense_lbl;
    public ListView<Transaction> transaction_list_view;
    public TextField payee_flb;
    public TextField amount_flb;
    public TextArea massage_flb;
    public Button send_money_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bindData();
        Model.getInstance().setLatestTransactions();
        transaction_list_view.setItems(Model.getInstance().getLatestTransactions());
        transaction_list_view.setCellFactory(cell -> new TransactionCellFactory());
        send_money_btn.setOnAction(event -> onSendMoney());
        accountSummary();
    }

    private void bindData() {
        Model model = Model.getInstance();
        user_name.textProperty().bind(Bindings.concat("HI, ").concat(model.getClient().firstNameProperty()));
        login_date.setText("Today, " + LocalDate.now());
        checking_balance.textProperty().bind(Bindings.format("$%,.2f", model.getClient().checkingAccountProperty().get().balanceProperty()));
        checking_acc_num.textProperty().bind(model.getClient().checkingAccountProperty().get().accountNumberProperty());
        savings_bal.textProperty().bind(Bindings.format("$%,.2f", model.getClient().savingsAccountProperty().get().balanceProperty()));
        savings_acc_num.textProperty().bind(model.getClient().savingsAccountProperty().get().accountNumberProperty());
    }

    private void onSendMoney() {
        double amount;
        try {
            amount = Double.parseDouble(amount_flb.getText().trim());
        } catch (Exception exception) {
            AlertManager.showError("Invalid amount", "Enter a numeric amount greater than zero.");
            return;
        }

        Model model = Model.getInstance();
        String sender = model.getClient().payeeAddressProperty().get();
        String receiver = payee_flb.getText() == null ? "" : payee_flb.getText().trim();
        String message = massage_flb.getText() == null ? "" : massage_flb.getText().trim();
        OperationResult result = model.getDataBaseDriver().transferSavings(sender, receiver, amount, message);
        if (!result.success()) {
            AlertManager.showError("Transfer failed", result.message());
            return;
        }

        model.refreshCurrentClient();
        model.refreshTransactions();
        accountSummary();
        payee_flb.clear();
        amount_flb.clear();
        massage_flb.clear();
        AlertManager.showInfo("Transfer complete", result.message());
    }

    private void accountSummary() {
        Model model = Model.getInstance();
        model.setAllTransactions();
        double income = 0;
        double expense = 0;
        String currentPayee = model.getClient().payeeAddressProperty().get();
        for (Transaction transaction : model.getAllTransactions()) {
            if (transaction.senderProperty().get().equals(currentPayee)) {
                expense += transaction.amountProperty().get();
            } else {
                income += transaction.amountProperty().get();
            }
        }
        income_lbl.setText(String.format("+$%,.2f", income));
        expense_lbl.setText(String.format("-$%,.2f", expense));
    }
}
__MAZE_14_0__
mkdir -p "$ROOT/src/main/java/com/example/bankmangament/Controllers/Client"
cat > "$ROOT/src/main/java/com/example/bankmangament/Controllers/Client/TransactionCellController.java" <<'__MAZE_15_0__'
package com.example.bankmangament.Controllers.Client;

import com.example.bankmangament.Models.Model;
import com.example.bankmangament.Models.Transaction;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class TransactionCellController implements Initializable {
    public FontAwesomeIconView in_icon;
    public FontAwesomeIconView out_icon;
    public Label trans_date_lbl;
    public Label sender_lbl;
    public Label receiver_lbl;
    public Label amount_lbl;

    public Button message_btn;


   private final Transaction transaction;


   public  TransactionCellController(Transaction transaction) {
       this.transaction = transaction;
   }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
            sender_lbl.textProperty().bind(transaction.senderProperty());
            receiver_lbl.textProperty().bind(transaction.receiverProperty());
            amount_lbl.textProperty().bind(transaction.amountProperty().asString("$%,.2f"));
            trans_date_lbl.textProperty().bind(transaction.dateProperty().asString());

            message_btn.setOnAction(event -> Model.getInstance().getViewfactory().showMessageWindow(transaction.senderProperty().get(), transaction.messageProperty().get() ) );
            transactionIcon();
    }

    private void transactionIcon(){

       if(transaction.senderProperty().get().equals(Model.getInstance().getClient().payeeAddressProperty().get())){
           in_icon.setFill(Color.rgb(240 , 240 , 240));
           out_icon.setFill(Color.RED);


       }  else{
           in_icon.setFill(Color.GREEN);
           out_icon.setFill(Color.rgb(240 , 240 , 240));
       }
    }
}
__MAZE_15_0__
mkdir -p "$ROOT/src/main/java/com/example/bankmangament/Controllers/Client"
cat > "$ROOT/src/main/java/com/example/bankmangament/Controllers/Client/TransactionController.java" <<'__MAZE_16_0__'
package com.example.bankmangament.Controllers.Client;

import com.example.bankmangament.Models.Model;
import com.example.bankmangament.Models.Transaction;
import com.example.bankmangament.Views.TransactionCellFactory;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ResourceBundle;

public class TransactionController implements Initializable {
    public ListView<Transaction> Transaction_list_view;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().setAllTransactions();
        Transaction_list_view.setItems(Model.getInstance().getAllTransactions());
        Transaction_list_view.setCellFactory(cell -> new TransactionCellFactory());
    }
}
__MAZE_16_0__
mkdir -p "$ROOT/src/main/java/com/example/bankmangament/Controllers"
