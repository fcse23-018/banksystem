package com.bankingsystem.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

public class CustomerTest {

    private Customer customer;

    @BeforeEach
    public void setUp() {
        customer = new Customer("John", "Doe", "123 Main St", "password123");
    }

    @Test
    public void testCustomerCreation() {
        assertEquals("John", customer.getFirstName());
        assertEquals("Doe", customer.getSurname());
        assertEquals("123 Main St", customer.getAddress());
        assertEquals("password123", customer.getPassword());
        assertNotNull(customer.getAccounts());
        assertTrue(customer.getAccounts().isEmpty());
    }

    @Test
    public void testAddAccount() {
        UUID ownerId = UUID.randomUUID();
        Account savingsAccount = new SavingsAccount(ownerId);
        customer.addAccount(savingsAccount);
        assertEquals(1, customer.getAccounts().size());
        assertEquals(savingsAccount, customer.getAccounts().get(0));

        Account chequeAccount = new ChequeAccount(ownerId, "Employer", "Employer Address");
        customer.addAccount(chequeAccount);
        assertEquals(2, customer.getAccounts().size());
        assertEquals(chequeAccount, customer.getAccounts().get(1));
    }
}
