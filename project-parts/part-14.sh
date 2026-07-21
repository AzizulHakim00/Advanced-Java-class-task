#!/usr/bin/env bash
set -euo pipefail
ROOT=${ROOT:-Maze-Bank-Management-System}
cat > "$ROOT/src/main/resources/com/example/bankmangament/Client/Transaction.fxml" <<'__MAZE_55_0__'
<?xml version="1.0" encoding="UTF-8"?>

<?import javafx.scene.control.ListView?>
<?import javafx.scene.layout.AnchorPane?>
<?import javafx.scene.text.Text?>

<AnchorPane prefHeight="750.0" prefWidth="850.0" styleClass="transaction_container" stylesheets="@../../../../Style/Transaction.css" xmlns="http://javafx.com/javafx/21" xmlns:fx="http://javafx.com/fxml/1" fx:controller="com.example.bankmangament.Controllers.Client.TransactionController">
    <Text layoutX="445.0" layoutY="53.0" strokeType="OUTSIDE" strokeWidth="0.0" text="Transactions" AnchorPane.leftAnchor="349.0" AnchorPane.topAnchor="14.0" />
    <ListView fx:id="Transaction_list_view" layoutX="29.0" layoutY="191.0" prefHeight="640.0" prefWidth="830.0" AnchorPane.leftAnchor="14.0" AnchorPane.topAnchor="100.0" />
</AnchorPane>
__MAZE_55_0__
mkdir -p "$ROOT/src/main/resources/com/example/bankmangament/Client"
cat > "$ROOT/src/main/resources/com/example/bankmangament/Client/TransactionCell.fxml" <<'__MAZE_56_0__'
<?xml version="1.0" encoding="UTF-8"?>

<?import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView?>
<?import javafx.scene.control.Button?>
<?import javafx.scene.control.Label?>
<?import javafx.scene.layout.AnchorPane?>
<?import javafx.scene.layout.VBox?>
<?import javafx.scene.shape.Line?>

<AnchorPane fx:id="l" prefHeight="60.0" prefWidth="550.0" styleClass="cell_container" stylesheets="@../../../../Style/TransactionCell.css" xmlns="http://javafx.com/javafx/21" xmlns:fx="http://javafx.com/fxml/1">
    <VBox layoutX="43.0" layoutY="5.0" prefHeight="50.0" prefWidth="50.0" styleClass="trans_icon_container" AnchorPane.bottomAnchor="5.0" AnchorPane.leftAnchor="14.0" AnchorPane.topAnchor="5.0">
        <FontAwesomeIconView fx:id="in_icon" glyphName="LONG_ARROW_RIGHT" size="17" />
        <FontAwesomeIconView fx:id="out_icon" glyphName="LONG_ARROW_LEFT" size="17" />
    </VBox>
    <Label fx:id="trans_date_lbl" layoutX="86.0" layoutY="22.0" styleClass="trans_date_lbl" text="2022-07-06" AnchorPane.leftAnchor="100.0" AnchorPane.topAnchor="22.0" />
    <Label fx:id="sender_lbl" layoutX="218.0" layoutY="22.0" styleClass="trans_pAddress_lbl" text="Patrik" AnchorPane.leftAnchor="200.0" AnchorPane.topAnchor="22.0" />
    <Line endY="20.0" layoutX="293.0" layoutY="12.0" AnchorPane.bottomAnchor="21.5" AnchorPane.leftAnchor="300.0" />
    <Label fx:id="receiver_lbl" layoutX="359.0" layoutY="22.0" styleClass="trans_pAddress_lbl" text="Benjmin" AnchorPane.leftAnchor="358.0" AnchorPane.topAnchor="22.0" />
    <Label fx:id="amount_lbl" layoutX="487.0" layoutY="15.0" styleClass="trans_ammount_lbl" text="2000" AnchorPane.rightAnchor="14.0" AnchorPane.topAnchor="15.0">
        <graphic>
          <FontAwesomeIconView glyphName="DOLLAR" />
        </graphic>
    </Label>
   <Button fx:id="message_btn" layoutX="424.0" layoutY="16.0" mnemonicParsing="false" styleClass="message_btn" AnchorPane.rightAnchor="80.0" AnchorPane.topAnchor="18.0">
      <graphic>
         <FontAwesomeIconView glyphName="BELL" size="14" />
      </graphic>
   </Button>
</AnchorPane>
__MAZE_56_0__
mkdir -p "$ROOT/src/main/resources/com/example/bankmangament"
cat > "$ROOT/src/main/resources/com/example/bankmangament/Login.fxml" <<'__MAZE_57_0__'
<?xml version="1.0" encoding="UTF-8"?>

<?import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView?>
<?import javafx.scene.control.Button?>
<?import javafx.scene.control.ChoiceBox?>
<?import javafx.scene.control.Label?>
<?import javafx.scene.control.PasswordField?>
<?import javafx.scene.control.TextField?>
<?import javafx.scene.layout.AnchorPane?>
<?import javafx.scene.layout.VBox?>
<?import javafx.scene.text.Text?>

<AnchorPane prefHeight="400.0" prefWidth="600.0" stylesheets="@../../../Style/login.css" xmlns="http://javafx.com/javafx/21" xmlns:fx="http://javafx.com/fxml/1" fx:controller="com.example.bankmangament.Controllers.LoginController">
    <VBox layoutX="30.0" layoutY="119.0" prefHeight="400.0" prefWidth="200.0" styleClass="login_logo_container" stylesheets="@../../../Style/login.css" AnchorPane.bottomAnchor="0.0" AnchorPane.leftAnchor="0.0" AnchorPane.topAnchor="0.0">
        <FontAwesomeIconView glyphName="BANK" size="30" />
        <Text strokeType="OUTSIDE" strokeWidth="0.0" text="Maze Bank" />
    </VBox>
    <VBox layoutX="362.0" layoutY="106.0" prefHeight="350.0" prefWidth="350.0" styleClass="login_form_container" stylesheets="@../../../Style/login.css" AnchorPane.rightAnchor="20.0" AnchorPane.topAnchor="20.0">
        <Label id="choice-prompt_text" text="Choose your Account Type" />
        <ChoiceBox fx:id="account_selector" prefWidth="90.0" styleClass="account_selector" stylesheets="@../../../Style/login.css" />
        <Label fx:id="payee_adress" text="Payee Address" />
        <TextField fx:id="payee_address_field" styleClass="input_field" stylesheets="@../../../Style/login.css" />
        <Label text="Password" />
        <PasswordField fx:id="password_field" styleClass="input_field" />
        <Button fx:id="login_btn" mnemonicParsing="false" text="Login" />
        <Label fx:id="error_lbl" styleClass="error_lbl"  />
    </VBox>
</AnchorPane>
__MAZE_57_0__
mkdir -p "$ROOT/src/test/java/com/example/bankmangament/Models"
cat > "$ROOT/src/test/java/com/example/bankmangament/Models/DataBaseDriverTest.java" <<'__MAZE_58_0__'
package com.example.bankmangament.Models;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DataBaseDriverTest {
    @TempDir
    Path tempDir;

    @Test
    void completesCoreBankingOperationsAtomically() {
        String url = "jdbc:sqlite:" + tempDir.resolve("test.db");
        try (DataBaseDriver database = new DataBaseDriver(url)) {
            assertTrue(database.createClientWithAccounts(
                    "Alice", "Rahman", "@arahman1", "Secret1!",
                    1_000, 1_000, "3201 000001", "3201 000002"
            ).success());
            assertTrue(database.createClientWithAccounts(
                    "Bob", "Karim", "@bkarim2", "Secret2!",
                    500, 500, "3201 000003", "3201 000004"
            ).success());

            assertTrue(database.authenticateClient("@arahman1", "Secret1!"));
            assertFalse(database.authenticateClient("@arahman1", "wrong"));

            OperationResult transfer = database.transferSavings("@arahman1", "@bkarim2", 200, "Test");
            assertTrue(transfer.success(), transfer.message());
            assertEquals(800, database.getSavingsAccount("@arahman1").balanceProperty().get(), 0.001);
            assertEquals(700, database.getSavingsAccount("@bkarim2").balanceProperty().get(), 0.001);
            assertEquals(1, database.getTransactions("@arahman1", -1).size());

            OperationResult internal = database.transferBetweenAccounts("@arahman1", true, 100);
            assertTrue(internal.success(), internal.message());
            assertEquals(900, database.getCheckingAccount("@arahman1").balanceProperty().get(), 0.001);
            assertEquals(900, database.getSavingsAccount("@arahman1").balanceProperty().get(), 0.001);

            assertTrue(database.depositSavings("@bkarim2", 50).success());
            assertEquals(750, database.getSavingsAccount("@bkarim2").balanceProperty().get(), 0.001);

            assertTrue(database.deleteClient("@bkarim2").success());
            assertTrue(database.findClient("@bkarim2").isEmpty());
        }
    }
}
__MAZE_58_0__
mkdir -p "$ROOT/src/test/java/com/example/bankmangament/Models"
cat > "$ROOT/src/test/java/com/example/bankmangament/Models/PasswordUtilsTest.java" <<'__MAZE_59_0__'
package com.example.bankmangament.Models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordUtilsTest {
    @Test
    void hashesAndVerifiesPasswords() {
        String hash = PasswordUtils.hashPassword("Secure123!");

        assertNotEquals("Secure123!", hash);
        assertTrue(PasswordUtils.isHashed(hash));
        assertTrue(PasswordUtils.matches("Secure123!", hash));
        assertFalse(PasswordUtils.matches("wrong", hash));
    }

    @Test
    void supportsLegacyPlainTextPasswords() {
        assertTrue(PasswordUtils.matches("legacy", "legacy"));
        assertFalse(PasswordUtils.matches("other", "legacy"));
    }
}
__MAZE_59_0__
