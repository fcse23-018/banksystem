package com.bankingsystem.view;

import com.bankingsystem.controller.TransferController;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TransferView extends Stage {

    private final TransferController controller;
    private final TextField destinationAccountField = new TextField();
    private final TextField amountField = new TextField();

    public TransferView(Stage owner, long sourceAccountNumber) {
        this.controller = new TransferController(this, sourceAccountNumber);

        initOwner(owner);
        initModality(Modality.APPLICATION_MODAL);
        setTitle("Transfer");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        layout.getChildren().addAll(
                new Label("Destination Account Number:"),
                destinationAccountField,
                new Label("Amount:"),
                amountField,
                new Button("Transfer") {{ setOnAction(e -> controller.handleTransfer()); }},
                new Button("Cancel") {{ setOnAction(e -> close()); }}
        );

        setScene(new Scene(layout));
    }

    public String getDestinationAccountNumber() {
        return destinationAccountField.getText();
    }

    public String getAmount() {
        return amountField.getText();
    }
}
