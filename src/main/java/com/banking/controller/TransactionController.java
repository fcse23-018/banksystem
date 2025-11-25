package com.banking.controller;

import com.banking.dao.TransactionDAO;
import com.banking.model.Account;
import com.banking.model.Transaction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Controller for the Transaction History View.
 * Displays transaction history for a selected account.
 * 
 * @author Donovan Ntsima (FCSE23-018)
 * @version 1.0
 */
public class TransactionController {
    
    @FXML
    private TableView<TransactionDisplay> transactionTable;
    
    @FXML
    private TableColumn<TransactionDisplay, String> dateColumn;
    
    @FXML
    private TableColumn<TransactionDisplay, String> typeColumn;
    
    @FXML
    private TableColumn<TransactionDisplay, String> amountColumn;
    
    @FXML
    private TableColumn<TransactionDisplay, String> balanceAfterColumn;
    
    private Account account;
    private TransactionDAO transactionDAO;
    
    /**
     * Initializes the controller.
     * Called automatically after FXML elements are loaded.
     */
    @FXML
    public void initialize() {
        transactionDAO = new TransactionDAO();
        
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        balanceAfterColumn.setCellValueFactory(new PropertyValueFactory<>("balanceAfter"));
    }
    
    /**
     * Sets the account and loads its transaction history.
     * 
     * @param account the account to display transactions for
     */
    public void setAccount(Account account) {
        this.account = account;
        loadTransactions();
    }
    
    /**
     * Loads all transactions for the current account.
     */
    private void loadTransactions() {
        List<Transaction> transactions = transactionDAO.getTransactionsByAccount(
            account.getAccountNumber(), account
        );
        
        ObservableList<TransactionDisplay> displayList = FXCollections.observableArrayList();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        for (Transaction transaction : transactions) {
            displayList.add(new TransactionDisplay(
                dateFormat.format(transaction.getDate()),
                transaction.getType(),
                String.format("BWP %.2f", transaction.getAmount()),
                String.format("BWP %.2f", transaction.getBalanceAfter())
            ));
        }
        
        transactionTable.setItems(displayList);
    }
    
    /**
     * Inner class for displaying transactions in the table.
     * JavaFX requires public properties or getters for TableView binding.
     */
    public static class TransactionDisplay {
        private final String date;
        private final String type;
        private final String amount;
        private final String balanceAfter;
        
        public TransactionDisplay(String date, String type, String amount, String balanceAfter) {
            this.date = date;
            this.type = type;
            this.amount = amount;
            this.balanceAfter = balanceAfter;
        }
        
        public String getDate() {
            return date;
        }
        
        public String getType() {
            return type;
        }
        
        public String getAmount() {
            return amount;
        }
        
        public String getBalanceAfter() {
            return balanceAfter;
        }
    }
}
