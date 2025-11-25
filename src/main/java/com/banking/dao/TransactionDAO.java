package com.banking.dao;

import com.banking.model.Transaction;
import com.banking.model.Account;
import com.banking.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Transaction entity.
 * Handles all database operations for transactions.
 * Logs and retrieves transaction history.
 * 
 * @author Donovan Ntsima (FCSE23-018)
 * @version 1.0
 */
public class TransactionDAO {
    
    /**
     * Creates a new transaction record in the database.
     * 
     * @param transaction the transaction to log
     * @return true if creation successful, false otherwise
     */
    public boolean createTransaction(Transaction transaction) {
        String sql = "INSERT INTO TRANSACTIONS (transaction_id, account_number, amount, transaction_type, balance_after) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, transaction.getTransactionID());
            pstmt.setString(2, transaction.getAccount().getAccountNumber());
            pstmt.setDouble(3, transaction.getAmount());
            pstmt.setString(4, transaction.getType());
            pstmt.setDouble(5, transaction.getBalanceAfter());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error creating transaction: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Retrieves all transactions for a specific account.
     * Ordered by date descending (most recent first).
     * 
     * @param accountNumber the account number
     * @param account the account object
     * @return list of all transactions for the account
     */
    public List<Transaction> getTransactionsByAccount(String accountNumber, Account account) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM TRANSACTIONS WHERE account_number = ? ORDER BY transaction_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, accountNumber);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                transactions.add(extractTransactionFromResultSet(rs, account));
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving transactions: " + e.getMessage());
        }
        
        return transactions;
    }
    
    /**
     * Retrieves a transaction by its ID.
     * 
     * @param transactionID the transaction ID
     * @param account the account object
     * @return the transaction if found, null otherwise
     */
    public Transaction getTransactionById(String transactionID, Account account) {
        String sql = "SELECT * FROM TRANSACTIONS WHERE transaction_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, transactionID);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractTransactionFromResultSet(rs, account);
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving transaction: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Retrieves recent transactions for an account (limited number).
     * 
     * @param accountNumber the account number
     * @param account the account object
     * @param limit maximum number of transactions to retrieve
     * @return list of recent transactions
     */
    public List<Transaction> getRecentTransactions(String accountNumber, Account account, int limit) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM TRANSACTIONS WHERE account_number = ? ORDER BY transaction_date DESC LIMIT ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, accountNumber);
            pstmt.setInt(2, limit);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                transactions.add(extractTransactionFromResultSet(rs, account));
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving recent transactions: " + e.getMessage());
        }
        
        return transactions;
    }
    
    /**
     * Deletes all transactions for a specific account.
     * 
     * @param accountNumber the account number
     * @return true if deletion successful, false otherwise
     */
    public boolean deleteTransactionsByAccount(String accountNumber) {
        String sql = "DELETE FROM TRANSACTIONS WHERE account_number = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, accountNumber);
            pstmt.executeUpdate();
            return true;
            
        } catch (SQLException e) {
            System.err.println("Error deleting transactions: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Generates a unique transaction ID.
     * Format: TXN + timestamp + random 4 digits
     * 
     * @return the generated transaction ID
     */
    public static String generateTransactionID() {
        long timestamp = System.currentTimeMillis() % 10000000;
        int random = (int) (Math.random() * 9000) + 1000;
        return String.format("TXN%d%d", timestamp, random);
    }
    
    /**
     * Extracts a Transaction object from a ResultSet.
     * 
     * @param rs the ResultSet containing transaction data
     * @param account the account involved in the transaction
     * @return the extracted Transaction object
     * @throws SQLException if extraction fails
     */
    private Transaction extractTransactionFromResultSet(ResultSet rs, Account account) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setTransactionID(rs.getString("transaction_id"));
        transaction.setAmount(rs.getDouble("amount"));
        transaction.setType(rs.getString("transaction_type"));
        transaction.setBalanceAfter(rs.getDouble("balance_after"));
        transaction.setDate(rs.getTimestamp("transaction_date"));
        transaction.setAccount(account);
        
        return transaction;
    }
}
