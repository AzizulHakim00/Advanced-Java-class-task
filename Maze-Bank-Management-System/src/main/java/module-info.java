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
