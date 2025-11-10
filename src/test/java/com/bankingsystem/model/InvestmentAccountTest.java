package com.bankingsystem.model;

import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

public class InvestmentAccountTest {

    private final UUID ownerId = UUID.randomUUID();

    @Test
    public void testConstructor_sufficientInitialDeposit() throws InsufficientFundsException {
        InvestmentAccount investmentAccount = new InvestmentAccount(ownerId, 600.0);
        assertNotNull(investmentAccount);
        assertEquals(600.0, investmentAccount.getBalance());
    }

    @Test
    public void testConstructor_minimumInitialDeposit() throws InsufficientFundsException {
        InvestmentAccount investmentAccount = new InvestmentAccount(ownerId, 500.0);
        assertNotNull(investmentAccount);
        assertEquals(500.0, investmentAccount.getBalance());
    }

    @Test
    public void testConstructor_insufficientInitialDeposit() {
        InsufficientFundsException exception = assertThrows(InsufficientFundsException.class, () -> {
            new InvestmentAccount(ownerId, 400.0);
        });
        assertEquals("Minimum initial deposit for an investment account is 500.0", exception.getMessage());
    }

    @Test
    public void testWithdraw_sufficientFunds() throws InsufficientFundsException {
        InvestmentAccount investmentAccount = new InvestmentAccount(ownerId, 1000.0);
        investmentAccount.withdraw(300.0);
        assertEquals(700.0, investmentAccount.getBalance());
    }

    @Test
    public void testWithdraw_insufficientFunds() throws InsufficientFundsException {
        InvestmentAccount investmentAccount = new InvestmentAccount(ownerId, 800.0);
        InsufficientFundsException exception = assertThrows(InsufficientFundsException.class, () -> {
            investmentAccount.withdraw(900.0);
        });
        assertEquals("Insufficient funds for this withdrawal.", exception.getMessage());
        assertEquals(800.0, investmentAccount.getBalance());
    }

    @Test
    public void testCalculateInterest() throws InsufficientFundsException {
        InvestmentAccount investmentAccount = new InvestmentAccount(ownerId, 1000.0);
        investmentAccount.calculateInterest();
        assertEquals(1050.0, investmentAccount.getBalance());
    }
}
