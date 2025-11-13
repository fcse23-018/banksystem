package com.bankingsystem.dao;

import com.bankingsystem.model.Customer;
import com.bankingsystem.util.DatabaseUtil;
import com.bankingsystem.util.EncryptionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    public Customer validate(String idNumber, String password) {
        String sql = "SELECT * FROM CUSTOMER WHERE id_number = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String hashedPassword = rs.getString("password_hash");
                if (EncryptionUtil.checkPassword(password, hashedPassword)) {
                    return new Customer(
                            rs.getInt("id"),
                            rs.getString("first_name"),
                            rs.getString("surname"),
                            EncryptionUtil.decrypt(rs.getString("address_enc")),
                            rs.getString("id_number"),
                            rs.getString("status"),
                            rs.getString("role")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean addCustomer(Customer customer, String password) {
        String sql = "INSERT INTO CUSTOMER (first_name, surname, address_enc, id_number, password_hash, status, role) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, customer.getFirstName());
            stmt.setString(2, customer.getSurname());
            stmt.setString(3, EncryptionUtil.encrypt(customer.getAddress()));
            stmt.setString(4, customer.getIdNumber());
            stmt.setString(5, EncryptionUtil.hashPassword(password));
            stmt.setString(6, "ACTIVE"); // Default status
            stmt.setString(7, "CUSTOMER"); // Default role

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Customer> searchCustomersByName(String keyword) {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM CUSTOMER WHERE LOWER(first_name) LIKE ? OR LOWER(surname) LIKE ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchKeyword = "%" + keyword.toLowerCase() + "%";
            stmt.setString(1, searchKeyword);
            stmt.setString(2, searchKeyword);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                customers.add(new Customer(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("surname"),
                        EncryptionUtil.decrypt(rs.getString("address_enc")),
                        rs.getString("id_number"),
                        rs.getString("status"),
                        rs.getString("role")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }
        public boolean isIdNumberUnique(String idNumber) throws SQLException {
        String sql = "SELECT COUNT(*) FROM customer WHERE id_number = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, idNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 0;
                }
            }
        }
        return true;
    }

}
