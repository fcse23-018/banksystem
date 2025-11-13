package com.bankingsystem.controller;

import com.bankingsystem.dao.BankStaffDAO;
import com.bankingsystem.dao.CustomerDAO;
import com.bankingsystem.model.BankStaff;
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

public class LoginController {

    @FXML
    private TextField idNumberField;

    @FXML
    private PasswordField passwordField;

    private CustomerDAO customerDAO;
    private BankStaffDAO bankStaffDAO;

    public LoginController() {
        customerDAO = new CustomerDAO();
        bankStaffDAO = new BankStaffDAO();
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        String idNumber = idNumberField.getText();
        String password = passwordField.getText();

        if (idNumber.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "ID Number and Password cannot be empty.");
            return;
        }

        // Hardcoded admin check
        if ("admin".equals(idNumber) && "admin123".equals(password)) {
            System.out.println("Admin login successful");
            openDashboard(event, "admin");
            return;
        }

        Customer customer = customerDAO.validate(idNumber, password);
        if (customer != null) {
            System.out.println("Customer login successful for: " + customer.getFirstName());
            openDashboard(event, customer.getIdNumber());
            return;
        }

        BankStaff staff = bankStaffDAO.validate(idNumber, password);
        if (staff != null) {
            System.out.println("Staff login successful for: " + staff.getFirstName());
            openDashboard(event, staff.getIdNumber());
            return;
        }

        showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid ID Number or Password.");
    }

    @FXML
    private void handleSignUpLink(ActionEvent event) {
        try {
            Parent signUpRoot = FXMLLoader.load(getClass().getResource("/com/bankingsystem/view/SignUp.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Sign Up");
            stage.setScene(new Scene(signUpRoot, 400, 500));
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Could not load the sign up screen.");
        }
    }

    @FXML
    private void handleQuit() {
        // In a real app, you'd close DB connections gracefully here.
        System.exit(0);
    }

    private void openDashboard(ActionEvent event, String userId) {
         try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/bankingsystem/view/Dashboard.fxml"));
            Parent dashboardRoot = loader.load();

            DashboardController controller = loader.getController();
            controller.initData(userId); // Pass user ID or role to dashboard

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Dashboard");
            stage.setScene(new Scene(dashboardRoot, 1024, 768));

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Could not load the dashboard.");
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
