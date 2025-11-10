package com.bankingsystem.controller;

import com.bankingsystem.dao.CustomerDAO;
import com.bankingsystem.view.DashboardView;
import com.bankingsystem.view.LoginView;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;

public class LoginController {
    private final LoginView view;
    private final Stage stage;

    public LoginController(LoginView view, Stage stage) {
        this.view = view;
        this.stage = stage;
    }

    /**
     * Handles the login action.
     * @param username The username entered by the user.
     * @param password The password entered by the user.
     */
    public void handleLogin(String username, String password) {
        CustomerDAO customerDAO = new CustomerDAO();
        // For now, we'll simulate a successful login
        // In a real application, you would validate the credentials against a database
        System.out.println("Login attempt with username: " + username);
        // if (customerDAO.validate(username, password)) { // Uncomment when DAO is implemented
            openDashboard();
        // } else {
            // Show an error message
        // }
    }

    /**
     * Opens the customer dashboard view.
     */
    private void openDashboard() {
        DashboardView dashboardView = new DashboardView(stage);
        Scene scene = new Scene(dashboardView, 800, 600);
        stage.setTitle("Customer Dashboard");
        stage.setScene(scene);
    }
}
