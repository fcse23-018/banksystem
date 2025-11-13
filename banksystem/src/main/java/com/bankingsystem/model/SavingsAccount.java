package com.bankingsystem.model;

import java.util.UUID;

/**
 * Represents a savings account.
 * This account type accrues interest and does not allow withdrawals.
 */
public class SavingsAccount extends Account implements InterestBearing {

    /**
     * Constructs a new SavingsAccount.
     *
     * @param ownerId The ID of the account owner.
     */
    public SavingsAccount(UUID ownerId) {
        super(AccountType.SAVINGS, ownerId);
    }

    /**
     * This method is not supported for savings accounts.
     *
     * @param amount The amount to withdraw.
     * @throws UnsupportedOperationException Always throws this exception because withdrawals are not allowed.
     */
    @Override
    public void withdraw(double amount) throws InsufficientFundsException {
        throw new UnsupportedOperationException("Withdrawals are not allowed from a savings account.");
    }

    /**
     * Calculates and deposits the interest earned on the account.
     */
    @Override
    public void calculateInterest() {
        double interest = getBalance() * 0.0005; // 0.05% interest rate
        deposit(interest);
    }
}
