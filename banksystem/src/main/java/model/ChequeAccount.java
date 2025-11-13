package model;

import java.util.UUID;

public class ChequeAccount extends Account {
    private String employerCompany;
    private String employerAddress;

    public ChequeAccount(UUID ownerId, String employerCompany, String employerAddress) {
        super(AccountType.CHEQUE, ownerId);
        this.employerCompany = employerCompany;
        this.employerAddress = employerAddress;
    }

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
