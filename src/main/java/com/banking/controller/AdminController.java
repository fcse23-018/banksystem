package com.banking.controller;

import com.banking.dao.AccountDAO;
import com.banking.dao.CustomerDAO;
import com.banking.model.Admin;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.*;
import com.banking.util.DatabaseConnection;

public class AdminController {
    
    @FXML private Label adminNameLabel;
    @FXML private Label totalCustomersLabel;
    @FXML private Label totalAccountsLabel;
    @FXML private Label totalBalanceLabel;
    @FXML private TextArea activityArea;
    
    private Admin admin;
    
    @FXML
    public void initialize() {
        loadStatistics();
    }
    
    public void setAdmin(Admin admin) {
        this.admin = admin;
        adminNameLabel.setText("Welcome, " + admin.getFullName());
        loadStatistics();
    }
    
    private void loadStatistics() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Get total customers
            String customerSql = "SELECT COUNT(*) FROM CUSTOMERS";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(customerSql)) {
                if (rs.next()) {
                    totalCustomersLabel.setText(String.valueOf(rs.getInt(1)));
                }
            }
            
            // Get total accounts
            String accountSql = "SELECT COUNT(*) FROM ACCOUNTS";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(accountSql)) {
                if (rs.next()) {
                    totalAccountsLabel.setText(String.valueOf(rs.getInt(1)));
                }
            }
            
            // Get total balance
            String balanceSql = "SELECT SUM(balance) FROM ACCOUNTS";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(balanceSql)) {
                if (rs.next()) {
                    totalBalanceLabel.setText(String.format("%.2f", rs.getDouble(1)));
                }
            }
            
            // Load recent transactions
            String transactionSql = "SELECT * FROM TRANSACTIONS ORDER BY transaction_date DESC LIMIT 10";
            StringBuilder activity = new StringBuilder();
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(transactionSql)) {
                while (rs.next()) {
                    activity.append(String.format("%s | %s | BWP %.2f | %s\n",
                            rs.getTimestamp("transaction_date"),
                            rs.getString("account_number"),
                            rs.getDouble("amount"),
                            rs.getString("transaction_type")));
                }
            }
            activityArea.setText(activity.toString());
            
        } catch (SQLException e) {
            showError("Database Error", "Failed to load statistics: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleDashboard() {
        loadStatistics();
    }
    
    @FXML
    private void handleCustomers() {
        showInfo("Manage Customers", "Customer management feature coming soon!");
    }
    
    @FXML
    private void handleReports() {
        showInfo("Reports", "Reports feature coming soon!");
    }
    
    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoginView.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) adminNameLabel.getScene().getWindow();
            stage.setScene(new Scene(root, 450, 450));
            stage.setTitle("Banking System - Login");
            
        } catch (Exception e) {
            showError("Navigation Error", "Failed to return to login: " + e.getMessage());
        }
    }
    
    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
