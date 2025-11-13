package model;

import java.util.UUID;

public class SavingsAccount extends Account implements InterestBearing {

    public SavingsAccount(UUID ownerId) {
        super(AccountType.SAVINGS, ownerId);
    }

    @Override
    public void withdraw(double amount) throws InsufficientFundsException {
        throw new UnsupportedOperationException("Withdrawals are not allowed from a savings account.");
    }

    @Override
    public void calculateInterest() {
        double interest = getBalance() * 0.0005;
        deposit(interest);
    }
}
