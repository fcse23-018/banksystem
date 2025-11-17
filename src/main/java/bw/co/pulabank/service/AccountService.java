package bw.co.pulabank.service;

import bw.co.pulabank.model.*;
import bw.co.pulabank.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AccountService {

    public List<Account> getAccountsForCustomer(UUID customerId) {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM accounts WHERE customer_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setObject(1, customerId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                accounts.add(Account.builder()
                    .id(rs.getObject("id", UUID.class))
                    .customerId(customerId)
                    .accountType(AccountType.valueOf(rs.getString("account_type")))
                    .accountNumber(rs.getString("account_number"))
                    .balance(rs.getDouble("balance"))
                    .status(AccountStatus.valueOf(rs.getString("status")))
                    .interestRate(rs.getDouble("interest_rate"))
                    .openedAt(rs.getTimestamp("opened_at").toLocalDateTime())
                    .build());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }

    public boolean createAccount(UUID customerId, AccountType type, String proofUrl) {
        String number = "60" + String.format("%010d", (long)(Math.random() * 1_000_000_000L));
        String status = (type == AccountType.INVESTMENT) ? "ACTIVE" : "PENDING";

        String sql = "INSERT INTO accounts (customer_id, account_type, account_number, " +
                     "balance, status, interest_rate, employment_proof_url) VALUES (?, ?, ?, 0, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setObject(1, customerId);
            pstmt.setString(2, type.name());
            pstmt.setString(3, number);
            pstmt.setString(4, status);
            pstmt.setDouble(5, type.getMonthlyInterestRate());
            pstmt.setString(6, proofUrl);

            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}