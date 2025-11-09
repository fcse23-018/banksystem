package com.bankingsystem.model;

import java.util.UUID;

public class InvestmentAccount extends Account implements InterestBearing {
    private static final double MIN_DEPOSIT = 500.00;

    public InvestmentAccount(UUID ownerId, double initialDeposit) throws InsufficientFundsException {
        super(AccountType.INVESTMENT, ownerId);
        if (initialDeposit < MIN_DEPOSIT) {
            throw new InsufficientFundsException("Minimum initial deposit for an investment account is " + MIN_DEPOSIT);
        }
        setBalance(initialDeposit);
    }

    @Override
    public void withdraw(double amount) throws InsufficientFundsException {
        if (getBalance() >= amount) {
            setBalance(getBalance() - amount);
        } else {
            throw new InsufficientFundsException("Insufficient funds for this withdrawal.");
        }
    }

    @Override
    public void calculateInterest() {
        double interest = getBalance() * 0.05;
        deposit(interest);
    }
}
