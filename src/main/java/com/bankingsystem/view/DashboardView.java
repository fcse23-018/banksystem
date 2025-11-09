package com.bankingsystem.view;

import com.bankingsystem.controller.DashboardController;
import com.bankingsystem.model.Account;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DashboardView extends VBox {

    private final DashboardController controller;
    private final TableView<Account> accountTable = new TableView<>();
    private final Stage stage;

    public DashboardView(Stage stage) {
        this.stage = stage;
        this.controller = new DashboardController(this, stage);

        setPadding(new Insets(20));
        setSpacing(20);

        Label title = new Label("Customer Dashboard");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        setupAccountTable();

        // Create buttons for actions
        Button depositButton = new Button("Deposit");
        depositButton.setOnAction(e -> handleDeposit());

        Button withdrawButton = new Button("Withdraw");
        withdrawButton.setOnAction(e -> handleWithdraw());

        Button transferButton = new Button("Transfer");
        transferButton.setOnAction(e -> handleTransfer());

        Button transactionsButton = new Button("View Transactions");
        transactionsButton.setOnAction(e -> handleViewTransactions());

        HBox buttons = new HBox(10, depositButton, withdrawButton, transferButton, transactionsButton);

        getChildren().addAll(title, accountTable, buttons);

        controller.loadAccounts();
    }

    private void setupAccountTable() {
        // Define columns for the account table
        TableColumn<Account, Long> accountNumberCol = new TableColumn<>("Account Number");
        accountNumberCol.setCellValueFactory(new PropertyValueFactory<>("accountNumber"));

        TableColumn<Account, Account.AccountType> accountTypeCol = new TableColumn<>("Account Type");
        accountTypeCol.setCellValueFactory(new PropertyValueFactory<>("accountType"));

        TableColumn<Account, Double> balanceCol = new TableColumn<>("Balance");
        balanceCol.setCellValueFactory(new PropertyValueFactory<>("balance"));

        accountTable.getColumns().addAll(accountNumberCol, accountTypeCol, balanceCol);
    }

    /**
     * Handles the deposit action. Opens the deposit view for the selected account.
     */
    private void handleDeposit() {
        Account selectedAccount = accountTable.getSelectionModel().getSelectedItem();
        if (selectedAccount != null) {
            DepositView depositView = new DepositView(stage, selectedAccount.getAccountNumber());
            depositView.showAndWait();
            controller.loadAccounts(); // Refresh table data
        } else {
            showAlert("No Account Selected", "Please select an account to make a deposit.");
        }
    }

    /**
     * Handles the withdrawal action. Opens the withdrawal view for the selected account.
     */
    private void handleWithdraw() {
        Account selectedAccount = accountTable.getSelectionModel().getSelectedItem();
        if (selectedAccount != null) {
            WithdrawView withdrawView = new WithdrawView(stage, selectedAccount.getAccountNumber());
            withdrawView.showAndWait();
            controller.loadAccounts(); // Refresh table data
        } else {
            showAlert("No Account Selected", "Please select an account to make a withdrawal.");
        }
    }

    /**
     * Handles the transfer action. Opens the transfer view for the selected account.
     */
    private void handleTransfer() {
        Account selectedAccount = accountTable.getSelectionModel().getSelectedItem();
        if (selectedAccount != null) {
            TransferView transferView = new TransferView(stage, selectedAccount.getAccountNumber());
            transferView.showAndWait();
            controller.loadAccounts(); // Refresh table data
        } else {
            showAlert("No Account Selected", "Please select an account to make a transfer.");
        }
    }

    /**
     * Handles the view transactions action. Opens the transaction view for the selected account.
     */
    private void handleViewTransactions() {
        Account selectedAccount = accountTable.getSelectionModel().getSelectedItem();
        if (selectedAccount != null) {
            TransactionView transactionView = new TransactionView(stage, selectedAccount.getAccountNumber());
            transactionView.show();
        } else {
            showAlert("No Account Selected", "Please select an account to view transactions.");
        }
    }

    /**
     * Shows an alert dialog.
     * @param title The title of the alert.
     * @param message The message to display.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public TableView<Account> getAccountTable() {
        return accountTable;
    }
}
