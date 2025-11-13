package com.bankingsystem.controller;

import com.bankingsystem.dao.AccountDAO;
import com.bankingsystem.dao.CustomerDAO;
import com.bankingsystem.dao.TransactionDAO;
import com.bankingsystem.model.Account;
import com.bankingsystem.model.Customer;
import com.bankingsystem.model.Transaction;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class DashboardController {

    @FXML
    private Label welcomeLabel;
    @FXML
    private TextField searchBar;
    @FXML
    private ListView<Customer> searchResults;
    @FXML
    private TabPane customerDetailsPane;
    @FXML
    private Tab profileTab;
    @FXML
    private TableView<Account> accountsTable;
    @FXML
    private TableView<Transaction> transactionsTable;
    @FXML
    private Button openAccountButton, depositButton, withdrawButton, transferButton, approveSuspendButton, closeAccountButton;

    private CustomerDAO customerDAO;
    private AccountDAO accountDAO;
    private TransactionDAO transactionDAO;

    private Customer loggedInUser;
    private Customer selectedCustomer;
    private boolean isStaff;

    public void initData(String idNumber) {
        customerDAO = new CustomerDAO();
        accountDAO = new AccountDAO();
        transactionDAO = new TransactionDAO();

        if ("admin".equals(idNumber)) {
            isStaff = true;
            welcomeLabel.setText("Welcome, Admin!");
            searchBar.setDisable(false);
        } else {
            isStaff = false;
            loggedInUser = customerDAO.getCustomerByIdNumber(idNumber);
            welcomeLabel.setText("Welcome, " + loggedInUser.getFirstName() + "!");
            searchBar.setDisable(true);
            loadCustomerData(loggedInUser);
        }

        configureControls();
        setupAccountTable();
        setupTransactionsTable();
    }

    private void configureControls() {
        openAccountButton.setManaged(isStaff);
        openAccountButton.setVisible(isStaff);
        approveSuspendButton.setManaged(isStaff);
        approveSuspendButton.setVisible(isStaff);
        closeAccountButton.setManaged(isStaff);
        closeAccountButton.setVisible(isStaff);
    }

    private void setupAccountTable() {
        TableColumn<Account, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Account, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("accountType"));

        TableColumn<Account, Double> balanceCol = new TableColumn<>("Balance");
        balanceCol.setCellValueFactory(new PropertyValueFactory<>("balance"));

        TableColumn<Account, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        accountsTable.getColumns().addAll(idCol, typeCol, balanceCol, statusCol);
    }

    private void setupTransactionsTable() {
        TableColumn<Transaction, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Transaction, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<Transaction, Double> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));

        TableColumn<Transaction, String> timestampCol = new TableColumn<>("Timestamp");
        timestampCol.setCellValueFactory(new PropertyValueFactory<>("timestamp"));

        transactionsTable.getColumns().addAll(idCol, typeCol, amountCol, timestampCol);
    }

    private void loadCustomerData(Customer customer) {
        this.selectedCustomer = customer;
        loadProfileTab(customer);
        loadAccountsTab(customer.getId());
    }

    private void loadProfileTab(Customer customer) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/bankingsystem/view/ProfileView.fxml"));
            Node profileView = loader.load();
            ProfileViewController controller = loader.getController();
            controller.initialize(customer, isStaff);
            profileTab.setContent(profileView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadAccountsTab(int customerId) {
        List<Account> accounts = accountDAO.getAccountsForCustomer(customerId);
        accountsTable.setItems(FXCollections.observableArrayList(accounts));
        accountsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                loadTransactionsTab(newSelection.getId());
            }
        });
    }

    private void loadTransactionsTab(int accountId) {
        List<Transaction> transactions = transactionDAO.getTransactionsForAccount(accountId);
        transactionsTable.setItems(FXCollections.observableArrayList(transactions));
    }

    @FXML
    private void handleSearch(KeyEvent event) {
        String query = searchBar.getText().trim();
        if (query.length() > 1 && isStaff) {
            List<Customer> customers = customerDAO.searchCustomersByName(query);
            searchResults.setItems(FXCollections.observableArrayList(customers));
        }
    }

    @FXML
    private void handleCustomerSelection(MouseEvent event) {
        Customer selected = searchResults.getSelectionModel().getSelectedItem();
        if (selected != null) {
            loadCustomerData(selected);
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/com/bankingsystem/view/Login.fxml"));
        stage.setScene(new Scene(root, 400, 400));
        stage.setTitle("Banking System Login");
    }

    @FXML
    private void handleQuit() {
        System.exit(0);
    }
    
    @FXML
    private void handleOpenAccount() {
        if (!isStaff) return;
        // Dialog to open new account
    }
    
    @FXML
    private void handleApproveSuspendAccount() {
        if (!isStaff) return;
        Account selected = accountsTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            // Logic to approve/suspend
        }
    }
    
    @FXML
    private void handleCloseAccount() {
        if (!isStaff) return;
        Account selected = accountsTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            // Logic to close account
        }
    }

    @FXML
    private void handleDeposit() {
        Account selected = accountsTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
           showTransactionDialog("Deposit", selected);
        }
    }

    @FXML
    private void handleWithdraw() {
        Account selected = accountsTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            showTransactionDialog("Withdrawal", selected);
        }
    }

    @FXML
    private void handleTransfer() {
        Account selected = accountsTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            showTransactionDialog("Transfer", selected);
        }
    }

    private void showTransactionDialog(String type, Account account) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/bankingsystem/view/TransactionDialog.fxml"));
            Parent root = loader.load();

            TransactionDialogController controller = loader.getController();
            controller.initData(type, account, accountDAO, transactionDAO);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(type);
            stage.setScene(new Scene(root));
            stage.showAndWait();

            // Refresh account data
            loadAccountsTab(selectedCustomer.getId());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
