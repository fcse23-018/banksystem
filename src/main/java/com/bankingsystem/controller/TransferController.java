package com.bankingsystem.controller;

import com.bankingsystem.dao.AccountDAO;
import com.bankingsystem.view.TransferView;
import javafx.scene.control.Alert;

public class TransferController {

    private final TransferView view;
    private final long sourceAccountNumber;
    private final AccountDAO accountDAO;

    public TransferController(TransferView view, long sourceAccountNumber) {
        this.view = view;
        this.sourceAccountNumber = sourceAccountNumber;
        this.accountDAO = new AccountDAO(); // Initialize AccountDAO
    }

    /**
     * Handles the transfer action.
     */
    public void handleTransfer() {
        try {
            long destinationAccountNumber = Long.parseLong(view.getDestinationAccountNumber());
            double amount = Double.parseDouble(view.getAmount());

            if (amount <= 0) {
                showAlert("Invalid Amount", "Amount must be positive.");
                return;
            }

            // Perform the transfer
            // if (!accountDAO.transfer(sourceAccountNumber, destinationAccountNumber, amount)) { // Uncomment when DAO method is implemented
            //     showAlert("Transfer Failed", "Check account details and balance.");
            //     return;
            // }

            showAlert("Success", "Transfer successful.");
            view.close();
        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter valid account number and amount.");
        }
    }

    /**
     * Shows an alert dialog.
     * @param title The title of the alert.
     * @param message The message to display.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
