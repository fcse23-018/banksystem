package com.banking.controller;

import com.banking.dao.CustomerDAO;
import com.banking.model.Customer;
import com.banking.util.PasswordUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller for the Registration View.
 * Handles new customer registration.
 * 
 * @author Donovan Ntsima (FCSE23-018)
 * @version 1.0
 */
public class RegisterController {
    
    @FXML
    private TextField firstNameField;
    
    @FXML
    private TextField surnameField;
    
    @FXML
    private TextField addressField;
    
    @FXML
    private TextField customerIDField;
    
    @FXML
    private PasswordField passwordField;
    
    @FXML
    private PasswordField confirmPasswordField;
    
    @FXML
    private Button registerButton;
    
    @FXML
    private Button backButton;
    
    private CustomerDAO customerDAO;
    
    /**
     * Initializes the controller.
     * Called automatically after FXML elements are loaded.
     */
    @FXML
    public void initialize() {
        customerDAO = new CustomerDAO();
    }
    
    /**
     * Handles register button click.
     * Validates input and creates a new customer.
     * 
     * @param event the action event
     */
    @FXML
    private void handleRegister(ActionEvent event) {
        String firstName = firstNameField.getText().trim();
        String surname = surnameField.getText().trim();
        String address = addressField.getText().trim();
        String customerID = customerIDField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        
        if (!validateInput(firstName, surname, address, customerID, password, confirmPassword)) {
            return;
        }
        
        if (customerDAO.customerExists(customerID)) {
            showAlert(Alert.AlertType.ERROR, "Registration Error", 
                "Customer ID already exists. Please choose a different ID.");
            return;
        }
        
        String hashedPassword = PasswordUtil.hashPassword(password);
        Customer customer = new Customer(customerID, firstName, surname, address, hashedPassword);
        
        if (customerDAO.createCustomer(customer)) {
            showAlert(Alert.AlertType.INFORMATION, "Registration Successful", 
                "Account created successfully! You can now log in.");
            
            try {
                openLogin();
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Navigation Error", 
                    "Could not return to login: " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Registration Error", 
                "Failed to create account. Please try again.");
        }
    }
    
    /**
     * Handles back button click.
     * Returns to the login view.
     * 
     * @param event the action event
     */
    @FXML
    private void handleBack(ActionEvent event) {
        try {
            openLogin();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Navigation Error", 
                "Could not return to login: " + e.getMessage());
        }
    }
    
    /**
     * Validates registration input.
     * 
     * @param firstName the first name
     * @param surname the surname
     * @param address the address
     * @param customerID the customer ID
     * @param password the password
     * @param confirmPassword the password confirmation
     * @return true if all inputs are valid, false otherwise
     */
    private boolean validateInput(String firstName, String surname, String address, 
                                  String customerID, String password, String confirmPassword) {
        if (firstName.isEmpty() || surname.isEmpty() || address.isEmpty() || 
            customerID.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", 
                "All fields are required. Please fill in all information.");
            return false;
        }
        
        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", 
                "Passwords do not match. Please re-enter your password.");
            return false;
        }
        
        if (password.length() < 6) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", 
                "Password must be at least 6 characters long.");
            return false;
        }
        
        if (customerID.length() < 4) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", 
                "Customer ID must be at least 4 characters long.");
            return false;
        }
        
        return true;
    }
    
    /**
     * Opens the login view.
     * 
     * @throws IOException if login FXML cannot be loaded
     */
    private void openLogin() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoginView.fxml"));
        Parent root = loader.load();
        
        Stage stage = (Stage) registerButton.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Banking System Login");
    }
    
    /**
     * Shows an alert dialog.
     * 
     * @param alertType the type of alert
     * @param title the alert title
     * @param message the alert message
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
