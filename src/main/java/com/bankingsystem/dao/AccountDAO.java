package com.bankingsystem.dao;

import com.bankingsystem.model.Account;
import com.bankingsystem.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AccountDAO {

    /**
     * Retrieves all accounts for a given customer.
     * @param customerId The ID of the customer.
     * @return A list of accounts.
     */
    public List<Account> getAccountsByCustomerId(UUID customerId) {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM accounts WHERE owner_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, customerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                // Create account objects from the result set and add to the list
                // This part needs to be implemented based on the Account class structure
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return accounts;
    }
}
