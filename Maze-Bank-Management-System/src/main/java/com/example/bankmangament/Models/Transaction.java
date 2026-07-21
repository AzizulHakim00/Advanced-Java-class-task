package com.example.bankmangament.Models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;

public class Transaction {
    private final StringProperty sender;
    private final StringProperty receiver;
    private final DoubleProperty amount;
    private final ObjectProperty<LocalDate> date;
    private final StringProperty message;

    public Transaction(String sender, String receiver, Double amount, LocalDate date, String message) {
        this.sender = new SimpleStringProperty(this, "Sender", sender);
        this.receiver = new SimpleStringProperty(this, "Receiver", receiver);
        this.amount = new SimpleDoubleProperty(this, "Amount", amount);
        this.date = new SimpleObjectProperty<>(this, "Date", date);
        this.message = new SimpleStringProperty(this, "Message", message == null ? "" : message);
    }

    public StringProperty senderProperty() {
        return sender;
    }

    public StringProperty receiverProperty() {
        return receiver;
    }

    // Kept for compatibility with older controller code.
    public StringProperty reciverProperty() {
        return receiverProperty();
    }

    public DoubleProperty amountProperty() {
        return amount;
    }

    public ObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    public StringProperty messageProperty() {
        return message;
    }
}
