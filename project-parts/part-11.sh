#!/usr/bin/env bash
set -euo pipefail
ROOT=${ROOT:-Maze-Bank-Management-System}
cat > "$ROOT/src/main/resources/Style/ClientCell.css" <<'__MAZE_36_0__'


.client_cell_container {
    -fx-background-color: #FFFFFF;
    -fx-background-radius: 10;
    -fx-effect: dropshadow(three-pass-box , #DDDDDD , 5 , 0 , 0 ,7);
}
.client_cell_container FontAwesomeIconView{
    -fx-fill: #132A13;
}
.client_cell_container HBox{
    -fx-spacing: 15;
    -fx-alignment: Center-left;
}
.client_cell_container HBox Label{
    -fx-text-fill: #444444;
    -fx-font-family: "Calibri Light";
    -fx-font-size: 1.5em;
}
.acc_display_lbl{
    -fx-font-family: Calibri;

    -fx-font-weight: bold;
   -fx-font-size: 1.5em;
}
.delete_btn{
    -fx-text-fill: #FFFFFF;
    -fx-background-color: #FF0000;
}


.delete_btn FontAwesomeIconView{
    -fx-fill: #FFFFFF;
}
.delete_btn:hover{
    -fx-cursor: hand;
}
__MAZE_36_0__
mkdir -p "$ROOT/src/main/resources/Style"
cat > "$ROOT/src/main/resources/Style/ClientMenu.css" <<'__MAZE_37_0__'

.main_menu_container{
    -fx-background-color: #EEEEEE;
    -fx-alignment: center;
    -fx-padding: 0 10 0 10 ;
}
.left_container{
   -fx-background-color: #FFFFFF;
    -fx-background-radius: 10 ;
    -fx-effect: dropshadow(two-pass-box, #AAAAAA, 5 ,0,0,4);
}
/* titile/logo Section */

.title_container{
    -fx-background-color: #FFFFFF;
    -fx-background-radius: 20 20 0 0 ;
    -fx-alignment: top_center;
    -fx-padding: 10;
}
.title_container FontAwesomeIconView{
    -fx-fill: #132A13;
}
.title_container Text{
        -fx-fill: #000000;
    -fx-font-size: 2em;
}

/* navigation Section */

.menu_container{
    -fx-padding: 10 10 10 20 ;
    -fx-spacing: 20;
}
.menu_container Button{
    -fx-pref-width: 115;
    -fx-pref-height: 40;
    -fx-background-color: #FFFFFF;
    -fx-text-fill: #132A13;
    -fx-font-size: 1.1em;
    -fx-alignment: center_left;
    -fx-effect: dropshadow(three-pass-box, #DDDDDD , 5, 0 , 0 , 6);
}
.menu_container Button:hover {
    -fx-cursor: hand;
}
.menu_container Button FontAwesomeIconView{
        -fx-fill: #132A13;
}
.alt_menu_btn{
    -fx-pref-width: 115;
    -fx-pref-height: 40;
    -fx-background-color: #132A13;
    -fx-text-fill: #FFFFFF;
    -fx-font-size: 1.1em;
    -fx-alignment: center_left;

}
.alt_menu_btn FontAwesomeIconView{
        -fx-fill:#FFFFFF;
}
.report_container{
    -fx-background-color: #EEFFEE;
    -fx-background-radius: 10;
    -fx-alignment: center;
    -fx-spacing: 10;
    -fx-padding: 10 0 10 0 ;

}
.report_container Text{
    -fx-font-weight: bold;
    -fx-fill: #132A13;
}
.report_container Label{
    -fx-text-alignment: center;
    -fx-wrap-text: true;
}
.report_container Button{
    -fx-pref-width: 80;
    -fx-background-color: #132A13;
    -fx-text-fill: #FFFFFF;
}
.report_container Button:hover{
    -fx-cursor: hand;
}
.report_container Button FontAwesomeIconView {
    -fx-fill: #FFFFFF;
}



__MAZE_37_0__
mkdir -p "$ROOT/src/main/resources/Style"
cat > "$ROOT/src/main/resources/Style/Clients.css" <<'__MAZE_38_0__'
.client_container{
    background-color: #EEEEEE;

}
.client_container Text {
    -fx-fill: #555555;
    -fx-font-size: 1.8em;
    -fx-font-family: "Calibri Light";
}
.client_container .list-view{
    -fx-background-color: #EEEEEE;
    -fx-border-color: 0;
}
.client_container .list-cell{
    -fx-background-color: #EEEEEE;
    -fx-padding: 10 0 10 0;
}

__MAZE_38_0__
mkdir -p "$ROOT/src/main/resources/Style"
cat > "$ROOT/src/main/resources/Style/CreateClient.css" <<'__MAZE_39_0__'
.create_client_container{
    -fx-background-color: #EEEEEE;
}
.create_client_container Text{
    -fx-font-size: 1.2em;
    -fx-fill: #888888;

}
.create_client_container VBox{
    -fx-border-width: 0 0 0 1;
    -fx-border-color: #000000;
    -fx-padding: 10;
    -fx-spacing: 15;
}
.create_client_container TextField{
    -fx-pref-width: 250;
    -fx-pref-height: 25;
    -fx-background-color: #FFFFFF;
    -fx-font-size: 1.3em;
    -fx-effect: dropshadow(three-pass-box,#AAAAAA , 3, 0 , 0 , 3);
}
.payee_address_lbl{
    -fx-pref-width: 250;
    -fx-pref-height: 30;
    -fx-padding: 5;
    -fx-background-color: #888888;
    -fx-background-radius: 5;
    -fx-text-fill: #FFFFFF;
    -fx-font-size: 1.3em;
    -fx-effect: dropshadow(three-pass-box , #AAAAAA , 3, 0 , 0 , 3);
}
.create_client_container Button{
    -fx-background-color: linear-gradient(to right ,#132A13, #253F25 );
    -fx-effect: dropshadow(three-pass-box , #AAAAAA , 3, 0 , 0, 3);
    -fx-pref-width: 250;
    -fx-font-size: 1.5em;
    -fx-font-family: "Calibri Light";
    -fx-text-fill: #FFFFFF;
}
.create_client_container Button:hover{
    -fx-cursor: hand;
}
.error_lbl {
    -fx-cursor: #FF0000;
}
__MAZE_39_0__
mkdir -p "$ROOT/src/main/resources/Style"
cat > "$ROOT/src/main/resources/Style/Dashboard.css" <<'__MAZE_40_0__'

.dashboard {
    -fx-background-color: #EEEEEE;
}


.user_name{
    -fx-font-size: 2.6em;
    -fx-font-family: "Calibri Light";
}
.date_lbl{
    -fx-text-fill: #AAAAAA;
    -fx-font-size: 1.1em;
    -fx-font-family: "Calibri Light";
}

.section_title{
    -fx-font-size: 1.4em;
    -fx-font-family: "Calibri Light";
}

/* Accounts view */
.account_view{
    -fx-background-color: #FFFFFF;
    -fx-background-radius: 10 0 0 10 ;
    -fx-padding: 10 0 10 0 ;
    -fx-alignment: center;
    -fx-border-width: 0 1 0 0;
    -fx-border-color: #DDDDDD;
    -fx-spacing: 10 ;
}

.account{
    -fx-background-radius: 5;
    -fx-effect: dropshadow(three-pass-box , #AAAAAA , 10 , 0 , 0 ,10 );

}
.account VBox {
    -fx-background-color: #FFFFFF;

}
.account Text {
    -fx-fill: #FFFFFF;
    -fx-font-family: "Calibri Light";
    -fx-font-size: 1.5em;
}
.account FontAwesomeIconView{
    -fx-fill: #FFFFFF;

}
.account_gradient {
    -fx-background-color: linear-gradient(to right , #123A13 , #253F25);
}
.account_balance{
    -fx-text-fill: #FFFFFF;
    -fx-font-family: "Calibri";
    -fx-font-size: 3em;
    -fx-font-weight: bold;
}
.account_number {
    -fx-text-fill: #FFFFFF;
    -fx-font-family: "Calibri";
    -fx-font-size: 1.5em;
    -fx-text-alignment: center;
}

/* Accounts Summary view */

.summary_view{
    -fx-background-color: #FFFFFF;
    -fx-padding: 5 10 5 10;
    -fx-spacing: 5;
    -fx-alignment: center_left;
}
.acc_summary_container{
    -fx-padding: 10;
    -fx-spacing: 10;
    -fx-alignment: center_left;
    -fx-border-radius: 10;
    -fx-border-color: #999999;
    -fx-border-width: 1;
    -fx-border-style: dashed;
}

.acc_summary_container Line{
    -fx-stroke: #AAAAAA;

}
.income_amount {
    -fx-text-fill: #00AA00;
    -fx-font-family: "Calibri Light";
    -fx-font-size: 1.5em;
}
.expense_amount{
    -fx-text-fill: #AA0000;
    -fx-font-family: "Calibri Light";
    -fx-font-size: 1.5em;
}


/* transactions view */
 .transaction_listview{
     -fx-background-color: #EEEEEE;
     -fx-border-width: 0 0 0 0;
     -fx-border-color: #AAAAAA;
 }
 .transaction_listview .scroll-bar:vertical {
     -fx-scale-x: 0;

 }

 .list-cell {
     -fx-background-color: #EEEEEE;
     -fx-padding: 15 0 15 0 ;

 }

 /* send money view */

.new_trans_container{
    -fx-background-color: #EEEEEE;
    -fx-padding: 20 10 20 10;
    -fx-spacing: 10;
    -fx-alignment: center_left;
}
.new_trans_container Label{
    -fx-font-family: "Calibri Light";
    -fx-font-size: 1.3em;

}
.new_trans_container TextField{

    -fx-pref-height: 40;
    -fx-background-color: #FFFFFF;
    -fx-effect: dropshadow(three-pass-box , #AAAAAA , 3, 0 , 0 , 3);

}
.new_trans_container Button {
    -fx-background-color: linear-gradient(to right , #123A13 , #253F25);
    -fx-effect: dropshadow(three-pass-box , #AAAAAA ,3,0 , 0 , 3);
    -fx-font-size: 1.5em;
    -fx-pref-width: 230;
    -fx-font-family: "Calibri Light";
    -fx-text-fill: #FFFFFF;
}
.new_trans_container Button:hover{
    -fx-cursor: hand;
}
__MAZE_40_0__
mkdir -p "$ROOT/src/main/resources/Style"
cat > "$ROOT/src/main/resources/Style/Deposit.css" <<'__MAZE_41_0__'
.deposit_container{
   -fx-background-color: #EEEEEE;
}

.search_box {
    -fx-spacing: 10 ;
    -fx-border-color: #AAAAAA;
    -fx-border-width: 0 0 1 0 ;
}

 Text {
    -fx-font-family: "Calibri Light";
    -fx-font-size: 1.6em;
    -fx-fill: #555555;
}

 TextField{
    -fx-pref-width: 200;
    -fx-pref-height: 35;
    -fx-background-color: #FFFFFF;
    -fx-effect: dropshadow(three-pass-box, #AAAAAA , 3  , 0 , 0 ,3);
}

 Button{
    -fx-background-color: linear-gradient(to right , #132A13 ,#253F25);
    -fx-effect: dropshadow(three-pass-box, #AAAAAA , 3  , 0 , 0 ,3);
     -fx-font-size: 1.6em;
     -fx-text-fill: #FFFFFF;
     -fx-font-family: "Calibri Light";
     -fx-pref-width: 200;
 }

Button:hover{
    -fx-cursor: hand;
}



 .list-view{
    -fx-background-color: #EEEEEE;
     -fx-border-width: 0;
}

 .list-cell{
    -fx-background-color: #EEEEEE;
    -fx-padding: 20 0 20 0 ;
}

 .deposit_box{
     -fx-spacing: 20 ;
     -fx-padding: 20 ;
 }
__MAZE_41_0__
mkdir -p "$ROOT/src/main/resources/Style"
cat > "$ROOT/src/main/resources/Style/Transaction.css" <<'__MAZE_42_0__'
.transaction_container{
    -fx-background-color: #EEEEEE;

}
.transaction_container Text{
    -fx-fill: #000000;
    -fx-font-size: 2.5em;
    -fx-font-family: "Calibri Light";
}
.transaction_container .list-view{
    -fx-background-color: #EEEEEE;
    -fx-padding: 10;
}
.transaction_container .list-cell{
    -fx-background-color: #EEEEEE;
    -fx-pref-width: 800;
}
__MAZE_42_0__
mkdir -p "$ROOT/src/main/resources/Style"
cat > "$ROOT/src/main/resources/Style/TransactionCell.css" <<'__MAZE_43_0__'


.cell_container{
    -fx-background-color: #FFFFFF;
    -fx-background-radius: 10;
-fx-effect: dropshadow(three-pass-box , #DDDDDD , 5 , 0 , 0 ,7);

}
.trans_icon_container{
    -fx-background-radius: #FFFFFF;
    -fx-alignment: center;
    -fx-spacing: 5;
}

.trans_date_lbl{
    -fx-text-fill: #AAAAAA;
    -fx-font-weight: 700;
}
.trans_pAddress_lbl{
    -fx-text-fill: #000000;
    -fx-font-weight: 700;
}
.cell_container Line{
    -fx-stroke: #EEEEEE;
}


.message_btn{
    -fx-background-color: #FFFFFF;

}
.message_btn FontAwesomeIconView{
    -fx-fill: #132A13;
}
.message_btn:hover{
    -fx-cursor: hand;
}

.trans_ammount_lbl{
    -fx-background-color: #132A13;
    -fx-background-radius: 2;
    -fx-text-fill: #FFFFFF;
    -fx-padding: 5;
    -fx-font-weight: 700;
}
.trans_ammount_lbl FontAwesomeIconView{
    -fx-fill: #FFFFFF;

}
__MAZE_43_0__
mkdir -p "$ROOT/src/main/resources/Style"
cat > "$ROOT/src/main/resources/Style/login.css" <<'__MAZE_44_0__'
.login-container{
    -fx-background-color: #EEEEEE;
}

.login_logo_container{
    -fx-background-color: #132A13;
    -fx-alignment: center;
}
.login_logo_container Text{
    -fx-font-size: 2em;
    -fx-fill: #FFFFFF;
}

.login_logo_container FontAwesomeIconView {
    -fx-fill: #ECF39E;
}

/* Account selector ChoiceBox Styles */

#choice-prompt_text{
    -fx-font-weight: 400;

}


   .account_selector{
    -fx-background-color: #ECF39E;
   }
   .account_selector .lebel{
       -fx-text-fill: #132A13;
       -fx-font-size: 1.1em;

   }
   .account_selector .arrow {
       -fx-background-color: #132A13;
   }
   #choice-box-popup-menu {
       -fx-background-color: #ECF39E;
   }

   /* login form styles */

.login_form_container {
    -fx-spacing: 15;
    -fx-alignment: top-left;
}
.login_form_container Label {
    -fx-font-size: 1.1em;
    -fx-font-weight: bold;
}
.input_field {
    -fx-background-color: #FFFFFF;
    -fx-pref-height: 35;
    -fx-pref-height: 1.2em;
}
.login_form_container Button {
    -fx-background-color: #132A13;
    -fx-text-fill: #FFFFFF;
    -fx-pref-height: 30;
    -fx-pref-width: 350;
    -fx-font-size: 1.2em;
}
.login_form_container Button:hover{
    -fx-cursor: hand;
}
.error_lbl{
    -fx-text-fill: #FF0000;
}
__MAZE_44_0__
mkdir -p "$ROOT/src/main/resources/com/example/bankmangament/Admin"
cat > "$ROOT/src/main/resources/com/example/bankmangament/Admin/Admin.fxml" <<'__MAZE_45_0__'
<?xml version="1.0" encoding="UTF-8"?>

<?import javafx.scene.layout.BorderPane?>

<BorderPane fx:id="admin_parent" xmlns="http://javafx.com/javafx/21" xmlns:fx="http://javafx.com/fxml/1">
    <left>
        <fx:include source="AdminMenu.fxml" />
    </left>
</BorderPane>
__MAZE_45_0__
mkdir -p "$ROOT/src/main/resources/com/example/bankmangament/Admin"
