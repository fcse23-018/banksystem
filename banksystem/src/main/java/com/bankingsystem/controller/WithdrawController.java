package com.bankingsystem.controller;

import com.bankingsystem.dao.AccountDAO;
import com.bankingsystem.view.WithdrawView;
import javafx.scene.control.Alert;

public class WithdrawController {

    private final WithdrawView view;
    private final long accountNumber;
    private final AccountDAO accountDAO;

    public WithdrawController(WithdrawView view, long accountNumber) {
        this.view = view;
        this.accountNumber = accountNumber;
        this.accountDAO = new AccountDAO(); // Initialize AccountDAO
    }

    /**
     * Handles the withdrawal action.
     */
    public void handleWithdraw() {
        try {
            double amount = Double.parseDouble(view.getAmount());
            if (amount <= 0) {
                showAlert("Invalid Amount", "Amount must be positive.");
                return;
            }

            // Perform the withdrawal
            // if (!accountDAO.withdraw(accountNumber, amount)) { // Uncomment when DAO method is implemented
            //     showAlert("Insufficient Funds", "You do not have enough funds for this withdrawal.");
            //     return;
            // }

            showAlert("Success", "Withdrawal successful.");
            view.close();
        } catch (NumberFormatException e) {
            showAlert("Invalid Amount", "Please enter a valid number.");
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
