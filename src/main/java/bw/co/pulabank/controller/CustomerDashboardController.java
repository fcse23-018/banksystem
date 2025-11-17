package bw.co.pulabank.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CustomerDashboardController {

    @FXML
    private Label welcomeLabel;

    public void initialize() {
        // You can set the welcome message or other initial properties here
        welcomeLabel.setText("Welcome, Valued Customer!");
    }

    // Add more methods to handle customer-specific actions
}
