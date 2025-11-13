package com.bankingsystem.model;

import java.util.UUID;

/**
 * Represents an investment account.
 * This account type requires a minimum initial deposit and accrues interest.
 */
public class InvestmentAccount extends Account implements InterestBearing {
    // The minimum deposit required to open an investment account.
    private static final double MIN_DEPOSIT = 500.00;

    /**
     * Constructs a new InvestmentAccount.
     *
     * @param ownerId The ID of the account owner.
     * @param initialDeposit The initial deposit amount.
     * @throws InsufficientFundsException If the initial deposit is less than the minimum required.
     */
    public InvestmentAccount(UUID ownerId, double initialDeposit) throws InsufficientFundsException {
        super(AccountType.INVESTMENT, ownerId);
        if (initialDeposit < MIN_DEPOSIT) {
            throw new InsufficientFundsException("Minimum initial deposit for an investment account is " + MIN_DEPOSIT);
        }
        setBalance(initialDeposit);
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

    /**
     * Calculates and deposits the interest earned on the account.
     */
    @Override
    public void calculateInterest() {
        double interest = getBalance() * 0.05; // 5% interest rate
        deposit(interest);
    }
}
