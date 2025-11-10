package com.bankingsystem.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransactionTest {

    private Transaction transaction;

    @BeforeEach
    public void setUp() {
        transaction = new Transaction();
    }

    @Test
    public void testSettersAndGetters() {
        long transactionId = 12345L;
        long accountId = 67890L;
        Transaction.TransactionType transactionType = Transaction.TransactionType.DEPOSIT;
        double amount = 500.0;
        Timestamp transactionDate = new Timestamp(System.currentTimeMillis());

        transaction.setTransactionId(transactionId);
        transaction.setAccountId(accountId);
        transaction.setTransactionType(transactionType);
        transaction.setAmount(amount);
        transaction.setTransactionDate(transactionDate);

        assertEquals(transactionId, transaction.getTransactionId());
        assertEquals(accountId, transaction.getAccountId());
        assertEquals(transactionType, transaction.getTransactionType());
        assertEquals(amount, transaction.getAmount());
        assertEquals(transactionDate, transaction.getTransactionDate());
    }
}
