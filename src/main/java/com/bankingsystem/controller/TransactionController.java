package com.bankingsystem.controller;

import com.bankingsystem.dao.TransactionDAO;
import com.bankingsystem.model.Transaction;
import com.bankingsystem.view.TransactionView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class TransactionController {

    private final TransactionView view;
    private final long accountNumber;
    private final TransactionDAO transactionDAO;

    public TransactionController(TransactionView view, long accountNumber) {
        this.view = view;
        this.accountNumber = accountNumber;
        this.transactionDAO = new TransactionDAO();
    }

    /**
     * Loads the transactions for the account and displays them in the table.
     */
    public void loadTransactions() {
        List<Transaction> transactions = transactionDAO.getTransactionsForAccount(accountNumber);
        ObservableList<Transaction> observableTransactions = FXCollections.observableArrayList(transactions);
        view.getTransactionTable().setItems(observableTransactions);
    }
}
