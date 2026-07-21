package com.example.bankmangament.Controllers.Admin;

import com.example.bankmangament.Models.Client;
import com.example.bankmangament.Models.Model;
import com.example.bankmangament.Views.ClientCellFactory;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientsController implements Initializable {
    public ListView<Client> clients_listview;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().setClients();
        clients_listview.setItems(Model.getInstance().getClients());
        clients_listview.setCellFactory(cell -> new ClientCellFactory());
    }
}
