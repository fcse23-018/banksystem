package com.banking.model;

/**
 * Savings account that earns 0.05% monthly interest.
 * Withdrawals are NOT allowed from this account type.
 * Implements InterestBearing interface for interest calculation.
 * 
 * @author Donovan Ntsima (FCSE23-018)
 * @version 1.0
 */
public class SavingsAccount extends Account implements InterestBearing {
    
    private static final double INTEREST_RATE = 0.0005; // 0.05% monthly
    
    /**
     * Default constructor for SavingsAccount.
     */
    public SavingsAccount() {
        super();
    }
    
    /**
     * Parameterized constructor for SavingsAccount.
     * 
     * @param accountNumber the unique account number
     * @param balance the initial balance
     * @param branch the branch where account was opened
     * @param customer the customer who owns this account
     */
    public SavingsAccount(String accountNumber, double balance, String branch, Customer customer) {
        super(accountNumber, balance, branch, customer);
    }
    
    /**
     * Withdrawals are NOT allowed for Savings accounts.
     * Overrides the abstract withdraw method to enforce this rule.
     * 
     * @param amount the amount to withdraw (ignored)
     * @throws UnsupportedOperationException always thrown as withdrawals are not permitted
     */
    @Override
    public void withdraw(double amount) {
        throw new UnsupportedOperationException(
            "Withdrawals are not allowed from Savings accounts. " +
            "Please transfer funds to another account type if you need to make withdrawals."
        );
    }
    
    /**
     * Calculates monthly interest at 0.05% rate.
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
     * @return "Savings Account"
     */
    @Override
    public String getAccountType() {
        return "Savings Account";
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
