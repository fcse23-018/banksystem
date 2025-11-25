package com.banking.model;

import java.util.Date;

/**
 * Represents a financial transaction on an account.
 * Tracks all deposits, withdrawals, and other account activities.
 * 
 * @author Donovan Ntsima (FCSE23-018)
 * @version 1.0
 */
public class Transaction {
    
    private String transactionID;
    private double amount;
    private Date date;
    private String type; // "DEPOSIT", "WITHDRAWAL", "INTEREST"
    private Account account;
    private double balanceAfter;
    
    /**
     * Default constructor for Transaction.
     */
    public Transaction() {
        this.date = new Date();
    }
    
    /**
     * Parameterized constructor for Transaction.
     * 
     * @param transactionID the unique transaction ID
     * @param amount the transaction amount
     * @param type the transaction type (DEPOSIT, WITHDRAWAL, INTEREST)
     * @param account the account involved in the transaction
     * @param balanceAfter the account balance after the transaction
     */
    public Transaction(String transactionID, double amount, String type, 
                      Account account, double balanceAfter) {
        this.transactionID = transactionID;
        this.amount = amount;
        this.date = new Date();
        this.type = type;
        this.account = account;
        this.balanceAfter = balanceAfter;
    }
    
    /**
     * Gets the transaction ID.
     * 
     * @return the transaction ID
     */
    public String getTransactionID() {
        return transactionID;
    }
    
    /**
     * Sets the transaction ID.
     * 
     * @param transactionID the transaction ID to set
     */
    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }
    
    /**
     * Gets the transaction amount.
     * 
     * @return the amount
     */
    public double getAmount() {
        return amount;
    }
    
    /**
     * Sets the transaction amount.
     * 
     * @param amount the amount to set
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    /**
     * Gets the transaction date.
     * 
     * @return the date
     */
    public Date getDate() {
        return date;
    }
    
    /**
     * Sets the transaction date.
     * 
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }
    
    /**
     * Gets the transaction type.
     * 
     * @return the type (DEPOSIT, WITHDRAWAL, INTEREST)
     */
    public String getType() {
        return type;
    }
    
    /**
     * Sets the transaction type.
     * 
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }
    
    /**
     * Gets the account associated with this transaction.
     * 
     * @return the account
     */
    public Account getAccount() {
        return account;
    }
    
    /**
     * Sets the account associated with this transaction.
     * 
     * @param account the account to set
     */
    public void setAccount(Account account) {
        this.account = account;
    }
    
    /**
     * Gets the balance after the transaction.
     * 
     * @return the balance after transaction
     */
    public double getBalanceAfter() {
        return balanceAfter;
    }
    
    /**
     * Sets the balance after the transaction.
     * 
     * @param balanceAfter the balance to set
     */
    public void setBalanceAfter(double balanceAfter) {
        this.balanceAfter = balanceAfter;
    }
    
    /**
     * Logs the transaction (placeholder for future logging implementation).
     */
    public void logTransaction() {
        System.out.printf("Transaction logged: %s - %s BWP %.2f%n", 
            transactionID, type, amount);
    }
    
    /**
     * Gets the transaction details as a formatted string.
     * 
     * @return formatted transaction details
     */
    public String getDetails() {
        return String.format("%s | %s | BWP %.2f | Balance: BWP %.2f | %s",
            transactionID, type, amount, balanceAfter, date.toString());
    }
    
    @Override
    public String toString() {
        return getDetails();
    }
}
