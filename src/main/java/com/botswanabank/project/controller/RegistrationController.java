package com.botswanabank.project.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class RegistrationController {

    @FXML
    private JFXTextField firstNameField;

    @FXML
    private JFXTextField surnameField;

    @FXML
    private JFXTextField addressField;

    @FXML
    private JFXTextField omangField;

    @FXML
    private JFXTextField emailField;

    @FXML
    private JFXTextField phoneField;

    @FXML
    private JFXComboBox<String> customerTypeComboBox;

    @FXML
    public void initialize() {
        customerTypeComboBox.setItems(FXCollections.observableArrayList("Individual", "Joint", "Minor", "Corporate"));
    }

    @FXML
    void handleRegister(ActionEvent event) {
        // Create new customer
        // Send OTP/validation email
    }
}
