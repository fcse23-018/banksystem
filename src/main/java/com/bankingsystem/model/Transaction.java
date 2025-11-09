package com.bankingsystem.model;

import java.sql.Timestamp;

public class Transaction {

    public enum TransactionType {
        DEPOSIT,
        WITHDRAWAL,
        TRANSFER
    }

    private long transactionId;
    private long accountId;
    private TransactionType transactionType;
    private double amount;
    private Timestamp transactionDate;

    // Getters and Setters

    public long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Timestamp getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Timestamp transactionDate) {
        this.transactionDate = transactionDate;
    }
}
