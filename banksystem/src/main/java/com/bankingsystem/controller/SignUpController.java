package com.bankingsystem.controller;

import com.bankingsystem.dao.CustomerDAO;
import com.bankingsystem.model.Customer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class SignUpController {

    @FXML
    private TextField firstNameField;
    @FXML
    private TextField surnameField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField idNumberField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;

    private CustomerDAO customerDAO;

    public SignUpController() {
        customerDAO = new CustomerDAO();
    }

    @FXML
    private void handleSignUp(ActionEvent event) {
        String firstName = firstNameField.getText();
        String surname = surnameField.getText();
        String address = addressField.getText();
        String idNumber = idNumberField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (firstName.isEmpty() || surname.isEmpty() || address.isEmpty() || idNumber.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please fill all the fields");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Passwords do not match");
            return;
        }

        try {
            if (!customerDAO.isIdNumberUnique(idNumber)) {
                showAlert(Alert.AlertType.ERROR, "Form Error!", "This ID Number is already registered.");
                return;
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error!", "There was an error checking the ID number.");
            e.printStackTrace();
            return;
        }

        Customer newCustomer = new Customer(0, firstName, surname, address, idNumber, "ACTIVE", "CUSTOMER");
        boolean success = customerDAO.addCustomer(newCustomer, password);

        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Registration Successful!", "You can now log in with your ID Number.");
            handleLoginLink(event);
        } else {
            showAlert(Alert.AlertType.ERROR, "Registration Failed!", "An unexpected error occurred. Please try again.");
        }
    }

    @FXML
    private void handleLoginLink(ActionEvent event) {
        try {
            Parent loginRoot = FXMLLoader.load(getClass().getResource("/com/bankingsystem/view/Login.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Banking System Login");
            stage.setScene(new Scene(loginRoot, 400, 400));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
