package com.bankingsystem.view;

import com.bankingsystem.controller.DepositController;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DepositView extends Stage {

    private final DepositController controller;
    private final TextField amountField = new TextField();

    public DepositView(Stage owner, long accountNumber) {
        this.controller = new DepositController(this, accountNumber);

        initOwner(owner);
        initModality(Modality.APPLICATION_MODAL);
        setTitle("Deposit");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        layout.getChildren().addAll(
                new Label("Amount:"),
                amountField,
                new Button("Deposit") {{ setOnAction(e -> controller.handleDeposit()); }},
                new Button("Cancel") {{ setOnAction(e -> close()); }}
        );

        setScene(new Scene(layout));
    }

    public String getAmount() {
        return amountField.getText();
    }
}
