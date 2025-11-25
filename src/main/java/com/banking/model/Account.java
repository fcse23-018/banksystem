package com.banking.model;

/**
 * Abstract base class for all bank accounts.
 * Demonstrates abstraction and inheritance principles.
 * 
 * @author Donovan Ntsima (FCSE23-018)
 * @version 1.0
 */
public abstract class Account {
    
    private String accountNumber;
    private double balance;
    private String branch;
    private Customer customer;
    
    /**
     * Default constructor for Account.
     */
    public Account() {
    }
    
    /**
     * Parameterized constructor for Account.
     * 
     * @param accountNumber the unique account number
     * @param balance the initial balance
     * @param branch the branch where account was opened
     * @param customer the customer who owns this account
     */
    public Account(String accountNumber, double balance, String branch, Customer customer) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.branch = branch;
        this.customer = customer;
    }
    
    /**
     * Gets the account number.
     * 
     * @return the account number
     */
    public String getAccountNumber() {
        return accountNumber;
    }
    
    /**
     * Sets the account number.
     * 
     * @param accountNumber the account number to set
     */
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
    
    /**
     * Gets the current balance.
     * 
     * @return the account balance
     */
    public double getBalance() {
        return balance;
    }
    
    /**
     * Sets the account balance.
     * 
     * @param balance the balance to set
     */
    public void setBalance(double balance) {
        this.balance = balance;
    }
    
    /**
     * Gets the branch name.
     * 
     * @return the branch name
     */
    public String getBranch() {
        return branch;
    }
    
    /**
     * Sets the branch name.
     * 
     * @param branch the branch to set
     */
    public void setBranch(String branch) {
        this.branch = branch;
    }
    
    /**
     * Gets the customer who owns this account.
     * 
     * @return the customer
     */
    public Customer getCustomer() {
        return customer;
    }
    
    /**
     * Sets the customer who owns this account.
     * 
     * @param customer the customer to set
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    /**
     * Deposits funds into the account.
     * Updates balance in real-time.
     * 
     * @param amount the amount to deposit
     * @throws IllegalArgumentException if amount is negative or zero
     */
    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        this.balance += amount;
    }
    
    /**
     * Abstract method for withdrawing funds.
     * Each account type implements its own withdrawal rules.
     * 
     * @param amount the amount to withdraw
     * @throws IllegalArgumentException if withdrawal is not allowed or invalid
     */
    public abstract void withdraw(double amount);
    
    /**
     * Gets the account type name.
     * Used for polymorphic behavior and display purposes.
     * 
     * @return the account type as a string
     */
    public abstract String getAccountType();
    
    @Override
    public String toString() {
        return String.format("%s - %s (Balance: BWP %.2f)", 
            accountNumber, getAccountType(), balance);
    }
}
