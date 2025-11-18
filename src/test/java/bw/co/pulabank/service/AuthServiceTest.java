package bw.co.pulabank.service;

import bw.co.pulabank.model.CurrentUser;
import bw.co.pulabank.model.UserRole;
import bw.co.pulabank.util.DatabaseUtil;
import bw.co.pulabank.util.PasswordUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthServiceTest {

    private Connection conn = mock(Connection.class);
    private PreparedStatement ps = mock(PreparedStatement.class);
    private ResultSet rs = mock(ResultSet.class);

    @BeforeEach
    public void setup() throws Exception {
        // Inject our mock connection via the test hook
        bw.co.pulabank.util.DatabaseUtil.setConnectionProvider(() -> conn);
        when(conn.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
    }

    @AfterEach
    public void tearDown() {
        bw.co.pulabank.util.DatabaseUtil.setConnectionProvider(null);
    }

    @Test
    public void loginCustomer_success() throws Exception {
        String email = "jane@example.com";
        String plain = "secret";
        String hash = PasswordUtil.hash(plain);

        when(rs.next()).thenReturn(true);
        when(rs.getString("password_hash")).thenReturn(hash);
        when(rs.getString("role")).thenReturn(null);
        when(rs.getObject("id", java.util.UUID.class)).thenReturn(UUID.randomUUID());
        when(rs.getString("first_name")).thenReturn("Jane");
        when(rs.getString("surname")).thenReturn("Doe");
        when(rs.getString("omang")).thenReturn("12345");
        when(rs.getString("phone")).thenReturn("+26770000000");
        when(rs.getString("customer_type")).thenReturn("INDIVIDUAL");
        when(rs.getString("status")).thenReturn("ACTIVE");

        AuthService svc = new AuthService();
        boolean ok = svc.login(email, plain);
        assertTrue(ok);

        CurrentUser cu = svc.getCurrentUser();
        assertNotNull(cu);
        assertEquals(email, cu.getEmail());
        assertEquals(UserRole.CUSTOMER, cu.getRole());
        assertNotNull(cu.getCustomer());
    }
}
