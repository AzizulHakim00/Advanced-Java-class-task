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
