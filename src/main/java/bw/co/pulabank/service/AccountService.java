package bw.co.pulabank.service;

import bw.co.pulabank.model.*;
import bw.co.pulabank.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AccountService {

    public List<Account> getCustomerAccounts(UUID customerId) {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM accounts WHERE customer_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, customerId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Account acc = Account.builder()
                        .id(rs.getObject("id", UUID.class))
                        .accountNumber(rs.getString("account_number"))
                        .accountType(AccountType.valueOf(rs.getString("account_type")))
                        .balance(rs.getDouble("balance"))
                        .status(AccountStatus.valueOf(rs.getString("status")))
                        .openedDate(rs.getTimestamp("opened_date").toLocalDateTime())
                        .build();
                accounts.add(acc);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return accounts;
    }

    public Account getAccountByNumber(String accountNumber) {
        String sql = "SELECT * FROM accounts WHERE account_number = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, accountNumber);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Account.builder()
                        .id(rs.getObject("id", UUID.class))
                        .accountNumber(accountNumber)
                        .accountType(AccountType.valueOf(rs.getString("account_type")))
                        .balance(rs.getDouble("balance"))
                        .status(AccountStatus.valueOf(rs.getString("status")))
                        .build();
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }
}