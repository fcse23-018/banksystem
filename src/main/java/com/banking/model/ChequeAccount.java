package com.banking.model;

/**
 * Cheque account used for salary payments and regular transactions.
 * Requires employment information (employer company and address).
 * Allows both deposits and withdrawals.
 * Does NOT earn interest.
 * 
 * @author Donovan Ntsima (FCSE23-018)
 * @version 1.0
 */
public class ChequeAccount extends Account {
    
    private String employerCompany;
    private String employerAddress;
    
    /**
     * Default constructor for ChequeAccount.
     */
    public ChequeAccount() {
        super();
    }
    
    /**
     * Parameterized constructor for ChequeAccount.
     * 
     * @param accountNumber the unique account number
     * @param balance the initial balance
     * @param branch the branch where account was opened
     * @param customer the customer who owns this account
     * @param employerCompany the name of the employer company
     * @param employerAddress the address of the employer
     */
    public ChequeAccount(String accountNumber, double balance, String branch, 
                        Customer customer, String employerCompany, String employerAddress) {
        super(accountNumber, balance, branch, customer);
        this.employerCompany = employerCompany;
        this.employerAddress = employerAddress;
    }
    
    /**
     * Gets the employer company name.
     * 
     * @return the employer company
     */
    public String getEmployerCompany() {
        return employerCompany;
    }
    
    /**
     * Sets the employer company name.
     * 
     * @param employerCompany the employer company to set
     */
    public void setEmployerCompany(String employerCompany) {
        this.employerCompany = employerCompany;
    }
    
    /**
     * Gets the employer address.
     * 
     * @return the employer address
     */
    public String getEmployerAddress() {
        return employerAddress;
    }
    
    /**
     * Sets the employer address.
     * 
     * @param employerAddress the employer address to set
     */
    public void setEmployerAddress(String employerAddress) {
        this.employerAddress = employerAddress;
    }
    
    /**
     * Sets both employer company and address.
     * Convenience method for updating employer information.
     * 
     * @param company the employer company name
     * @param address the employer address
     */
    public void setEmployerInfo(String company, String address) {
        this.employerCompany = company;
        this.employerAddress = address;
    }
    
    /**
     * Withdraws funds from the cheque account.
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
     * Gets the account type name.
     * 
     * @return "Cheque Account"
     */
    @Override
    public String getAccountType() {
        return "Cheque Account";
    }
}
