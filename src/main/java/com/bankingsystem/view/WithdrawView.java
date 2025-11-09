package com.bankingsystem.view;

import com.bankingsystem.controller.WithdrawController;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class WithdrawView extends Stage {

    private final WithdrawController controller;
    private final TextField amountField = new TextField();

    public WithdrawView(Stage owner, long accountNumber) {
        this.controller = new WithdrawController(this, accountNumber);

        initOwner(owner);
        initModality(Modality.APPLICATION_MODAL);
        setTitle("Withdraw");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        layout.getChildren().addAll(
                new Label("Amount:"),
                amountField,
                new Button("Withdraw") {{ setOnAction(e -> controller.handleWithdraw()); }},
                new Button("Cancel") {{ setOnAction(e -> close()); }}
        );

        setScene(new Scene(layout));
    }

    public String getAmount() {
        return amountField.getText();
    }
}
