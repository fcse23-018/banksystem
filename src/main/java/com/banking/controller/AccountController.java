package com.banking.controller;

import com.banking.dao.AccountDAO;
import com.banking.dao.TransactionDAO;
import com.banking.model.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 * Controller for Account operations (Open, Deposit, Withdraw).
 * Handles account creation and transaction processing.
 * 
 * @author Donovan Ntsima (FCSE23-018)
 * @version 1.0
 */
public class AccountController {
    
    @FXML
    private ComboBox<String> accountTypeComboBox;
    
    @FXML
    private TextField initialDepositField;
    
    @FXML
    private TextField employerCompanyField;
    
    @FXML
    private TextField employerAddressField;
    
    @FXML
    private Label employerLabel;
    
    @FXML
    private ComboBox<String> accountSelectComboBox;
    
    @FXML
    private TextField amountField;
    
    @FXML
    private Button submitButton;
    
    @FXML
    private Button cancelButton;
    
    private Customer customer;
    private DashboardController dashboardController;
    private AccountDAO accountDAO;
    private TransactionDAO transactionDAO;
    
    /**
     * Initializes the controller.
     * Called automatically after FXML elements are loaded.
     */
    @FXML
    public void initialize() {
        accountDAO = new AccountDAO();
        transactionDAO = new TransactionDAO();
        
        if (accountTypeComboBox != null) {
            accountTypeComboBox.setItems(FXCollections.observableArrayList(
                "Savings Account", "Investment Account", "Cheque Account"
            ));
            
            accountTypeComboBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> handleAccountTypeSelection(newValue)
            );
        }
    }
    
    /**
     * Sets the customer and dashboard controller reference.
     * 
     * @param customer the current customer
     * @param dashboardController reference to the dashboard controller
     */
    public void setCustomerAndDashboard(Customer customer, DashboardController dashboardController) {
        this.customer = customer;
        this.dashboardController = dashboardController;
        
        if (accountSelectComboBox != null) {
            accountSelectComboBox.setItems(FXCollections.observableArrayList());
            for (Account account : customer.getAccounts()) {
                accountSelectComboBox.getItems().add(account.toString());
            }
            if (!customer.getAccounts().isEmpty()) {
                accountSelectComboBox.getSelectionModel().selectFirst();
            }
        }
    }
    
    /**
     * Handles account type selection in the combo box.
     * Shows/hides employer fields based on account type.
     * 
     * @param accountType the selected account type
     */
    private void handleAccountTypeSelection(String accountType) {
        boolean isChequeAccount = "Cheque Account".equals(accountType);
        
        if (employerCompanyField != null && employerAddressField != null && employerLabel != null) {
            employerCompanyField.setVisible(isChequeAccount);
            employerCompanyField.setManaged(isChequeAccount);
            employerAddressField.setVisible(isChequeAccount);
            employerAddressField.setManaged(isChequeAccount);
            employerLabel.setVisible(isChequeAccount);
            employerLabel.setManaged(isChequeAccount);
        }
    }
    
    /**
     * Handles submit button click for opening a new account.
     * 
     * @param event the action event
     */
    @FXML
    private void handleOpenAccount(ActionEvent event) {
        String accountType = accountTypeComboBox.getValue();
        String initialDepositStr = initialDepositField.getText().trim();
        
        if (accountType == null || initialDepositStr.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", 
                "Please select account type and enter initial deposit.");
            return;
        }
        
        try {
            double initialDeposit = Double.parseDouble(initialDepositStr);
            
            if (initialDeposit <= 0) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", 
                    "Initial deposit must be greater than zero.");
                return;
            }
            
            Account newAccount;
            
            if ("Investment Account".equals(accountType)) {
                if (initialDeposit < InvestmentAccount.getMinInitialDeposit()) {
                    showAlert(Alert.AlertType.ERROR, "Validation Error", 
                        String.format("Investment account requires minimum BWP %.2f", 
                        InvestmentAccount.getMinInitialDeposit()));
                    return;
                }
                newAccount = customer.openAccount("INVESTMENT", initialDeposit);
            } else if ("Cheque Account".equals(accountType)) {
                String employerCompany = employerCompanyField.getText().trim();
                String employerAddress = employerAddressField.getText().trim();
                
                if (employerCompany.isEmpty() || employerAddress.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Validation Error", 
                        "Please provide employer information for Cheque account.");
                    return;
                }
                
                newAccount = customer.openAccount("CHEQUE", initialDeposit, 
                    employerCompany + "|" + employerAddress);
            } else {
                newAccount = customer.openAccount("SAVINGS", initialDeposit);
            }
            
            if (accountDAO.createAccount(newAccount)) {
                Transaction transaction = new Transaction(
                    TransactionDAO.generateTransactionID(),
                    initialDeposit,
                    "DEPOSIT",
                    newAccount,
                    newAccount.getBalance()
                );
                transactionDAO.createTransaction(transaction);
                
                showAlert(Alert.AlertType.INFORMATION, "Success", 
                    "Account opened successfully!\nAccount Number: " + newAccount.getAccountNumber());
                
                dashboardController.refresh();
                closeWindow();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", 
                    "Failed to create account. Please try again.");
            }
            
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", 
                "Please enter a valid numeric amount.");
        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", e.getMessage());
        }
    }
    
    /**
     * Handles submit button click for deposit.
     * 
     * @param event the action event
     */
    @FXML
    private void handleDeposit(ActionEvent event) {
        int selectedIndex = accountSelectComboBox.getSelectionModel().getSelectedIndex();
        String amountStr = amountField.getText().trim();
        
        if (selectedIndex < 0 || amountStr.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", 
                "Please select an account and enter amount.");
            return;
        }
        
        try {
            double amount = Double.parseDouble(amountStr);
            
            if (amount <= 0) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", 
                    "Deposit amount must be greater than zero.");
                return;
            }
            
            Account account = customer.getAccounts().get(selectedIndex);
            account.deposit(amount);
            
            if (accountDAO.updateBalance(account.getAccountNumber(), account.getBalance())) {
                Transaction transaction = new Transaction(
                    TransactionDAO.generateTransactionID(),
                    amount,
                    "DEPOSIT",
                    account,
                    account.getBalance()
                );
                transactionDAO.createTransaction(transaction);
                
                showAlert(Alert.AlertType.INFORMATION, "Success", 
                    String.format("Deposited BWP %.2f successfully!\nNew Balance: BWP %.2f", 
                    amount, account.getBalance()));
                
                dashboardController.refresh();
                closeWindow();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", 
                    "Failed to process deposit. Please try again.");
            }
            
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", 
                "Please enter a valid numeric amount.");
        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", e.getMessage());
        }
    }
    
    /**
     * Handles submit button click for withdrawal.
     * 
     * @param event the action event
     */
    @FXML
    private void handleWithdraw(ActionEvent event) {
        int selectedIndex = accountSelectComboBox.getSelectionModel().getSelectedIndex();
        String amountStr = amountField.getText().trim();
        
        if (selectedIndex < 0 || amountStr.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", 
                "Please select an account and enter amount.");
            return;
        }
        
        try {
            double amount = Double.parseDouble(amountStr);
            
            if (amount <= 0) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", 
                    "Withdrawal amount must be greater than zero.");
                return;
            }
            
            Account account = customer.getAccounts().get(selectedIndex);
            
            if (account instanceof SavingsAccount) {
                showAlert(Alert.AlertType.WARNING, "Not Allowed", 
                    "Withdrawals are NOT allowed from Savings accounts.\n" +
                    "Please use Investment or Cheque account for withdrawals.");
                return;
            }
            
            account.withdraw(amount);
            
            if (accountDAO.updateBalance(account.getAccountNumber(), account.getBalance())) {
                Transaction transaction = new Transaction(
                    TransactionDAO.generateTransactionID(),
                    amount,
                    "WITHDRAWAL",
                    account,
                    account.getBalance()
                );
                transactionDAO.createTransaction(transaction);
                
                showAlert(Alert.AlertType.INFORMATION, "Success", 
                    String.format("Withdrew BWP %.2f successfully!\nNew Balance: BWP %.2f", 
                    amount, account.getBalance()));
                
                dashboardController.refresh();
                closeWindow();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", 
                    "Failed to process withdrawal. Please try again.");
            }
            
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", 
                "Please enter a valid numeric amount.");
        } catch (UnsupportedOperationException | IllegalArgumentException e) {
            showAlert(Alert.AlertType.ERROR, "Withdrawal Error", e.getMessage());
        }
    }
    
    /**
     * Handles transfer funds button click with PIN confirmation.
     * 
     * @param event the action event
     */
    @FXML
    private void handleTransfer(ActionEvent event) {
        try {
            ComboBox<String> fromAccountComboBox = (ComboBox<String>) submitButton.getScene().lookup("#fromAccountComboBox");
            TextField toAccountField = (TextField) submitButton.getScene().lookup("#toAccountField");
            TextField amountField = (TextField) submitButton.getScene().lookup("#amountField");
            PasswordField pinField = (PasswordField) submitButton.getScene().lookup("#pinField");
            
            if (fromAccountComboBox == null || fromAccountComboBox.getValue() == null) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Please select a source account.");
                return;
            }
            
            String toAccount = toAccountField.getText().trim();
            String amountStr = amountField.getText().trim();
            String pin = pinField.getText().trim();
            
            if (toAccount.isEmpty() || amountStr.isEmpty() || pin.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "All fields are required.");
                return;
            }
            
            // Validate PIN
            if (!customer.validatePin(pin)) {
                showAlert(Alert.AlertType.ERROR, "Invalid PIN", "The PIN you entered is incorrect.");
                return;
            }
            
            double amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                showAlert(Alert.AlertType.ERROR, "Invalid Amount", "Amount must be greater than zero.");
                return;
            }
            
            int selectedIndex = fromAccountComboBox.getSelectionModel().getSelectedIndex();
            Account fromAccount = customer.getAccounts().get(selectedIndex);
            
            if (fromAccount instanceof SavingsAccount) {
                showAlert(Alert.AlertType.WARNING, "Not Allowed", 
                    "Transfers from Savings accounts are not allowed.\nUse Investment or Cheque account.");
                return;
            }
            
            if (fromAccount.getBalance() < amount) {
                showAlert(Alert.AlertType.ERROR, "Insufficient Funds", 
                    String.format("Available balance: BWP %.2f", fromAccount.getBalance()));
                return;
            }
            
            // Perform transfer
            if (accountDAO.transferFunds(fromAccount.getAccountNumber(), toAccount, amount)) {
                fromAccount.withdraw(amount);
                
                Transaction transaction = new Transaction(
                    TransactionDAO.generateTransactionID(),
                    amount,
                    "TRANSFER",
                    fromAccount,
                    fromAccount.getBalance()
                );
                transaction.setTransferToAccount(toAccount);
                transactionDAO.createTransaction(transaction);
                
                showAlert(Alert.AlertType.INFORMATION, "Success", 
                    String.format("Transferred BWP %.2f to %s successfully!\nNew Balance: BWP %.2f", 
                    amount, toAccount, fromAccount.getBalance()));
                
                dashboardController.refresh();
                closeWindow();
            } else {
                showAlert(Alert.AlertType.ERROR, "Transfer Failed", 
                    "Transfer failed. Please check recipient account number and try again.");
            }
            
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please enter a valid numeric amount.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Transfer Error", e.getMessage());
        }
    }
    
    /**
     * Handles cancel button click.
     * Closes the current window.
     * 
     * @param event the action event
     */
    @FXML
    private void handleCancel(ActionEvent event) {
        closeWindow();
    }
    
    /**
     * Closes the current window.
     */
    private void closeWindow() {
        Stage stage = (Stage) submitButton.getScene().getWindow();
        stage.close();
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
