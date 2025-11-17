package bw.co.pulabank.service;

import bw.co.pulabank.model.*;
import bw.co.pulabank.util.DatabaseUtil;
import bw.co.pulabank.util.PasswordUtil;

import java.sql.*;

public class AuthService {
    private static CurrentUser currentUser;

    public boolean login(String email, String password) {
        String sql = """
            SELECT c.id, c.first_name, c.surname, c.email, c.omang, c.phone,
                   c.customer_type, c.status, c.password_hash,
                   s.staff_id, s.full_name AS staff_name, s.role
            FROM customers c
            LEFT JOIN staff s ON c.email = s.email
            WHERE c.email = ?
            """;

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next() && PasswordUtil.check(password, rs.getString("password_hash"))) {
                CurrentUser.CurrentUserBuilder builder = CurrentUser.builder()
                        .email(email);

                String roleStr = rs.getString("role");
                if (roleStr != null) {
                    // Staff login
                    builder.role(UserRole.valueOf(roleStr))
                           .staffName(rs.getString("staff_name"));
                } else {
                    // Customer login
                    Customer customer = Customer.builder()
                            .id(rs.getObject("id", java.util.UUID.class))
                            .firstName(rs.getString("first_name"))
                            .surname(rs.getString("surname"))
                            .email(email)
                            .omang(rs.getString("omang"))
                            .phone(rs.getString("phone"))
                            .customerType(CustomerType.valueOf(rs.getString("customer_type")))
                            .status(CustomerStatus.valueOf(rs.getString("status")))
                            .build();
                    builder.role(UserRole.CUSTOMER).customer(customer);
                }
                currentUser = builder.build();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void logout() { currentUser = null; }
    public CurrentUser getCurrentUser() { return currentUser; }
    public boolean isLoggedIn() { return currentUser != null; }
}