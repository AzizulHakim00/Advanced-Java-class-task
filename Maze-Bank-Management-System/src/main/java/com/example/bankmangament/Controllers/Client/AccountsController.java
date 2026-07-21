package com.example.bankmangament.Controllers.Client;

import com.example.bankmangament.Models.CheckingAccount;
import com.example.bankmangament.Models.Model;
import com.example.bankmangament.Models.OperationResult;
import com.example.bankmangament.Models.SavingsAccount;
import com.example.bankmangament.Views.AlertManager;
import javafx.beans.binding.Bindings;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class AccountsController implements Initializable {
    public Label checkingt_acc_num;
    public Label transaction_limit;
    public Label ch_acc_date;
    public Label ch_acc_bal;
    public Label savings_acc_num;
    public Label withdrawal_limit;
    public Label sv_acc_date;
    public Label sv_acc_bal;
    public TextField amount_to_sv;
    public Button trans_sv_btn;
    public TextField amount_to_ch;
    public Button trans_to_ch_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bindData();
        trans_sv_btn.setOnAction(event -> transfer(true));
        trans_to_ch_btn.setOnAction(event -> transfer(false));
    }

    private void bindData() {
        Model model = Model.getInstance();
        CheckingAccount checking = (CheckingAccount) model.getClient().checkingAccountProperty().get();
        SavingsAccount savings = (SavingsAccount) model.getClient().savingsAccountProperty().get();

        checkingt_acc_num.textProperty().bind(checking.accountNumberProperty());
        transaction_limit.textProperty().bind(Bindings.format("%d transactions/day", checking.transactionLimitProperty()));
        ch_acc_date.textProperty().bind(model.getClient().dateCreatedProperty().asString());
        ch_acc_bal.textProperty().bind(Bindings.format("$%,.2f", checking.balanceProperty()));

        savings_acc_num.textProperty().bind(savings.accountNumberProperty());
        withdrawal_limit.textProperty().bind(Bindings.format("$%,.2f", savings.withdrawalLimitProperty()));
        sv_acc_date.textProperty().bind(model.getClient().dateCreatedProperty().asString());
        sv_acc_bal.textProperty().bind(Bindings.format("$%,.2f", savings.balanceProperty()));
    }

    private void transfer(boolean checkingToSavings) {
        TextField field = checkingToSavings ? amount_to_sv : amount_to_ch;
        double amount;
        try {
            amount = Double.parseDouble(field.getText().trim());
        } catch (Exception exception) {
            AlertManager.showError("Invalid amount", "Enter a numeric amount greater than zero.");
            return;
        }

        Model model = Model.getInstance();
        OperationResult result = model.getDataBaseDriver().transferBetweenAccounts(
                model.getClient().payeeAddressProperty().get(),
                checkingToSavings,
                amount
        );
        if (!result.success()) {
            AlertManager.showError("Transfer failed", result.message());
            return;
        }
        model.refreshCurrentClient();
        field.clear();
        AlertManager.showInfo("Transfer complete", result.message());
    }
}
