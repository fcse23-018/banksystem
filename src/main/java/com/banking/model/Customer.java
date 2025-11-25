package com.banking.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a bank customer who can hold multiple accounts.
 * Demonstrates composition relationship (Customer has many Accounts).
 * 
 * @author Donovan Ntsima (FCSE23-018)
 * @version 1.0
 */
public class Customer {
    
    private String customerID;
    private String firstName;
    private String surname;
    private String address;
    private String password;
    private String pin;
    private List<Account> accounts;
    
    /**
     * Default constructor for Customer.
     * Initializes the accounts list.
     */
    public Customer() {
        this.accounts = new ArrayList<>();
    }
    
    /**
     * Parameterized constructor for Customer.
     * 
     * @param customerID the unique customer ID
     * @param firstName the customer's first name
     * @param surname the customer's surname
     * @param address the customer's address
     * @param password the customer's hashed password
     */
    public Customer(String customerID, String firstName, String surname, 
                   String address, String password) {
        this.customerID = customerID;
        this.firstName = firstName;
        this.surname = surname;
        this.address = address;
        this.password = password;
        this.pin = "1234";
        this.accounts = new ArrayList<>();
    }
    
    /**
     * Full parameterized constructor for Customer including PIN.
     * 
     * @param customerID the unique customer ID
     * @param firstName the customer's first name
     * @param surname the customer's surname
     * @param address the customer's address
     * @param password the customer's hashed password
     * @param pin the customer's 4-digit PIN
     */
    public Customer(String customerID, String firstName, String surname, 
                   String address, String password, String pin) {
        this.customerID = customerID;
        this.firstName = firstName;
        this.surname = surname;
        this.address = address;
        this.password = password;
        this.pin = pin;
        this.accounts = new ArrayList<>();
    }
    
    /**
     * Gets the customer ID.
     * 
     * @return the customer ID
     */
    public String getCustomerID() {
        return customerID;
    }
    
    /**
     * Sets the customer ID.
     * 
     * @param customerID the customer ID to set
     */
    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }
    
    /**
     * Gets the first name.
     * 
     * @return the first name
     */
    public String getFirstName() {
        return firstName;
    }
    
    /**
     * Sets the first name.
     * 
     * @param firstName the first name to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    /**
     * Gets the surname.
     * 
     * @return the surname
     */
    public String getSurname() {
        return surname;
    }
    
    /**
     * Sets the surname.
     * 
     * @param surname the surname to set
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }
    
    /**
     * Gets the full name of the customer.
     * 
     * @return the full name (first name + surname)
     */
    public String getFullName() {
        return firstName + " " + surname;
    }
    
    /**
     * Gets the address.
     * 
     * @return the address
     */
    public String getAddress() {
        return address;
    }
    
    /**
     * Sets the address.
     * 
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }
    
    /**
     * Gets the password (hashed).
     * 
     * @return the hashed password
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * Sets the password (should be hashed before setting).
     * 
     * @param password the hashed password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    /**
     * Gets the PIN.
     * 
     * @return the 4-digit PIN
     */
    public String getPin() {
        return pin;
    }
    
    /**
     * Sets the PIN.
     * 
     * @param pin the 4-digit PIN to set
     */
    public void setPin(String pin) {
        this.pin = pin;
    }
    
    /**
     * Validates the provided PIN against the customer's PIN.
     * 
     * @param inputPin the PIN to validate
     * @return true if PIN matches, false otherwise
     */
    public boolean validatePin(String inputPin) {
        return this.pin != null && this.pin.equals(inputPin);
    }
    
    /**
     * Gets the list of accounts owned by this customer.
     * 
     * @return the list of accounts
     */
    public List<Account> getAccounts() {
        return accounts;
    }
    
    /**
     * Sets the list of accounts.
     * 
     * @param accounts the accounts list to set
     */
    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }
    
    /**
     * Updates the customer's profile information.
     * 
     * @param address the new address
     */
    public void updateProfile(String address) {
        this.address = address;
    }
    
    /**
     * Opens a new Savings or Investment account.
     * Demonstrates method overloading.
     * 
     * @param accountType the type of account ("SAVINGS" or "INVESTMENT")
     * @param initialDeposit the initial deposit amount
     * @return the newly created account
     * @throws IllegalArgumentException if account type is invalid or deposit requirements not met
     */
    public Account openAccount(String accountType, double initialDeposit) {
        String accountNumber = generateAccountNumber();
        Account account;
        
        switch (accountType.toUpperCase()) {
            case "SAVINGS":
                account = new SavingsAccount(accountNumber, initialDeposit, "Main Branch", this);
                break;
            case "INVESTMENT":
                if (initialDeposit < InvestmentAccount.getMinInitialDeposit()) {
                    throw new IllegalArgumentException(
                        String.format("Investment account requires minimum BWP %.2f", 
                        InvestmentAccount.getMinInitialDeposit())
                    );
                }
                account = new InvestmentAccount(accountNumber, initialDeposit, "Main Branch", this);
                break;
            default:
                throw new IllegalArgumentException("Invalid account type: " + accountType);
        }
        
        accounts.add(account);
        return account;
    }
    
    /**
     * Opens a new Cheque account with employer information.
     * Demonstrates method overloading.
     * 
     * @param accountType the type of account (must be "CHEQUE")
     * @param initialDeposit the initial deposit amount
     * @param employerInfo employer details in format "company|address"
     * @return the newly created cheque account
     * @throws IllegalArgumentException if account type is not CHEQUE or employer info is invalid
     */
    public Account openAccount(String accountType, double initialDeposit, String employerInfo) {
        if (!"CHEQUE".equalsIgnoreCase(accountType)) {
            throw new IllegalArgumentException("This method is only for opening Cheque accounts");
        }
        
        String[] parts = employerInfo.split("\\|");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Employer info must be in format: company|address");
        }
        
        String accountNumber = generateAccountNumber();
        ChequeAccount account = new ChequeAccount(
            accountNumber, initialDeposit, "Main Branch", this, parts[0], parts[1]
        );
        
        accounts.add(account);
        return account;
    }
    
    /**
     * Generates a unique account number.
     * Format: ACC + timestamp + random 3 digits
     * 
     * @return the generated account number
     */
    private String generateAccountNumber() {
        long timestamp = System.currentTimeMillis() % 1000000;
        int random = (int) (Math.random() * 900) + 100;
        return String.format("ACC%d%d", timestamp, random);
    }
    
    @Override
    public String toString() {
        return String.format("%s - %s (%d accounts)", 
            customerID, getFullName(), accounts.size());
    }
}
