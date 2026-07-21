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
