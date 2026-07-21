#!/usr/bin/env bash
set -euo pipefail
ROOT=${ROOT:-Maze-Bank-Management-System}
cat > "$ROOT/src/main/resources/com/example/bankmangament/Client/Accounts.fxml" <<'__MAZE_51_0__'
<?xml version="1.0" encoding="UTF-8"?>

<?import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView?>
<?import javafx.scene.control.Button?>
<?import javafx.scene.control.Label?>
<?import javafx.scene.control.TextField?>
<?import javafx.scene.layout.AnchorPane?>
<?import javafx.scene.layout.VBox?>
<?import javafx.scene.text.Text?>

<AnchorPane prefHeight="750.0" prefWidth="850.0" styleClass="accounts_view_container" stylesheets="@../../../../Style/Account.css" xmlns="http://javafx.com/javafx/21" xmlns:fx="http://javafx.com/fxml/1" fx:controller="com.example.bankmangament.Controllers.Client.AccountsController">
    <Text layoutX="55.0" layoutY="47.0" strokeType="OUTSIDE" strokeWidth="0.0" text="Checking Account" AnchorPane.leftAnchor="14.0" AnchorPane.topAnchor="14.0" />
    <VBox layoutX="12.0" layoutY="60.0" prefHeight="300.0" prefWidth="450.0" AnchorPane.leftAnchor="14.0" AnchorPane.topAnchor="40.0">
        <Text strokeType="OUTSIDE" strokeWidth="0.0" text="Account Number" />
        <Label fx:id="checkingt_acc_num" text="3452 4950" />
        <Text strokeType="OUTSIDE" strokeWidth="0.0" text="Transaction Limit" />
        <Label fx:id="transaction_limit" text="10" />
        <Text strokeType="OUTSIDE" strokeWidth="0.0" text="Date Created" />
        <Label fx:id="ch_acc_date" text="2022-0707" />
        <Text strokeType="OUTSIDE" strokeWidth="0.0" text="Balance" />
        <Label fx:id="ch_acc_bal" text="\$3,000.00" />
    </VBox>
    <Text layoutX="14.0" layoutY="436.0" strokeType="OUTSIDE" strokeWidth="0.0" text="Savings Account" AnchorPane.leftAnchor="14.0" AnchorPane.topAnchor="364.0" />
    <VBox layoutX="14.0" layoutY="397.0" prefHeight="300.0" prefWidth="450.0" AnchorPane.bottomAnchor="60.0" AnchorPane.leftAnchor="14.0">
        <Text strokeType="OUTSIDE" strokeWidth="0.0" text="Account Number" />
        <Label fx:id="savings_acc_num" text="3452 4950" />
        <Text strokeType="OUTSIDE" strokeWidth="0.0" text="Withdrawal Limit" />
        <Label fx:id="withdrawal_limit" text="10" />
        <Text strokeType="OUTSIDE" strokeWidth="0.0" text="Date Created" />
        <Label fx:id="sv_acc_date" text="2022-0707" />
        <Text strokeType="OUTSIDE" strokeWidth="0.0" text="Balance" />
        <Label fx:id="sv_acc_bal" text="\$12,000.00" />
    </VBox>
    <Text layoutX="569.0" layoutY="45.0" strokeType="OUTSIDE" strokeWidth="0.0" text="Move Funds To Savings Account" AnchorPane.rightAnchor="164.0" AnchorPane.topAnchor="38.0" />
    <TextField fx:id="amount_to_sv" layoutX="468.0" layoutY="90.0" prefHeight="25.0" prefWidth="216.0" AnchorPane.rightAnchor="164.0" AnchorPane.topAnchor="72.0" />
    <Button fx:id="trans_sv_btn" layoutX="538.0" layoutY="106.0" mnemonicParsing="false" prefHeight="25.0" prefWidth="216.0" text="Transfer" AnchorPane.rightAnchor="164.0" AnchorPane.topAnchor="123.0">
        <graphic>
          <FontAwesomeIconView fill="WHITE" glyphName="ARROW_DOWN" size="20" />
        </graphic>
    </Button>
    <Text layoutX="489.0" layoutY="370.0" strokeType="OUTSIDE" strokeWidth="0.0" text="Move Funds To Checking Account" AnchorPane.rightAnchor="164.0" AnchorPane.topAnchor="364.0" />
    <TextField fx:id="amount_to_ch" layoutX="470.0" layoutY="402.0" prefHeight="25.0" prefWidth="216.0" AnchorPane.rightAnchor="164.0" AnchorPane.topAnchor="392.0" />
    <Button fx:id="trans_to_ch_btn" layoutX="470.0" layoutY="451.0" mnemonicParsing="false" prefHeight="25.0" prefWidth="216.0" text="Transfer" AnchorPane.rightAnchor="164.0" AnchorPane.topAnchor="441.0">
        <graphic>
          <FontAwesomeIconView fill="WHITE" glyphName="ARROW_UP" size="20" />
        </graphic>
    </Button>
</AnchorPane>
__MAZE_51_0__
mkdir -p "$ROOT/src/main/resources/com/example/bankmangament/Client"
cat > "$ROOT/src/main/resources/com/example/bankmangament/Client/Client.fxml" <<'__MAZE_52_0__'
<?xml version="1.0" encoding="UTF-8"?>

<?import javafx.scene.layout.BorderPane?>

<BorderPane fx:id="client_parent" xmlns="http://javafx.com/javafx/21" xmlns:fx="http://javafx.com/fxml/1">
    <left>
        <fx:include source="ClientMenu.fxml" />
    </left>
</BorderPane>
__MAZE_52_0__
mkdir -p "$ROOT/src/main/resources/com/example/bankmangament/Client"
cat > "$ROOT/src/main/resources/com/example/bankmangament/Client/ClientMenu.fxml" <<'__MAZE_53_0__'
<?xml version="1.0" encoding="UTF-8"?>

<?import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView?>
<?import javafx.scene.control.Button?>
<?import javafx.scene.control.Label?>
<?import javafx.scene.layout.AnchorPane?>
<?import javafx.scene.layout.VBox?>
<?import javafx.scene.shape.Line?>
<?import javafx.scene.text.Text?>

<VBox maxHeight="-Infinity" maxWidth="-Infinity" minHeight="-Infinity" minWidth="-Infinity" prefHeight="750.0" prefWidth="180.0" styleClass="main_menu_container" stylesheets="@../../../../Style/ClientMenu.css" xmlns="http://javafx.com/javafx/21" xmlns:fx="http://javafx.com/fxml/1" fx:controller="com.example.bankmangament.Controllers.Client.ClientMenuController">
   <AnchorPane prefHeight="730.0" prefWidth="160.0" styleClass="left_container">
      <VBox prefHeight="80.0" prefWidth="160.0" styleClass="title_container" AnchorPane.leftAnchor="0.0" AnchorPane.rightAnchor="0.0" AnchorPane.topAnchor="0.0">
         <FontAwesomeIconView glyphName="BANK" size="30" />
         <Text strokeType="OUTSIDE" strokeWidth="0.0" text="Maze Bank" />
      </VBox>
      <VBox layoutX="31.0" layoutY="40.0" prefHeight="400.0" prefWidth="160.0" styleClass="menu_container" AnchorPane.leftAnchor="0.0" AnchorPane.rightAnchor="0.0" AnchorPane.topAnchor="100.0">
         <Button fx:id="dassboard_btn" mnemonicParsing="false" text="DashBoard">
            <graphic>
               <FontAwesomeIconView glyphName="HOME" size="20" />
            </graphic>
         </Button>
         <Button fx:id="transaction_btn" mnemonicParsing="false" text="Transactions">
            <graphic>
               <FontAwesomeIconView glyphName="HANDSHAKE_ALT" size="20" />
            </graphic>
         </Button>
         <Button fx:id="account_btn" mnemonicParsing="false" text="Accounts">
            <graphic>
               <FontAwesomeIconView glyphName="ADDRESS_BOOK_ALT" size="20" />
            </graphic>
         </Button>
         <Line endX="120.0" />
         <Button fx:id="profile_btn" mnemonicParsing="false" styleClass="alt_menu_btn" text="Profile">
            <graphic>
               <FontAwesomeIconView glyphName="USER" size="20" />
            </graphic>
         </Button>
         <Button fx:id="logout_btn" mnemonicParsing="false" styleClass="alt_menu_btn" text="Logout">
            <graphic>
               <FontAwesomeIconView glyphName="EXTERNAL_LINK" size="20" />
            </graphic>
         </Button>
      </VBox>
      <VBox layoutX="25.0" layoutY="610.0" prefHeight="120.0" prefWidth="130.0" styleClass="report_container" AnchorPane.bottomAnchor="0.0" AnchorPane.leftAnchor="14.0" AnchorPane.rightAnchor="14.0">
         <Text strokeType="OUTSIDE" strokeWidth="0.0" text="Report Bug" />
         <Label text="Use this to report any errors ..." />
         <Button fx:id="report_btn" mnemonicParsing="false" text="Report">
            <graphic>
               <FontAwesomeIconView glyphName="PLUS" />
            </graphic>
         </Button>
      </VBox>
   </AnchorPane>
</VBox>
__MAZE_53_0__
mkdir -p "$ROOT/src/main/resources/com/example/bankmangament/Client"
cat > "$ROOT/src/main/resources/com/example/bankmangament/Client/Dashboard.fxml" <<'__MAZE_54_0__'
<?xml version="1.0" encoding="UTF-8"?>

<?import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView?>
<?import java.lang.String?>
<?import javafx.scene.control.Button?>
<?import javafx.scene.control.Label?>
<?import javafx.scene.control.ListView?>
<?import javafx.scene.control.TextArea?>
<?import javafx.scene.control.TextField?>
<?import javafx.scene.layout.AnchorPane?>
<?import javafx.scene.layout.HBox?>
<?import javafx.scene.layout.VBox?>
<?import javafx.scene.shape.Line?>
<?import javafx.scene.text.Text?>

<AnchorPane prefHeight="750.0" prefWidth="850.0" styleClass="dashboard" stylesheets="@../../../../Style/Dashboard.css" xmlns="http://javafx.com/javafx/21" xmlns:fx="http://javafx.com/fxml/1" fx:controller="com.example.bankmangament.Controllers.Client.DashboardController">
   <Text fx:id="user_name" layoutX="14.0" layoutY="19.0" strokeType="OUTSIDE" strokeWidth="0.0" styleClass="user_name" text="Hi , Azizul" AnchorPane.leftAnchor="20.0" AnchorPane.topAnchor="30.0" />
   <Label fx:id="login_date" layoutX="14.0" layoutY="6.0" styleClass="date_lbl" text="Today, 2022-03-12" AnchorPane.rightAnchor="14.0" AnchorPane.topAnchor="27.0" />
   <Text layoutX="825.0" layoutY="108.0" strokeType="OUTSIDE" strokeWidth="0.0" styleClass="section_title" text="Account Summary" AnchorPane.rightAnchor="14.0" AnchorPane.topAnchor="90.0" />
   <Text layoutX="725.0" layoutY="120.0" strokeType="OUTSIDE" strokeWidth="0.0" styleClass="section_title" text="My Accounts" AnchorPane.leftAnchor="20.0" AnchorPane.topAnchor="90.0" />
   <HBox layoutX="20.0" layoutY="120.0" prefHeight="180.0" prefWidth="620.0" styleClass="account_view" AnchorPane.leftAnchor="20.0" AnchorPane.topAnchor="120.0">
      <AnchorPane prefHeight="150.0" prefWidth="295.0">
         <children>
            <Label fx:id="checking_balance" layoutX="39.0" layoutY="38.0" styleClass="account_balance" text="\$63 500.56" AnchorPane.leftAnchor="14.0" AnchorPane.topAnchor="25.0" />
            <Label layoutX="92.0" layoutY="101.0" styleClass="account_number" text="**** **** **** " AnchorPane.bottomAnchor="45.0" AnchorPane.leftAnchor="14.0" />
            <Label fx:id="checking_acc_num" layoutX="134.0" layoutY="110.0" styleClass="account_number" text="4558" AnchorPane.bottomAnchor="47.0" AnchorPane.leftAnchor="138.0" />
            <VBox layoutX="233.0" layoutY="65.0" prefHeight="100.0" prefWidth="35.0" AnchorPane.bottomAnchor="-3.0" AnchorPane.rightAnchor="14.0" />
            <FontAwesomeIconView glyphName="BANK" layoutX="258.0" layoutY="29.0" size="30" AnchorPane.rightAnchor="14.0" AnchorPane.topAnchor="7.0" />
            <Text layoutX="29.0" layoutY="156.0" strokeType="OUTSIDE" strokeWidth="0.0" text="Checking Account" AnchorPane.bottomAnchor="10.0" AnchorPane.leftAnchor="14.0" />
         </children>
         <styleClass>
            <String fx:value="account" />
            <String fx:value="account_gradient" />
         </styleClass>
      </AnchorPane>
      <AnchorPane layoutX="10.0" layoutY="10.0" prefHeight="150.0" prefWidth="295.0">
         <styleClass>
            <String fx:value="account" />
            <String fx:value="account_gradient" />
         </styleClass>
         <Label fx:id="savings_bal" layoutX="39.0" layoutY="38.0" styleClass="account_balance" text="\$3 500.56" AnchorPane.leftAnchor="14.0" AnchorPane.topAnchor="25.0" />
         <Label layoutX="92.0" layoutY="101.0" styleClass="account_number" text="**** **** **** " AnchorPane.bottomAnchor="45.0" AnchorPane.leftAnchor="14.0" />
         <Label fx:id="savings_acc_num" layoutX="134.0" layoutY="110.0" styleClass="account_number" text="4558" AnchorPane.bottomAnchor="47.0" AnchorPane.leftAnchor="138.0" />
         <VBox layoutX="181.0" layoutY="-20.0" prefHeight="100.0" prefWidth="35.0" AnchorPane.bottomAnchor="0.0" AnchorPane.rightAnchor="14.0" />
         <FontAwesomeIconView glyphName="BANK" layoutX="258.0" layoutY="29.0" size="30" AnchorPane.rightAnchor="14.0" AnchorPane.topAnchor="7.0" />
         <Text layoutX="20.0" layoutY="150.0" strokeType="OUTSIDE" strokeWidth="0.0" text="Savings Accounbtr" AnchorPane.bottomAnchor="5.5" AnchorPane.leftAnchor="20.0" />
      </AnchorPane>
   </HBox>
   <VBox layoutX="647.0" layoutY="120.0" prefHeight="180.0" prefWidth="210.0" styleClass="summary_view" AnchorPane.rightAnchor="0.0" AnchorPane.topAnchor="120.0">
      <VBox prefHeight="200.0" prefWidth="100.0" styleClass="acc_summary_container">
         <children>
            <Text strokeType="OUTSIDE" strokeWidth="0.0" text="Income" />
            <Label fx:id="income_lbl" styleClass="income_amount" text="+ $4000.00" />
            <Line endX="160.0" />
            <Text strokeType="OUTSIDE" strokeWidth="0.0" text="Expensen" />
            <Label fx:id="expense_lbl" styleClass="expense_amount" text="- $1500.00" />
         </children>
      </VBox>
   </VBox>
   <Text layoutX="20.0" layoutY="380.0" strokeType="OUTSIDE" strokeWidth="0.0" styleClass="section_title" text="Latest Transaction" AnchorPane.leftAnchor="20.0" AnchorPane.topAnchor="340.0" />
   <Text layoutX="753.0" layoutY="360.0" strokeType="OUTSIDE" strokeWidth="0.0" styleClass="section_title" text="Send Money" AnchorPane.rightAnchor="14.0" AnchorPane.topAnchor="340.0" />
   <ListView fx:id="transaction_list_view" layoutX="54.0" layoutY="480.0" prefHeight="365.0" prefWidth="570.0" styleClass="transaction_listview" AnchorPane.bottomAnchor="14.0" AnchorPane.leftAnchor="20.0" />
   <VBox layoutX="695.0" layoutY="394.0" prefHeight="366.0" prefWidth="240.0" styleClass="new_trans_container" AnchorPane.bottomAnchor="14.0" AnchorPane.rightAnchor="14.0">
      <Label text="Payee Adress" />
      <TextField fx:id="payee_flb" />
      <Label text="Amount in $" />
      <TextField fx:id="amount_flb" />
      <Label text="Massage(optional)" />
      <TextArea fx:id="massage_flb" prefHeight="70.0" prefWidth="200.0" />
      <Button fx:id="send_money_btn" mnemonicParsing="false" text="Send Money" />
   </VBox>
</AnchorPane>
__MAZE_54_0__
mkdir -p "$ROOT/src/main/resources/com/example/bankmangament/Client"
