package bw.co.pulabank.service;

import bw.co.pulabank.model.Account;
import bw.co.pulabank.model.AccountStatus;
import bw.co.pulabank.model.AccountType;
import bw.co.pulabank.util.DatabaseUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AccountServiceTest {
    private Connection conn = mock(Connection.class);
    private PreparedStatement ps = mock(PreparedStatement.class);
    private ResultSet rs = mock(ResultSet.class);

    @BeforeEach
    public void setup() throws Exception {
        // Use the DatabaseUtil provider hook so we don't need static mocking frameworks
        bw.co.pulabank.util.DatabaseUtil.setConnectionProvider(() -> conn);
        when(conn.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
    }

    @AfterEach
    public void tearDown() {
        bw.co.pulabank.util.DatabaseUtil.setConnectionProvider(null);
    }

    @Test
    public void getCustomerAccounts_returnsList() throws Exception {
        UUID cid = UUID.randomUUID();

        when(rs.next()).thenReturn(true, true, false);
        when(rs.getObject("id", java.util.UUID.class)).thenReturn(UUID.randomUUID());
        when(rs.getString("account_number")).thenReturn("ACC123");
        when(rs.getString("account_type")).thenReturn(AccountType.SAVINGS.name());
        when(rs.getDouble("balance")).thenReturn(1000.0);
        when(rs.getString("status")).thenReturn(AccountStatus.ACTIVE.name());
        when(rs.getTimestamp("opened_at")).thenReturn(Timestamp.valueOf(java.time.LocalDateTime.now()));

        AccountService svc = new AccountService();
        List<Account> accounts = svc.getCustomerAccounts(cid);

        assertNotNull(accounts);
        assertEquals(2, accounts.size());
        for (Account a : accounts) {
            assertEquals("ACC123", a.getAccountNumber());
            assertEquals(AccountType.SAVINGS, a.getAccountType());
        }
    }
}
