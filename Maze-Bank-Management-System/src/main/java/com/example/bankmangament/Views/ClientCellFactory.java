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
