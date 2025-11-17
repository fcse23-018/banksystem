package bw.co.pulabank.controller;

import bw.co.pulabank.model.Account;
import bw.co.pulabank.model.TransactionType;
import bw.co.pulabank.service.AccountService;
import bw.co.pulabank.service.TransactionService;
import bw.co.pulabank.util.AlertHelper;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class TransferController {

    @FXML
    private TextField fromAccountField;

    @FXML
    private TextField toAccountField;

    @FXML
    private TextField amountField;

    @FXML
    private TextField referenceField;

    @FXML
    private Text statusText;

    private final TransactionService transactionService = new TransactionService();
    private final AccountService accountService = new AccountService();

    public void initialize() {
        // Controller initialization
    }

    @FXML
    private void handleTransfer() {
        if (!validateInput()) {
            return;
        }

        String fromAccount = fromAccountField.getText().trim();
        String toAccount = toAccountField.getText().trim();
        String amountStr = amountField.getText().trim();
        String reference = referenceField.getText().trim();

        try {
            double amount = Double.parseDouble(amountStr);

            if (!validateAmount(amount)) {
                return;
            }

            if (transactionService.transfer(fromAccount, toAccount, amount, reference)) {
                recordTransaction(fromAccount, toAccount, amount, reference);
                AlertHelper.showSuccess("Transfer Successful", 
                    "BWP " + String.format("%.2f", amount) + " transferred successfully.");
                clearForm();
            } else {
                statusText.setText("Transfer failed. Please try again.");
                AlertHelper.showError("Transfer Failed", "Unable to process transfer.");
            }
        } catch (NumberFormatException e) {
            statusText.setText("Invalid amount format.");
            AlertHelper.showError("Input Error", "Amount must be a valid number.");
        }
    }

    private boolean validateInput() {
        if (fromAccountField.getText().isEmpty() || 
            toAccountField.getText().isEmpty() || 
            amountField.getText().isEmpty()) {
            statusText.setText("All fields are required.");
            AlertHelper.showError("Validation Error", "Please fill all fields.");
            return false;
        }

        if (fromAccountField.getText().equals(toAccountField.getText())) {
            statusText.setText("Cannot transfer to the same account.");
            AlertHelper.showError("Validation Error", "Sender and receiver accounts must be different.");
            return false;
        }

        return true;
    }

    private boolean validateAmount(double amount) {
        if (amount <= 0) {
            statusText.setText("Amount must be greater than 0.");
            AlertHelper.showError("Validation Error", "Amount must be positive.");
            return false;
        }

        if (amount > 1000000) {
            statusText.setText("Amount exceeds maximum limit.");
            AlertHelper.showError("Validation Error", "Maximum transfer amount is BWP 1,000,000.");
            return false;
        }

        return true;
    }

    private void recordTransaction(String fromAccount, String toAccount, double amount, String reference) {
        Account fromAcc = accountService.getAccountByNumber(fromAccount);
        Account toAcc = accountService.getAccountByNumber(toAccount);

        if (fromAcc != null && toAcc != null) {
            transactionService.recordTransaction(fromAcc, toAcc, amount, reference, TransactionType.TRANSFER_OUT);
            transactionService.recordTransaction(toAcc, fromAcc, amount, reference, TransactionType.TRANSFER_IN);
        }
    }

    private void clearForm() {
        fromAccountField.clear();
        toAccountField.clear();
        amountField.clear();
        referenceField.clear();
        statusText.setText("Ready for next transfer.");
    }
}
