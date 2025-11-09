package com.bankingsystem.view;

import com.bankingsystem.controller.TransactionController;
import com.bankingsystem.model.Transaction;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.Timestamp;

public class TransactionView extends Stage {

    private final TransactionController controller;
    private final TableView<Transaction> transactionTable = new TableView<>();

    public TransactionView(Stage owner, long accountNumber) {
        this.controller = new TransactionController(this, accountNumber);

        initOwner(owner);
        initModality(Modality.APPLICATION_MODAL);
        setTitle("Transaction History");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        Label title = new Label("Transaction History for Account: " + accountNumber);
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        setupTransactionTable();

        layout.getChildren().addAll(title, transactionTable);

        setScene(new Scene(layout, 600, 400));

        controller.loadTransactions();
    }

    private void setupTransactionTable() {
        TableColumn<Transaction, Long> transactionIdCol = new TableColumn<>("Transaction ID");
        transactionIdCol.setCellValueFactory(new PropertyValueFactory<>("transactionId"));

        TableColumn<Transaction, Transaction.TransactionType> transactionTypeCol = new TableColumn<>("Type");
        transactionTypeCol.setCellValueFactory(new PropertyValueFactory<>("transactionType"));

        TableColumn<Transaction, Double> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));

        TableColumn<Transaction, Timestamp> transactionDateCol = new TableColumn<>("Date");
        transactionDateCol.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));

        transactionTable.getColumns().addAll(transactionIdCol, transactionTypeCol, amountCol, transactionDateCol);
    }

    public TableView<Transaction> getTransactionTable() {
        return transactionTable;
    }
}
