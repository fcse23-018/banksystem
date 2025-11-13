package com.bankingsystem.dao;

import com.bankingsystem.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDAO {

    /**
     * Validates a customer's login credentials.
     * @param username The username.
     * @param password The password.
     * @return true if the credentials are valid, false otherwise.
     */
    public boolean validate(String username, String password) {
        String sql = "SELECT password FROM customers WHERE first_name = ?"; // Assuming username is the first name for now

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password");
                return storedPassword.equals(password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Adds a new customer to the database.
     * @param firstName The customer's first name.
     * @param lastName The customer's last name.
     * @param email The customer's email.
     * @param password The customer's password.
     * @return true if the customer was added successfully, false otherwise.
     */
    public boolean addCustomer(String firstName, String lastName, String email, String password) {
        String sql = "INSERT INTO customers (first_name, last_name, email, password) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setString(3, email);
            stmt.setString(4, password);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
