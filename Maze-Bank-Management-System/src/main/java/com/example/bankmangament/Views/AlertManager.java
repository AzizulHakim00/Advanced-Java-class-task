package com.example.bankmangament.Views;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

import java.util.Optional;

public final class AlertManager {
    private AlertManager() {
    }

    public static void showInfo(String title, String message) {
        show(Alert.AlertType.INFORMATION, title, message);
    }

    public static void showWarning(String title, String message) {
        show(Alert.AlertType.WARNING, title, message);
    }

    public static void showError(String title, String message) {
        show(Alert.AlertType.ERROR, title, message);
    }

    public static boolean confirm(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.OK, ButtonType.CANCEL);
        alert.setTitle(title);
        alert.setHeaderText(null);
        return alert.showAndWait().filter(ButtonType.OK::equals).isPresent();
    }

    public static Optional<String> prompt(String title, String header, String placeholder) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(placeholder);
        return dialog.showAndWait().map(String::trim).filter(value -> !value.isBlank());
    }

    private static void show(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Backward-compatible helpers used by older code.
    public static void showInfoAlert() {
        showInfo("Information", "Operation completed.");
    }

    public static void showWarningAlert() {
        showWarning("Warning", "Please check the entered information.");
    }

    public static void showErrorAlert() {
        showError("Error", "The operation could not be completed.");
    }

    public static void showConfirmationAlert() {
        confirm("Confirmation", "Do you want to proceed?");
    }
}
