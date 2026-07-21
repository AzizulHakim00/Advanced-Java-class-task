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
