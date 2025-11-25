package com.banking.controller;

import com.banking.dao.AccountDAO;
import com.banking.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

/**
 * Controller for the Dashboard View.
 * Displays customer's accounts, balances, and calculated interest.
 * 
 * @author Donovan Ntsima (FCSE23-018)
 * @version 1.0
 */
public class DashboardController {
    
    @FXML
    private Label welcomeLabel;
    
    @FXML
    private ListView<String> accountListView;
    
    @FXML
    private TextArea accountDetailsArea;
    
    @FXML
    private Button openAccountButton;
    
    @FXML
    private Button depositButton;
    
    @FXML
    private Button withdrawButton;
    
    @FXML
    private Button historyButton;
    
    @FXML
    private Button refreshButton;
    
    @FXML
    private Button logoutButton;
    
    private Customer customer;
    private AccountDAO accountDAO;
    private ObservableList<String> accountList;
    
    /**
     * Initializes the controller.
     * Called automatically after FXML elements are loaded.
     */
    @FXML
    public void initialize() {
        accountDAO = new AccountDAO();
        accountList = FXCollections.observableArrayList();
        accountListView.setItems(accountList);
        
        accountListView.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> displayAccountDetails()
        );
    }
    
    /**
     * Sets the customer and loads their accounts.
     * 
     * @param customer the logged-in customer
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
        welcomeLabel.setText("Welcome, " + customer.getFullName() + "!");
        loadAccounts();
    }
    
    /**
     * Loads all accounts for the current customer from the database.
     */
    private void loadAccounts() {
        List<Account> accounts = accountDAO.getAccountsByCustomer(customer.getCustomerID(), customer);
        customer.setAccounts(accounts);
        
        accountList.clear();
        for (Account account : accounts) {
            accountList.add(account.toString());
        }
        
        if (!accounts.isEmpty()) {
            accountListView.getSelectionModel().selectFirst();
        }
    }
    
    /**
     * Displays detailed information for the selected account.
     * Shows balance, account type, and monthly interest (if applicable).
     */
    private void displayAccountDetails() {
        int selectedIndex = accountListView.getSelectionModel().getSelectedIndex();
        
        if (selectedIndex >= 0 && selectedIndex < customer.getAccounts().size()) {
            Account account = customer.getAccounts().get(selectedIndex);
            
            StringBuilder details = new StringBuilder();
            details.append("Account Number: ").append(account.getAccountNumber()).append("\n");
            details.append("Account Type: ").append(account.getAccountType()).append("\n");
            details.append("Branch: ").append(account.getBranch()).append("\n");
            details.append("Current Balance: BWP ").append(String.format("%.2f", account.getBalance())).append("\n\n");
            
            if (account instanceof InterestBearing) {
                InterestBearing interestAccount = (InterestBearing) account;
                double interest = interestAccount.calculateInterest();
                details.append("Monthly Interest: BWP ").append(String.format("%.2f", interest)).append("\n");
                
                if (account instanceof SavingsAccount) {
                    details.append("Interest Rate: 0.05% per month\n");
                    details.append("Note: Withdrawals are NOT allowed from Savings accounts.\n");
                } else if (account instanceof InvestmentAccount) {
                    details.append("Interest Rate: 5% per month\n");
                    details.append("Minimum Balance Requirement: BWP 500.00\n");
                }
            }
            
            if (account instanceof ChequeAccount) {
                ChequeAccount chequeAccount = (ChequeAccount) account;
                details.append("\nEmployer Information:\n");
                details.append("Company: ").append(chequeAccount.getEmployerCompany()).append("\n");
                details.append("Address: ").append(chequeAccount.getEmployerAddress()).append("\n");
            }
            
            accountDetailsArea.setText(details.toString());
        }
    }
    
    /**
     * Handles open account button click.
     * Opens the account creation view.
     * 
     * @param event the action event
     */
    @FXML
    private void handleOpenAccount(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/OpenAccountView.fxml"));
            Parent root = loader.load();
            
            AccountController accountController = loader.getController();
            accountController.setCustomerAndDashboard(customer, this);
            
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Open New Account");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Navigation Error", 
                "Could not open account creation view: " + e.getMessage());
        }
    }
    
    /**
     * Handles deposit button click.
     * Opens the deposit view.
     * 
     * @param event the action event
     */
    @FXML
    private void handleDeposit(ActionEvent event) {
        if (customer.getAccounts().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "No Accounts", 
                "Please open an account first before making deposits.");
            return;
        }
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DepositView.fxml"));
            Parent root = loader.load();
            
            AccountController accountController = loader.getController();
            accountController.setCustomerAndDashboard(customer, this);
            
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Deposit Funds");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Navigation Error", 
                "Could not open deposit view: " + e.getMessage());
        }
    }
    
    /**
     * Handles withdraw button click.
     * Opens the withdrawal view.
     * 
     * @param event the action event
     */
    @FXML
    private void handleWithdraw(ActionEvent event) {
        if (customer.getAccounts().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "No Accounts", 
                "Please open an account first before making withdrawals.");
            return;
        }
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/WithdrawView.fxml"));
            Parent root = loader.load();
            
            AccountController accountController = loader.getController();
            accountController.setCustomerAndDashboard(customer, this);
            
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Withdraw Funds");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Navigation Error", 
                "Could not open withdrawal view: " + e.getMessage());
        }
    }
    
    /**
     * Handles transfer button click.
     * Opens the transfer funds view.
     * 
     * @param event the action event
     */
    @FXML
    private void handleTransfer(ActionEvent event) {
        if (customer.getAccounts().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "No Accounts", 
                "Please open an account first before making transfers.");
            return;
        }
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/TransferView.fxml"));
            Parent root = loader.load();
            
            AccountController accountController = loader.getController();
            accountController.setCustomerAndDashboard(customer, this);
            
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Transfer Funds");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Navigation Error", 
                "Could not open transfer view: " + e.getMessage());
        }
    }
    
    /**
     * Handles transaction history button click.
     * Opens the transaction history view.
     * 
     * @param event the action event
     */
    @FXML
    private void handleHistory(ActionEvent event) {
        int selectedIndex = accountListView.getSelectionModel().getSelectedIndex();
        
        if (selectedIndex < 0) {
            showAlert(Alert.AlertType.WARNING, "No Selection", 
                "Please select an account to view its transaction history.");
            return;
        }
        
        try {
            Account selectedAccount = customer.getAccounts().get(selectedIndex);
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/TransactionHistoryView.fxml"));
            Parent root = loader.load();
            
            TransactionController transactionController = loader.getController();
            transactionController.setAccount(selectedAccount);
            
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Transaction History - " + selectedAccount.getAccountNumber());
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Navigation Error", 
                "Could not open transaction history: " + e.getMessage());
        }
    }
    
    /**
     * Handles refresh button click.
     * Reloads accounts from the database.
     * 
     * @param event the action event
     */
    @FXML
    private void handleRefresh(ActionEvent event) {
        loadAccounts();
        showAlert(Alert.AlertType.INFORMATION, "Refresh Complete", 
            "Account information has been refreshed.");
    }
    
    /**
     * Handles logout button click.
     * Returns to the login view.
     * 
     * @param event the action event
     */
    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoginView.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Banking System Login");
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Navigation Error", 
                "Could not return to login: " + e.getMessage());
        }
    }
    
    /**
     * Refreshes the dashboard (called from other controllers after transactions).
     */
    public void refresh() {
        loadAccounts();
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
