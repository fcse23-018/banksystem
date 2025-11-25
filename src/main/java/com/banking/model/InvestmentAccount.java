package com.banking.model;

/**
 * Investment account that earns 5% monthly interest.
 * Requires a minimum initial deposit of BWP 500.00.
 * Allows both deposits and withdrawals.
 * Implements InterestBearing interface for interest calculation.
 * 
 * @author Donovan Ntsima (FCSE23-018)
 * @version 1.0
 */
public class InvestmentAccount extends Account implements InterestBearing {
    
    private static final double INTEREST_RATE = 0.05; // 5% monthly
    private static final double MIN_INITIAL_DEPOSIT = 500.0;
    
    /**
     * Default constructor for InvestmentAccount.
     */
    public InvestmentAccount() {
        super();
    }
    
    /**
     * Parameterized constructor for InvestmentAccount.
     * 
     * @param accountNumber the unique account number
     * @param balance the initial balance (must be at least BWP 500)
     * @param branch the branch where account was opened
     * @param customer the customer who owns this account
     * @throws IllegalArgumentException if initial balance is less than BWP 500
     */
    public InvestmentAccount(String accountNumber, double balance, String branch, Customer customer) {
        super(accountNumber, balance, branch, customer);
        if (balance < MIN_INITIAL_DEPOSIT) {
            throw new IllegalArgumentException(
                String.format("Investment account requires minimum initial deposit of BWP %.2f", 
                MIN_INITIAL_DEPOSIT)
            );
        }
    }
    
    /**
     * Withdraws funds from the investment account.
     * Ensures sufficient balance before withdrawal.
     * 
     * @param amount the amount to withdraw
     * @throws IllegalArgumentException if amount is negative, zero, or exceeds balance
     */
    @Override
    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        if (amount > getBalance()) {
            throw new IllegalArgumentException(
                String.format("Insufficient funds. Available balance: BWP %.2f", getBalance())
            );
        }
        setBalance(getBalance() - amount);
    }
    
    /**
     * Calculates monthly interest at 5% rate.
     * Implements the InterestBearing interface contract.
     * 
     * @return the calculated interest amount
     */
    @Override
    public double calculateInterest() {
        return getBalance() * INTEREST_RATE;
    }
    
    /**
     * Gets the account type name.
     * 
     * @return "Investment Account"
     */
    @Override
    public String getAccountType() {
        return "Investment Account";
    }
    
    /**
     * Gets the minimum initial deposit requirement.
     * 
     * @return the minimum initial deposit amount
     */
    public static double getMinInitialDeposit() {
        return MIN_INITIAL_DEPOSIT;
    }
    
    /**
     * Gets the interest rate for display purposes.
     * 
     * @return the interest rate as a percentage
     */
    public double getInterestRatePercentage() {
        return INTEREST_RATE * 100;
    }
}
