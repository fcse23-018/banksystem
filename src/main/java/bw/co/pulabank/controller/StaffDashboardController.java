package bw.co.pulabank.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class StaffDashboardController {

    @FXML
    private Label welcomeLabel;

    public void initialize() {
        // You can set the welcome message or other initial properties here
        welcomeLabel.setText("Welcome, Staff Member!");
    }

    // Add more methods to handle staff-specific actions
}
