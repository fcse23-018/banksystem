package bw.co.pulabank.service;

import bw.co.pulabank.model.*;
import bw.co.pulabank.util.CurrencyFormatter;
import bw.co.pulabank.util.DatabaseUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TransactionService {

    public boolean deposit(UUID accountId, double amount, String reference) {
        return executeTransaction(accountId, amount, TransactionType.DEPOSIT, reference);
    }

    public boolean withdraw(UUID accountId, double amount, String reference) {
        // Check balance first
        double balance = getAccountBalance(accountId);
        if (balance < amount) return false;

        return executeTransaction(accountId, -amount, TransactionType.WITHDRAWAL, reference);
    }

    public boolean transfer(UUID fromId, UUID toId, double amount, String reference) {
        if (!withdraw(fromId, amount, "Transfer → " + toId)) return false;
        deposit(toId, amount, "Transfer ← " + fromId);
        return true;
    }

    private boolean executeTransaction(UUID accountId, double amount, TransactionType type, String ref) {
        String sql = "INSERT INTO transactions (account_id, type, amount, balance_after, reference, created_at) " +
                     "SELECT ?, ?, ?, balance + ?, ?, NOW() FROM accounts WHERE id = ? " +
                     "RETURNING (balance + ?)";

        String update = "UPDATE accounts SET balance = balance + ? WHERE id = ?";

        try (Connection conn = DatabaseUtil.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement ps1 = conn.prepareStatement(sql);
                 PreparedStatement ps2 = conn.prepareStatement(update)) {

                ps1.setObject(1, accountId);
                ps1.setString(2, type.name());
                ps1.setDouble(3, amount);
                ps1.setDouble(4, amount);
                ps1.setString(5, ref);
                ps1.setObject(6, accountId);
                ps1.setDouble(7, amount);

                ps2.setDouble(1, amount);
                ps2.setObject(2, accountId);

                ps1.executeQuery();
                ps2.executeUpdate();

                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Transaction> getTransactions(UUID accountId) {
        List<Transaction> list = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE account_id = ? ORDER BY created_at DESC LIMIT 50";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setObject(1, accountId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                list.add(Transaction.builder()
                    .id(rs.getObject("id", UUID.class))
                    .type(TransactionType.valueOf(rs.getString("type")))
                    .amount(rs.getDouble("amount"))
                    .balanceAfter(rs.getDouble("balance_after"))
                    .reference(rs.getString("reference"))
                    .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                    .build());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private double getAccountBalance(UUID accountId) {
        String sql = "SELECT balance FROM accounts WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setObject(1, accountId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getDouble("balance");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
}