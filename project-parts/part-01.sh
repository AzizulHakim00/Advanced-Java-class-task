#!/usr/bin/env bash
set -euo pipefail
ROOT=${ROOT:-Maze-Bank-Management-System}
mkdir -p "$ROOT/.github/workflows"
cat > "$ROOT/.github/workflows/maven.yml" <<'__MAZE_0_0__'
name: Maven Build

on:
  push:
    branches: [main, master]
  pull_request:

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - name: Check out repository
        uses: actions/checkout@v4
      - name: Set up Java 21
        uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: '21'
          cache: maven
      - name: Run tests
        run: mvn --batch-mode clean test
__MAZE_0_0__
mkdir -p "$ROOT/."
cat > "$ROOT/.gitignore" <<'__MAZE_1_0__'
target/
build/
.idea/
.vscode/
*.iml
*.ipr
*.iws
*.db-journal
*.db-shm
*.db-wal
.DS_Store

mazebank.db
__MAZE_1_0__
mkdir -p "$ROOT/."
cat > "$ROOT/README.md" <<'__MAZE_2_0__'
# Maze Bank Management System

Maze Bank is a JavaFX desktop banking simulation with separate client and administrator dashboards. It uses SQLite for local persistence and Maven for builds.

> This is an academic/demo application. It is not intended to process real money or store real banking information.

## Completed features

### Client

- Secure client login with PBKDF2 password hashing
- Legacy plain-text password migration after successful login
- Checking and savings account overview
- Live balance updates
- Send money to another registered client
- Atomic transfers with rollback on failure
- Receiver validation, self-transfer protection and insufficient-balance checks
- Savings withdrawal-limit validation
- Checking-to-savings and savings-to-checking transfers
- Latest and complete transaction history, ordered newest first
- Income and expense summary
- Client profile dialog
- Database-backed bug reports
- Logout and session cleanup

### Administrator

- Secure administrator login
- Create clients with checking and savings accounts
- Independent opening balances for both accounts
- Unique payee addresses and account numbers
- Password validation and hashing
- Browse all clients
- Search clients by payee address
- Additive deposits into savings accounts
- Delete clients and their related account data
- Form validation and clear error messages

### Engineering improvements

- Prepared statements instead of SQL string concatenation
- Atomic database transactions for money movement
- PBKDF2-HMAC-SHA256 password storage
- Database indexes and audit tables
- Automated JUnit tests for passwords and core banking operations
- GitHub Actions Maven build
- Cleaned FXML labels and fixed incomplete controllers

## Technology stack

- Java 21
- JavaFX 21.0.6
- FXML and CSS
- SQLite
- Maven Wrapper
- JUnit 5

## Demo login

On first launch, the automatically created database contains these demo accounts:

| Role | Username / Payee address | Password |
|---|---|---|
| Administrator | `Admin` | `Admin@123` |
| Client | `@bBaker1` | `Client@123` |

Change demo credentials before sharing the database publicly with real information.

## Run in IntelliJ IDEA

1. Open the folder containing `pom.xml`.
2. Select JDK 21 as the project SDK.
3. Allow Maven to download dependencies.
4. Run `com.example.bankmangament.App`.

## Run from a terminal

Run with Maven:

```bash
mvn clean javafx:run
```

The application creates `mazebank.db` automatically beside `pom.xml` on first launch and seeds the demo accounts.

## Run tests

```bash
mvn clean test
```

## Project structure

```text
src/main/java/com/example/bankmangament/
├── Controllers/
│   ├── Admin/
│   └── Client/
├── Models/
├── Views/
└── App.java

src/main/resources/
├── com/example/bankmangament/
│   ├── Admin/
│   ├── Client/
│   └── Login.fxml
├── Images/
└── Style/
```

## Database tables

- `Admins`
- `Clients`
- `CheckingAccounts`
- `SavingsAccounts`
- `Transactions`
- `AccountTransfers`
- `BugReports`

## Important note

Money values use SQLite `REAL` fields because this project inherited that schema. A production-grade financial system should use fixed-precision decimal values, comprehensive authorization, encrypted backups, immutable audit logging, multi-factor authentication and an audited server-side architecture.
__MAZE_2_0__
mkdir -p "$ROOT/."
cat > "$ROOT/pom.xml" <<'__MAZE_3_0__'
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.azizulhakim</groupId>
    <artifactId>maze-bank-management</artifactId>
    <version>1.0.0</version>
    <name>Maze Bank Management System</name>
    <description>JavaFX desktop banking management system with SQLite.</description>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.release>21</maven.compiler.release>
        <javafx.version>21.0.6</javafx.version>
        <junit.version>5.10.2</junit.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.openjfx</groupId>
            <artifactId>javafx-controls</artifactId>
            <version>${javafx.version}</version>
        </dependency>
        <dependency>
            <groupId>org.openjfx</groupId>
            <artifactId>javafx-fxml</artifactId>
            <version>${javafx.version}</version>
        </dependency>
        <dependency>
            <groupId>de.jensd</groupId>
            <artifactId>fontawesomefx-fontawesome</artifactId>
            <version>4.7.0-9.1.2</version>
        </dependency>
        <dependency>
            <groupId>org.xerial</groupId>
            <artifactId>sqlite-jdbc</artifactId>
            <version>3.45.3.0</version>
        </dependency>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>${junit.version}</version>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.13.0</version>
                <configuration>
                    <release>${maven.compiler.release}</release>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>3.2.5</version>
                <configuration>
                    <useModulePath>false</useModulePath>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.openjfx</groupId>
                <artifactId>javafx-maven-plugin</artifactId>
                <version>0.0.8</version>
                <configuration>
                    <mainClass>com.example.bankmangament/com.example.bankmangament.App</mainClass>
                    <launcher>maze-bank</launcher>
                    <jlinkZipName>maze-bank</jlinkZipName>
                    <jlinkImageName>maze-bank</jlinkImageName>
                    <noManPages>true</noManPages>
                    <stripDebug>true</stripDebug>
                    <noHeaderFiles>true</noHeaderFiles>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
__MAZE_3_0__
mkdir -p "$ROOT/src/main/java/com/example/bankmangament"
cat > "$ROOT/src/main/java/com/example/bankmangament/App.java" <<'__MAZE_4_0__'
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
__MAZE_4_0__
mkdir -p "$ROOT/src/main/java/com/example/bankmangament/Controllers/Admin"
cat > "$ROOT/src/main/java/com/example/bankmangament/Controllers/Admin/AdminController.java" <<'__MAZE_5_0__'
package com.example.bankmangament.Controllers.Admin;

import com.example.bankmangament.Models.Model;
import com.example.bankmangament.Views.AdminMenuOptions;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminController implements Initializable {
    public BorderPane admin_parent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().getViewfactory().getAdminSelectMenuItem().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }
            switch (newValue) {
                case CLIENTS -> admin_parent.setCenter(Model.getInstance().getViewfactory().getClients_view());
                case DEPOSITS -> admin_parent.setCenter(Model.getInstance().getViewfactory().getDeposit_view());
                default -> admin_parent.setCenter(Model.getInstance().getViewfactory().getCreate_client_view());
            }
        });
        Model.getInstance().getViewfactory().getAdminSelectMenuItem().set(AdminMenuOptions.CREATECLIENT);
    }
}
__MAZE_5_0__
mkdir -p "$ROOT/src/main/java/com/example/bankmangament/Controllers/Admin"
cat > "$ROOT/src/main/java/com/example/bankmangament/Controllers/Admin/AdminMenuController.java" <<'__MAZE_6_0__'
package com.example.bankmangament.Controllers.Admin;

import com.example.bankmangament.Models.Model;
import com.example.bankmangament.Views.AdminMenuOptions;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminMenuController implements Initializable {
    public Button create_client_btn;
    public Button client_btn;
    public Button deposit_btn;
    public Button logout_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        create_client_btn.setOnAction(event -> select(AdminMenuOptions.CREATECLIENT));
        client_btn.setOnAction(event -> select(AdminMenuOptions.CLIENTS));
        deposit_btn.setOnAction(event -> select(AdminMenuOptions.DEPOSITS));
        logout_btn.setOnAction(event -> onLogout());
    }

    private void select(AdminMenuOptions option) {
        Model.getInstance().getViewfactory().getAdminSelectMenuItem().set(option);
    }

    private void onLogout() {
        Stage stage = (Stage) client_btn.getScene().getWindow();
        Model.getInstance().setAdminLoginSuccessFlag(false);
        Model.getInstance().getViewfactory().closeStage(stage);
        Model.getInstance().getViewfactory().showLoginWindow();
    }
}
__MAZE_6_0__
mkdir -p "$ROOT/src/main/java/com/example/bankmangament/Controllers/Admin"
cat > "$ROOT/src/main/java/com/example/bankmangament/Controllers/Admin/ClientCellContainer.java" <<'__MAZE_7_0__'
package com.example.bankmangament.Controllers.Admin;

import com.example.bankmangament.Models.Client;
import com.example.bankmangament.Models.Model;
import com.example.bankmangament.Models.OperationResult;
import com.example.bankmangament.Views.AlertManager;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientCellContainer implements Initializable {
    public Label fName_lbl;
    public Label lName_lbl;
    public Label pAddress_lbl;
    public Label ch_acc_lbl;
    public Label sv_acc_lbl;
    public Label date_lbl;
    public Button delete_btn;

    private final Client client;

    public ClientCellContainer(Client client) {
        this.client = client;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fName_lbl.textProperty().bind(client.firstNameProperty());
        lName_lbl.textProperty().bind(client.lastNameProperty());
        pAddress_lbl.textProperty().bind(client.payeeAddressProperty());
        ch_acc_lbl.textProperty().bind(client.checkingAccountProperty().get().accountNumberProperty());
        sv_acc_lbl.textProperty().bind(client.savingsAccountProperty().get().accountNumberProperty());
        date_lbl.textProperty().bind(client.dateCreatedProperty().asString());
        delete_btn.setOnAction(event -> deleteClient());
    }

    private void deleteClient() {
        String payeeAddress = client.payeeAddressProperty().get();
        boolean confirmed = AlertManager.confirm(
                "Delete client",
                "Delete " + client.firstNameProperty().get() + " " + client.lastNameProperty().get()
                        + " (" + payeeAddress + ") and all account data?"
        );
        if (!confirmed) {
            return;
        }
        OperationResult result = Model.getInstance().getDataBaseDriver().deleteClient(payeeAddress);
        if (!result.success()) {
            AlertManager.showError("Delete failed", result.message());
            return;
        }
        Model.getInstance().getClients().remove(client);
        AlertManager.showInfo("Client deleted", result.message());
    }
}
__MAZE_7_0__
mkdir -p "$ROOT/src/main/java/com/example/bankmangament/Controllers/Admin"
