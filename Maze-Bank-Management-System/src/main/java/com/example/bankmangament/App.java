package com.example.bankmangament;

import com.example.bankmangament.Models.Model;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage stage) {
        Model.getInstance().getViewfactory().showLoginWindow();
    }

    @Override
    public void stop() {
        Model.getInstance().getDataBaseDriver().close();
    }
}
