package com.banking.controller;

import com.banking.dao.CustomerDAO;
import com.banking.model.Customer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller for the Login View.
 * Handles user authentication and navigation to registration.
 * 
 * @author Donovan Ntsima (FCSE23-018)
 * @version 1.0
 */
public class LoginController {
    
    @FXML
    private TextField customerIDField;
    
    @FXML
    private PasswordField passwordField;
    
    @FXML
    private Button loginButton;
    
    @FXML
    private Button registerButton;
    
    @FXML
    private Label statusLabel;
    
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
     * Handles login button click.
     * Authenticates user and navigates to dashboard if successful.
     * 
     * @param event the action event
     */
    @FXML
    private void handleLogin(ActionEvent event) {
        String customerID = customerIDField.getText().trim();
        String password = passwordField.getText();
        
        if (customerID.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Login Error", "Please enter both Customer ID and Password");
            return;
        }
        
        Customer customer = customerDAO.authenticate(customerID, password);
        
        if (customer != null) {
            showAlert(Alert.AlertType.INFORMATION, "Login Successful", 
                "Welcome back, " + customer.getFullName() + "!");
            
            try {
                openDashboard(customer);
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Navigation Error", 
                    "Could not open dashboard: " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Login Failed", 
                "Invalid Customer ID or Password");
            statusLabel.setText("Login failed. Please try again.");
        }
    }
    
    /**
     * Handles register button click.
     * Opens the registration view.
     * 
     * @param event the action event
     */
    @FXML
    private void handleRegister(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/RegisterView.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) registerButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Customer Registration");
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Navigation Error", 
                "Could not open registration: " + e.getMessage());
        }
    }
    
    /**
     * Opens the dashboard view for the logged-in customer.
     * 
     * @param customer the authenticated customer
     * @throws IOException if dashboard FXML cannot be loaded
     */
    private void openDashboard(Customer customer) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DashboardView.fxml"));
        Parent root = loader.load();
        
        DashboardController dashboardController = loader.getController();
        dashboardController.setCustomer(customer);
        
        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.setScene(new Scene(root, 800, 600));
        stage.setTitle("Banking Dashboard - " + customer.getFullName());
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
