package com.bankingsystem.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ChequeAccountTest {

    private ChequeAccount chequeAccount;
    private final UUID ownerId = UUID.randomUUID();

    @BeforeEach
    public void setUp() {
        chequeAccount = new ChequeAccount(ownerId, "Test Company", "123 Test Address");
        chequeAccount.setAccountNumber(987654321);
    }

    @Test
    public void testDeposit() {
        chequeAccount.deposit(200.0);
        assertEquals(200.0, chequeAccount.getBalance());
    }

    @Test
    public void testDeposit_negativeAmount() {
        chequeAccount.deposit(-100.0);
        assertEquals(0.0, chequeAccount.getBalance());
    }

    @Test
    public void testWithdraw_sufficientFunds() throws InsufficientFundsException {
        chequeAccount.setBalance(500.0);
        chequeAccount.withdraw(250.0);
        assertEquals(250.0, chequeAccount.getBalance());
    }

    @Test
    public void testWithdraw_insufficientFunds() {
        chequeAccount.setBalance(100.0);
        InsufficientFundsException exception = assertThrows(InsufficientFundsException.class, () -> {
            chequeAccount.withdraw(200.0);
        });
        assertEquals("Insufficient funds for this withdrawal.", exception.getMessage());
        assertEquals(100.0, chequeAccount.getBalance());
    }

    @Test
    public void testWithdraw_exactBalance() throws InsufficientFundsException {
        chequeAccount.setBalance(300.0);
        chequeAccount.withdraw(300.0);
        assertEquals(0.0, chequeAccount.getBalance());
    }
}
