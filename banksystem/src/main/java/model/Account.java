package model;

import java.util.UUID;

public abstract class Account {
    private long accountNumber;
    private double balance;
    private AccountType accountType;
    private UUID ownerId;
    private AccountState state;

    public enum AccountType {
        SAVINGS, INVESTMENT, CHEQUE
    }

    public enum AccountState {
        NEW, ACTIVE, FROZEN, CLOSED
    }

    public Account(AccountType accountType, UUID ownerId) {
        this.accountType = accountType;
        this.ownerId = ownerId;
        this.state = AccountState.NEW;
        this.balance = 0.0;
    }

    public abstract void withdraw(double amount) throws InsufficientFundsException;

    public void deposit(double amount) {
        if (amount > 0) {
            this.balance += amount;
        }
    }

    // Getters and Setters

    public long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(UUID ownerId) {
        this.ownerId = ownerId;
    }

    public AccountState getState() {
        return state;
    }

    public void setState(AccountState state) {
        this.state = state;
    }
}
