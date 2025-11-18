package bw.co.pulabank.service;

import bw.co.pulabank.model.*;
import bw.co.pulabank.util.DatabaseUtil;

import java.sql.*;
import java.time.LocalDateTime;

public class TransactionService {

    public boolean transfer(String fromAccNum, String toAccNum, double amount, String reference) {
        String sql = "{CALL perform_transfer(?, ?, ?, ?)}"; // Use your Supabase stored procedure or plain SQL

        try (Connection conn = DatabaseUtil.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, fromAccNum);
            cs.setString(2, toAccNum);
            cs.setDouble(3, amount);
            cs.setString(4, reference != null ? reference : "Transfer");

            cs.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void recordTransaction(Account from, Account to, double amount, String ref, TransactionType type) {
        String sql = "INSERT INTO transactions (from_account, to_account, amount, type, reference, timestamp) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, from != null ? from.getAccountNumber() : null);
            ps.setString(2, to != null ? to.getAccountNumber() : null);
            ps.setDouble(3, amount);
            ps.setString(4, type.name());
            ps.setString(5, ref);
            ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}