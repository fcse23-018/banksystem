package com.bankingsystem.controller;

import com.bankingsystem.dao.AccountDAO;
import com.bankingsystem.dao.TransactionDAO;
import com.bankingsystem.model.Account;
import com.bankingsystem.model.Transaction;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class TransactionDialogController {

    @FXML
    private Label titleLabel, recipientLabel;
    @FXML
    private TextField amountField, descriptionField, recipientField;
    @FXML
    private Button confirmButton;

    private String transactionType;
    private Account sourceAccount;
    private AccountDAO accountDAO;
    private TransactionDAO transactionDAO;

    public void initData(String type, Account account, AccountDAO accountDAO, TransactionDAO transactionDAO) {
        this.transactionType = type;
        this.sourceAccount = account;
        this.accountDAO = accountDAO;
        this.transactionDAO = transactionDAO;

        titleLabel.setText(type);
        confirmButton.setText("Confirm " + type);
        
        if ("Transfer".equals(type)) {
            recipientLabel.setVisible(true);
            recipientField.setVisible(true);
        }
    }

    @FXML
    private void handleConfirm() {
        String amountStr = amountField.getText();
        String description = descriptionField.getText();
        double amount;

        try {
            amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Amount must be positive.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a valid amount.");
            return;
        }

        switch (transactionType) {
            case "Deposit":
                handleDeposit(amount, description);
                break;
            case "Withdrawal":
                handleWithdrawal(amount, description);
                break;
            case "Transfer":
                handleTransfer(amount, description);
                break;
        }
    }

    private void handleDeposit(double amount, String description) {
        sourceAccount.setBalance(sourceAccount.getBalance() + amount);
        if (accountDAO.updateBalance(sourceAccount.getId(), sourceAccount.getBalance())) {
            transactionDAO.addTransaction(new Transaction(0, sourceAccount.getId(), "DEPOSIT", amount, null, description));
            showAlert(Alert.AlertType.INFORMATION, "Success", "Deposit successful.");
            closeDialog();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update balance.");
        }
    }

    private void handleWithdrawal(double amount, String description) {
        if (sourceAccount.getBalance() < amount) {
            showAlert(Alert.AlertType.ERROR, "Insufficient Funds", "You do not have enough funds for this withdrawal.");
            return;
        }
        sourceAccount.setBalance(sourceAccount.getBalance() - amount);
        if (accountDAO.updateBalance(sourceAccount.getId(), sourceAccount.getBalance())) {
            transactionDAO.addTransaction(new Transaction(0, sourceAccount.getId(), "WITHDRAWAL", amount, null, description));
            showAlert(Alert.AlertType.INFORMATION, "Success", "Withdrawal successful.");
            closeDialog();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update balance.");
        }
    }

    private void handleTransfer(double amount, String description) {
        if (sourceAccount.getBalance() < amount) {
            showAlert(Alert.AlertType.ERROR, "Insufficient Funds", "You do not have enough funds for this transfer.");
            return;
        }

        try {
            int recipientId = Integer.parseInt(recipientField.getText());
            Account recipientAccount = accountDAO.getAccountById(recipientId);

            if (recipientAccount == null) {
                showAlert(Alert.AlertType.ERROR, "Invalid Recipient", "Recipient account not found.");
                return;
            }

            // Perform the transfer
            sourceAccount.setBalance(sourceAccount.getBalance() - amount);
            recipientAccount.setBalance(recipientAccount.getBalance() + amount);

            if (accountDAO.updateBalance(sourceAccount.getId(), sourceAccount.getBalance()) && 
                accountDAO.updateBalance(recipientAccount.getId(), recipientAccount.getBalance())) {
                
                transactionDAO.addTransaction(new Transaction(0, sourceAccount.getId(), "TRANSFER_OUT", amount, null, description + " to " + recipientId));
                transactionDAO.addTransaction(new Transaction(0, recipientId, "TRANSFER_IN", amount, null, description + " from " + sourceAccount.getId()));
                
                showAlert(Alert.AlertType.INFORMATION, "Success", "Transfer successful.");
                closeDialog();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to complete transfer.");
            }

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a valid recipient account ID.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void closeDialog() {
        Stage stage = (Stage) confirmButton.getScene().getWindow();
        stage.close();
    }
}
