package com.banking.model;

/**
 * Interface for accounts that earn interest.
 * Implements polymorphism for interest calculation across different account types.
 * 
 * @author Donovan Ntsima (FCSE23-018)
 * @version 1.0
 */
public interface InterestBearing {
    
    /**
     * Calculates the monthly interest for the account.
     * Different account types implement different interest rates:
     * - SavingsAccount: 0.05% monthly
     * - InvestmentAccount: 5% monthly
     * 
     * @return the calculated interest amount
     */
    double calculateInterest();
}
