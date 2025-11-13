package dao;

import model.Transaction;
import util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    /**
     * Retrieves all transactions for a given account.
     * @param accountId The ID of the account to retrieve transactions for.
     * @return A list of transactions.
     */
    public List<Transaction> getTransactionsForAccount(long accountId) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE account_id = ? ORDER BY transaction_date DESC";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, accountId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Transaction transaction = new Transaction();
                transaction.setTransactionId(rs.getLong("transaction_id"));
                transaction.setAccountId(rs.getLong("account_id"));
                transaction.setTransactionType(Transaction.TransactionType.valueOf(rs.getString("transaction_type")));
                transaction.setAmount(rs.getDouble("amount"));
                transaction.setTransactionDate(rs.getTimestamp("transaction_date"));
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return transactions;
    }

    // Other DAO methods for creating transactions would go here
}
