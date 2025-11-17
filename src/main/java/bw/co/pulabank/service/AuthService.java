package bw.co.pulabank.service;

import bw.co.pulabank.model.*;
import bw.co.pulabank.util.DatabaseUtil;
import bw.co.pulabank.util.PasswordUtil;

import java.sql.*;

public class AuthService {

    private static CurrentUser currentUser;

    public boolean login(String email, String password) {
        String sql = "SELECT c.*, s.role, s.full_name FROM customers c " +
                     "LEFT JOIN staff s ON c.email = s.email WHERE c.email = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next() && PasswordUtil.check(password, rs.getString("password_hash"))) {
                UserRole role = rs.getString("role") != null ?
                        UserRole.valueOf(rs.getString("role")) : UserRole.CUSTOMER;

                CurrentUser.CurrentUserBuilder builder = CurrentUser.builder()
                        .email(email)
                        .role(role);

                if (role == UserRole.CUSTOMER) {
                    Customer customer = Customer.builder()
                            .id(rs.getObject("id", java.util.UUID.class))
                            .firstName(rs.getString("first_name"))
                            .surname(rs.getString("surname"))
                            .email(email)
                            .customerType(CustomerType.valueOf(rs.getString("customer_type")))
                            .status(CustomerStatus.valueOf(rs.getString("status")))
                            .build();
                    builder.customer(customer);
                } else {
                    builder.staffName(rs.getString("full_name"));
                }

                currentUser = builder.build();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean register(Customer customer, String password) {
        String sql = "INSERT INTO customers (id, first_name, surname, email, password_hash, " +
                     "omang, address, customer_type, phone, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            UUID id = UUID.randomUUID();
            pstmt.setObject(1, id);
            pstmt.setString(2, customer.getFirstName());
            pstmt.setString(3, customer.getSurname());
            pstmt.setString(4, customer.getEmail());
            pstmt.setString(5, PasswordUtil.hash(password));
            pstmt.setString(6, customer.getOmang());
            pstmt.setString(7, customer.getAddress());
            pstmt.setString(8, customer.getCustomerType().name());
            pstmt.setString(9, customer.getPhone());
            pstmt.setString(10, CustomerStatus.PENDING.name());

            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void logout() {
        currentUser = null;
    }

    public CurrentUser getCurrentUser() { return currentUser; }
    public boolean isLoggedIn() { return currentUser != null; }
}