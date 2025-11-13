package com.bankingsystem.model;

import java.util.UUID;

/**
 * Represents a cheque account.
 * This account type allows withdrawals and is associated with an employer.
 */
public class ChequeAccount extends Account {
    private String employerCompany;
    private String employerAddress;

    /**
     * Constructs a new ChequeAccount.
     *
     * @param ownerId The ID of the account owner.
     * @param employerCompany The name of the employer company.
     * @param employerAddress The address of the employer.
     */
    public ChequeAccount(UUID ownerId, String employerCompany, String employerAddress) {
        super(AccountType.CHEQUE, ownerId);
        this.employerCompany = employerCompany;
        this.employerAddress = employerAddress;
    }

    /**
     * Withdraws a specified amount from the account.
     *
     * @param amount The amount to withdraw.
     * @throws InsufficientFundsException If the withdrawal amount is greater than the account balance.
     */
    @Override
    public void withdraw(double amount) throws InsufficientFundsException {
        if (getBalance() >= amount) {
            setBalance(getBalance() - amount);
        } else {
            throw new InsufficientFundsException("Insufficient funds for this withdrawal.");
        }
    }

    // Getters and Setters

    public String getEmployerCompany() {
        return employerCompany;
    }

    public void setEmployerCompany(String employerCompany) {
        this.employerCompany = employerCompany;
    }

    public String getEmployerAddress() {
        return employerAddress;
    }

    public void setEmployerAddress(String employerAddress) {
        this.employerAddress = employerAddress;
    }
}
