package com.bankingsystem.dao;

import com.bankingsystem.model.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class AccountDAOTest {

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @Mock
    private ResultSet mockResultSet;

    private AccountDAO accountDAO;

    @BeforeEach
    public void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        accountDAO = new AccountDAO(mockConnection);
    }

    @Test
    public void testGetAccountsByCustomerId() throws SQLException {
        UUID customerId = UUID.randomUUID();
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        // Mock account data if the Account class was fully implemented

        List<Account> accounts = accountDAO.getAccountsByCustomerId(customerId);

        // The test will pass if no exceptions are thrown and the method returns an empty list.
        // A more complete test would assert the contents of the list.
        assertEquals(0, accounts.size());
    }
}
