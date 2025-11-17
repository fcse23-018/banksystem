package com.botswanabank.project.controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private JFXTextField emailField;

    @FXML
    private JFXPasswordField passwordField;

    @FXML
    void handleLogin(ActionEvent event) {
        // Authenticate user
        // If successful, load dashboard
    }

    @FXML
    void handleRegister(ActionEvent event) throws IOException {
        // Load registration screen
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/botswanabank/project/view/Registration.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Register New Customer");
        stage.setScene(scene);
        stage.show();
    }
}
