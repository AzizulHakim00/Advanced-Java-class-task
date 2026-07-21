package com.example.bankmangament.Controllers.Admin;

import com.example.bankmangament.Models.Model;
import com.example.bankmangament.Models.OperationResult;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.security.SecureRandom;
import java.util.Locale;
import java.util.ResourceBundle;

public class CreateClientController implements Initializable {
    public TextField fName_fid;
    public TextField LName_fid;
    public TextField passsword_fid;
    public CheckBox pAddress_box;
    public Label pAddress_lbl;
    public CheckBox ch_acc_box;
    public TextField ch_amount_fid;
    public CheckBox sv_acc_box;
    public Button create_client_btn;
    public TextField sv_amount_fid;
    public Label error_lbl;

    private final SecureRandom random = new SecureRandom();
    private String payeeAddress = "";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        create_client_btn.setOnAction(event -> createClient());
        pAddress_box.selectedProperty().addListener((observable, oldValue, selected) -> {
            if (selected) {
                generatePayeeAddress();
            } else {
                payeeAddress = "";
                pAddress_lbl.setText("");
            }
        });
        fName_fid.textProperty().addListener((observable, oldValue, newValue) -> regenerateIfSelected());
        LName_fid.textProperty().addListener((observable, oldValue, newValue) -> regenerateIfSelected());
        ch_acc_box.setSelected(true);
        sv_acc_box.setSelected(true);
    }

    private void regenerateIfSelected() {
        if (pAddress_box.isSelected()) {
            generatePayeeAddress();
        }
    }

    private void generatePayeeAddress() {
        String firstName = clean(fName_fid.getText());
        String lastName = clean(LName_fid.getText());
        if (firstName.isBlank() || lastName.isBlank()) {
            payeeAddress = "";
            pAddress_lbl.setText("Enter first and last name first");
            return;
        }
        String safeLastName = lastName.replaceAll("[^A-Za-z0-9]", "");
        int id = Model.getInstance().getDataBaseDriver().getLastClientID() + 1;
        payeeAddress = ("@" + Character.toLowerCase(firstName.charAt(0)) + safeLastName + id).toLowerCase(Locale.ROOT);
        pAddress_lbl.setText(payeeAddress);
    }

    private void createClient() {
        error_lbl.setStyle("-fx-text-fill: red; -fx-font-size: 1.1em; -fx-font-weight: bold;");
        if (!ch_acc_box.isSelected() || !sv_acc_box.isSelected()) {
            error_lbl.setText("Both checking and savings accounts are required.");
            return;
        }
        if (payeeAddress.isBlank()) {
            generatePayeeAddress();
        }
        if (payeeAddress.isBlank()) {
            error_lbl.setText("Enter a valid name and generate a payee address.");
            return;
        }

        double checkingBalance;
        double savingsBalance;
        try {
            checkingBalance = parseOpeningBalance(ch_amount_fid.getText());
            savingsBalance = parseOpeningBalance(sv_amount_fid.getText());
        } catch (IllegalArgumentException exception) {
            error_lbl.setText(exception.getMessage());
            return;
        }

        String checkingNumber = generateUniqueAccountNumber();
        String savingsNumber;
        do {
            savingsNumber = generateUniqueAccountNumber();
        } while (savingsNumber.equals(checkingNumber));
        OperationResult result = Model.getInstance().getDataBaseDriver().createClientWithAccounts(
                fName_fid.getText(),
                LName_fid.getText(),
                payeeAddress,
                passsword_fid.getText(),
                checkingBalance,
                savingsBalance,
                checkingNumber,
                savingsNumber
        );

        if (!result.success()) {
            error_lbl.setText(result.message());
            return;
        }
        Model.getInstance().setClients();
        error_lbl.setStyle("-fx-text-fill: #167d35; -fx-font-size: 1.1em; -fx-font-weight: bold;");
        error_lbl.setText(result.message() + " Payee: " + payeeAddress);
        clearForm();
    }

    private double parseOpeningBalance(String value) {
        if (value == null || value.isBlank()) {
            return 0;
        }
        try {
            double amount = Double.parseDouble(value.trim());
            if (!Double.isFinite(amount) || amount < 0) {
                throw new NumberFormatException();
            }
            return amount;
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Opening balances must be valid non-negative numbers.");
        }
    }

    private String generateUniqueAccountNumber() {
        String number;
        do {
            number = String.format("3201 %06d", random.nextInt(1_000_000));
        } while (Model.getInstance().getDataBaseDriver().accountNumberExists(number));
        return number;
    }

    private void clearForm() {
        fName_fid.clear();
        LName_fid.clear();
        passsword_fid.clear();
        pAddress_box.setSelected(false);
        pAddress_lbl.setText("");
        ch_amount_fid.clear();
        sv_amount_fid.clear();
        ch_acc_box.setSelected(true);
        sv_acc_box.setSelected(true);
        payeeAddress = "";
    }

    private String clean(String value) {
        return value == null ? "" : value.trim();
    }
}
