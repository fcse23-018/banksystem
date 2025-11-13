package com.bankingsystem.dao;

import com.bankingsystem.model.BankStaff;
import com.bankingsystem.util.DatabaseUtil;
import com.bankingsystem.util.EncryptionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BankStaffDAO {

    public BankStaff validate(String idNumber, String password) {
        String sql = "SELECT * FROM CUSTOMER WHERE id_number = ? AND role = 'STAFF'";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String hashedPassword = rs.getString("password_hash");
                if (EncryptionUtil.checkPassword(password, hashedPassword)) {
                    return new BankStaff(
                            rs.getInt("id"),
                            rs.getString("first_name"),
                            rs.getString("surname"),
                            rs.getString("id_number"),
                            rs.getString("role")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
