#!/usr/bin/env bash
set -euo pipefail
ROOT=${ROOT:-Maze-Bank-Management-System}
cat > "$ROOT/src/main/resources/com/example/bankmangament/Admin/AdminMenu.fxml" <<'__MAZE_46_0__'
<?xml version="1.0" encoding="UTF-8"?>

<?import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView?>
<?import javafx.scene.control.Button?>
<?import javafx.scene.layout.AnchorPane?>
<?import javafx.scene.layout.VBox?>
<?import javafx.scene.text.Text?>


<VBox maxHeight="-Infinity" maxWidth="-Infinity" minHeight="-Infinity" minWidth="-Infinity" prefHeight="750.0"
      prefWidth="180.0" styleClass="main_menu_container" stylesheets="@../../../../Style/ClientMenu.css"
      xmlns="http://javafx.com/javafx/21" xmlns:fx="http://javafx.com/fxml/1" fx:controller="com.example.bankmangament.Controllers.Admin.AdminMenuController">
    <AnchorPane prefHeight="730.0" prefWidth="160.0" styleClass="left_container">
        <VBox layoutX="40.0" layoutY="30.0" prefHeight="80.0" prefWidth="160.0" styleClass="title_container"
              AnchorPane.leftAnchor="0.0" AnchorPane.rightAnchor="0.0" AnchorPane.topAnchor="0.0">
            <children>
                <FontAwesomeIconView glyphName="BANK" size="30"/>
                <Text strokeType="OUTSIDE" strokeWidth="0.0" text="Maze Bank "/>
            </children>
        </VBox>
        <VBox layoutX="32.0" layoutY="265.0" prefHeight="400.0" prefWidth="160.0" styleClass="menu_container"
              AnchorPane.leftAnchor="0.0" AnchorPane.rightAnchor="0.0" AnchorPane.topAnchor="100.0">
            <Button fx:id="create_client_btn" mnemonicParsing="false" text="Create Client">
                <graphic>
                    <FontAwesomeIconView glyphName="PLUS" size="20"/>
                </graphic>
            </Button>
            <Button fx:id="client_btn" mnemonicParsing="false" text="Clients">
                <graphic>
                    <FontAwesomeIconView glyphName="LIST" size="20"/>
                </graphic>
            </Button>
            <Button fx:id="deposit_btn" mnemonicParsing="false" text="Deposit">
                <graphic>
                    <FontAwesomeIconView glyphName="MONEY" size="20"/>
                </graphic>
            </Button>
            <Button fx:id="logout_btn" mnemonicParsing="false" styleClass="alt_menu_btn" text="Logout">
                <graphic>
                    <FontAwesomeIconView glyphName="EXTERNAL_LINK" size="20"/>
                </graphic>
            </Button>
        </VBox>
    </AnchorPane>
</VBox>
__MAZE_46_0__
mkdir -p "$ROOT/src/main/resources/com/example/bankmangament/Admin"
cat > "$ROOT/src/main/resources/com/example/bankmangament/Admin/ClientCell.fxml" <<'__MAZE_47_0__'
<?xml version="1.0" encoding="UTF-8"?>

<?import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView?>
<?import javafx.scene.control.Button?>
<?import javafx.scene.control.Label?>
<?import javafx.scene.layout.AnchorPane?>
<?import javafx.scene.layout.HBox?>

<AnchorPane prefHeight="50.0" prefWidth="750.0" styleClass="client_cell_container" stylesheets="@../../../../Style/ClientCell.css" xmlns="http://javafx.com/javafx/21" xmlns:fx="http://javafx.com/fxml/1" >
    <FontAwesomeIconView glyphName="USER" layoutX="8.0" layoutY="29.0" size="20" AnchorPane.leftAnchor="14.0" AnchorPane.topAnchor="14.0" />
    <HBox layoutX="186.0" layoutY="5.0" prefHeight="40.0" prefWidth="550.0" AnchorPane.leftAnchor="60.0" AnchorPane.topAnchor="5.0">
        <children>
          <Label fx:id="fName_lbl" text="Benjaminh" />
          <Label fx:id="lName_lbl" layoutX="10.0" layoutY="10.0" text="Baker" />
          <Label fx:id="pAddress_lbl" layoutX="66.0" layoutY="10.0" text="\@bBaker1" />
          <Label fx:id="ch_acc_lbl" layoutX="96.0" layoutY="10.0" styleClass="acc_display_lbl" text="35469921" />
          <Label fx:id="sv_acc_lbl" layoutX="151.0" layoutY="10.0" styleClass="acc_display_lbl" text="23330213" />
          <Label fx:id="date_lbl" layoutX="203.0" layoutY="10.0" text="2022-07-07" />
        </children>
    </HBox>
    <Button fx:id="delete_btn" layoutX="684.0" layoutY="2.0" mnemonicParsing="false" styleClass="delete_btn" text="delete" AnchorPane.rightAnchor="14.0" AnchorPane.topAnchor="12.0">
        <graphic>
          <FontAwesomeIconView glyphName="WINDOW_CLOSE_ALT" size="15" />
        </graphic>
    </Button>
</AnchorPane>
__MAZE_47_0__
mkdir -p "$ROOT/src/main/resources/com/example/bankmangament/Admin"
cat > "$ROOT/src/main/resources/com/example/bankmangament/Admin/Clients.fxml" <<'__MAZE_48_0__'
<?xml version="1.0" encoding="UTF-8"?>

<?import javafx.scene.control.ListView?>
<?import javafx.scene.layout.AnchorPane?>
<?import javafx.scene.text.Text?>

<AnchorPane prefHeight="750.0" prefWidth="850.0" styleClass="client_container" stylesheets="@../../../../Style/Clients.css" xmlns="http://javafx.com/javafx/21" xmlns:fx="http://javafx.com/fxml/1" fx:controller="com.example.bankmangament.Controllers.Admin.ClientsController">
    <Text layoutX="63.0" layoutY="67.0" strokeType="OUTSIDE" strokeWidth="0.0" text="Clients" AnchorPane.leftAnchor="14.0" AnchorPane.topAnchor="14.0" />
    <ListView fx:id="clients_listview" layoutX="40.0" layoutY="53.0" prefHeight="650.0" prefWidth="800.0" stylesheets="@../../../../Style/Clients.css" AnchorPane.leftAnchor="14.0" AnchorPane.topAnchor="50.0" />
</AnchorPane>
__MAZE_48_0__
mkdir -p "$ROOT/src/main/resources/com/example/bankmangament/Admin"
cat > "$ROOT/src/main/resources/com/example/bankmangament/Admin/CreateClient.fxml" <<'__MAZE_49_0__'
<?xml version="1.0" encoding="UTF-8"?>

<?import javafx.scene.control.Button?>
<?import javafx.scene.control.CheckBox?>
<?import javafx.scene.control.Label?>
<?import javafx.scene.control.TextField?>
<?import javafx.scene.layout.AnchorPane?>
<?import javafx.scene.layout.VBox?>
<?import javafx.scene.text.Text?>

<AnchorPane prefHeight="750.0" prefWidth="850.0" styleClass="create_client_container" stylesheets="@../../../../Style/CreateClient.css" xmlns="http://javafx.com/javafx/21" xmlns:fx="http://javafx.com/fxml/1" fx:controller="com.example.bankmangament.Controllers.Admin.CreateClientController">
    <Text layoutX="35.0" layoutY="40.0" strokeType="OUTSIDE" strokeWidth="0.0" text="Create New Client Account" AnchorPane.leftAnchor="14.0" AnchorPane.topAnchor="14.0" />
    <VBox fillWidth="false" layoutX="14.0" layoutY="50.0" prefHeight="700.0" prefWidth="600.0" AnchorPane.leftAnchor="14.0" AnchorPane.topAnchor="40.0">
        <Text strokeType="OUTSIDE" strokeWidth="0.0" text="First Name " />
        <TextField fx:id="fName_fid" />
        <Text strokeType="OUTSIDE" strokeWidth="0.0" text="Last Name " />
        <TextField fx:id="LName_fid" />
        <Text strokeType="OUTSIDE" strokeWidth="0.0" text="Password" />
        <TextField fx:id="passsword_fid" />
        <Text strokeType="OUTSIDE" strokeWidth="0.0" text="Payee Address" />
        <CheckBox fx:id="pAddress_box" mnemonicParsing="false" />
        <Label fx:id="pAddress_lbl" styleClass="payee_address_lbl" />
        <Text layoutX="10.0" layoutY="148.0" strokeType="OUTSIDE" strokeWidth="0.0" text="Accounts" />
        <CheckBox fx:id="ch_acc_box" mnemonicParsing="false" text="Add Checking Account" />
        <Text layoutX="10.0" layoutY="147.0" strokeType="OUTSIDE" strokeWidth="0.0" text="Checking Account Balance" />
        <TextField fx:id="ch_amount_fid" layoutX="10.0" layoutY="109.0" />
        <CheckBox fx:id="sv_acc_box" layoutX="10.0" layoutY="185.0" mnemonicParsing="false" text="Add Savings Account" />
        <Text layoutX="10.0" layoutY="215.0" strokeType="OUTSIDE" strokeWidth="0.0" text="Savings Account Balance" />
        <TextField fx:id="sv_amount_fid" layoutX="10.0" layoutY="219.0" />
        <Button fx:id="create_client_btn" mnemonicParsing="false" text=" Create New Client" />
        <Label fx:id="error_lbl" layoutX="10.0" layoutY="169.0" styleClass="error_lbl" />
    </VBox>
</AnchorPane>
__MAZE_49_0__
mkdir -p "$ROOT/src/main/resources/com/example/bankmangament/Admin"
cat > "$ROOT/src/main/resources/com/example/bankmangament/Admin/Deposit.fxml" <<'__MAZE_50_0__'
<?xml version="1.0" encoding="UTF-8"?>

<?import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView?>
<?import javafx.scene.control.Button?>
<?import javafx.scene.control.ListView?>
<?import javafx.scene.control.TextField?>
<?import javafx.scene.layout.AnchorPane?>
<?import javafx.scene.layout.HBox?>
<?import javafx.scene.layout.VBox?>
<?import javafx.scene.text.Text?>

<AnchorPane prefHeight="750.0" prefWidth="850.0" stylesheets="@../../../../Style/Deposit.css" xmlns="http://javafx.com/javafx/21" xmlns:fx="http://javafx.com/fxml/1" fx:controller="com.example.bankmangament.Controllers.Admin.DepositController">
    <VBox layoutX="13.0" layoutY="65.0" prefHeight="570.0" prefWidth="820.0" AnchorPane.leftAnchor="15.0" AnchorPane.topAnchor="100.0">
        <children>
          <HBox alignment="CENTER" prefHeight="60.0" prefWidth="820.0" styleClass="search_box">
              <children>
                  <Text strokeType="OUTSIDE" strokeWidth="0.0" text="Search By payee Address" />
                  <TextField fx:id="payeeAdress_fid" />
                  <Button fx:id="search_btn" mnemonicParsing="false" text="Search">
                      <graphic>
                          <FontAwesomeIconView fill="WHITE" glyphName="SEARCH" size="20" />
                      </graphic>
                  </Button>
              </children>
          </HBox>
          <ListView fx:id="result_listview" prefHeight="200.0" prefWidth="820.0" styleClass="search_box" stylesheets="@../../../../Style/Deposit.css" />
          <VBox alignment="TOP_CENTER" fillWidth="false" prefHeight="315.0" prefWidth="100.0" styleClass="deposit_box">
              <children>
                  <Text strokeType="OUTSIDE" strokeWidth="0.0" text="Deposit Section" />
                  <Text strokeType="OUTSIDE" strokeWidth="0.0" text="Amount" />
                  <TextField fx:id="amount_fid" />
                  <Button fx:id="deposit_btn" mnemonicParsing="false" text="Deposit">
                      <graphic>
                          <FontAwesomeIconView fill="WHITE" glyphName="MONEY" size="20" />
                      </graphic>
                  </Button>
              </children>
          </VBox>
        </children>
    </VBox>
</AnchorPane>
__MAZE_50_0__
mkdir -p "$ROOT/src/main/resources/com/example/bankmangament/Client"
