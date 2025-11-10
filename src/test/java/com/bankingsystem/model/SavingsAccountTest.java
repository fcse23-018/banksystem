package com.bankingsystem.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SavingsAccountTest {

    private SavingsAccount savingsAccount;
    private final UUID ownerId = UUID.randomUUID();

    @BeforeEach
    public void setUp() {
        savingsAccount = new SavingsAccount(ownerId);
        savingsAccount.setAccountNumber(123456789);
    }

    @Test
    public void testDeposit() {
        savingsAccount.deposit(100.0);
        assertEquals(100.0, savingsAccount.getBalance());
    }

    @Test
    public void testDeposit_negativeAmount() {
        savingsAccount.deposit(-50.0);
        assertEquals(0.0, savingsAccount.getBalance());
    }

    @Test
    public void testCalculateInterest() {
        savingsAccount.setBalance(1000.0);
        savingsAccount.calculateInterest();
        assertEquals(1000.5, savingsAccount.getBalance());
    }

    @Test
    public void testCalculateInterest_zeroBalance() {
        savingsAccount.setBalance(0.0);
        savingsAccount.calculateInterest();
        assertEquals(0.0, savingsAccount.getBalance());
    }

    @Test
    public void testWithdraw() {
        try {
            savingsAccount.withdraw(100.0);
        } catch (UnsupportedOperationException e) {
            assertEquals("Withdrawals are not allowed from a savings account.", e.getMessage());
        } catch (InsufficientFundsException e) {
            // This is expected to not be thrown, but needs to be here for the compiler
        }
    }
}
