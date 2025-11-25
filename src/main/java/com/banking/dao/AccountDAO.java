package com.banking.dao;

import com.banking.model.*;
import com.banking.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Account entity.
 * Handles all database operations for accounts.
 * Implements polymorphic account loading based on account_type discriminator.
 * 
 * @author Donovan Ntsima (FCSE23-018)
 * @version 1.0
 */
public class AccountDAO {
    
    /**
     * Creates a new account in the database.
     * 
     * @param account the account to create
     * @return true if creation successful, false otherwise
     */
    public boolean createAccount(Account account) {
        String sql = "INSERT INTO ACCOUNTS (account_number, customer_id, account_type, balance, branch, employer_company, employer_address) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, account.getAccountNumber());
            pstmt.setString(2, account.getCustomer().getCustomerID());
            pstmt.setString(3, account.getAccountType());
            pstmt.setDouble(4, account.getBalance());
            pstmt.setString(5, account.getBranch());
            
            if (account instanceof ChequeAccount) {
                ChequeAccount chequeAccount = (ChequeAccount) account;
                pstmt.setString(6, chequeAccount.getEmployerCompany());
                pstmt.setString(7, chequeAccount.getEmployerAddress());
            } else {
                pstmt.setNull(6, Types.VARCHAR);
                pstmt.setNull(7, Types.VARCHAR);
            }
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error creating account: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Retrieves an account by its account number.
     * Implements polymorphic loading based on account type.
     * 
     * @param accountNumber the account number to search for
     * @param customer the customer who owns the account
     * @return the account if found, null otherwise
     */
    public Account getAccountByNumber(String accountNumber, Customer customer) {
        String sql = "SELECT * FROM ACCOUNTS WHERE account_number = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, accountNumber);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractAccountFromResultSet(rs, customer);
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving account: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Retrieves all accounts for a specific customer.
     * Implements polymorphic loading for different account types.
     * 
     * @param customerID the customer ID
     * @param customer the customer object
     * @return list of all accounts owned by the customer
     */
    public List<Account> getAccountsByCustomer(String customerID, Customer customer) {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM ACCOUNTS WHERE customer_id = ? ORDER BY created_at";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, customerID);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Account account = extractAccountFromResultSet(rs, customer);
                if (account != null) {
                    accounts.add(account);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving accounts: " + e.getMessage());
        }
        
        return accounts;
    }
    
    /**
     * Updates an account's balance in the database.
     * 
     * @param accountNumber the account number
     * @param newBalance the new balance
     * @return true if update successful, false otherwise
     */
    public boolean updateBalance(String accountNumber, double newBalance) {
        String sql = "UPDATE ACCOUNTS SET balance = ? WHERE account_number = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDouble(1, newBalance);
            pstmt.setString(2, accountNumber);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating balance: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Deletes an account from the database.
     * Cascades to delete all associated transactions.
     * 
     * @param accountNumber the account number to delete
     * @return true if deletion successful, false otherwise
     */
    public boolean deleteAccount(String accountNumber) {
        String sql = "DELETE FROM ACCOUNTS WHERE account_number = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, accountNumber);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting account: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Transfers funds from one account to another.
     * Uses database transaction to ensure atomicity.
     * 
     * @param fromAccount the source account number
     * @param toAccount the destination account number
     * @param amount the amount to transfer
     * @return true if transfer successful, false otherwise
     */
    public boolean transferFunds(String fromAccount, String toAccount, double amount) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction
            
            // Deduct from source account
            String deductSql = "UPDATE ACCOUNTS SET balance = balance - ? WHERE account_number = ? AND balance >= ?";
            try (PreparedStatement pstmt = conn.prepareStatement(deductSql)) {
                pstmt.setDouble(1, amount);
                pstmt.setString(2, fromAccount);
                pstmt.setDouble(3, amount);
                
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected == 0) {
                    conn.rollback();
                    return false; // Insufficient funds
                }
            }
            
            // Add to destination account
            String addSql = "UPDATE ACCOUNTS SET balance = balance + ? WHERE account_number = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(addSql)) {
                pstmt.setDouble(1, amount);
                pstmt.setString(2, toAccount);
                
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected == 0) {
                    conn.rollback();
                    return false; // Destination account not found
                }
            }
            
            conn.commit(); // Commit transaction
            return true;
            
        } catch (SQLException e) {
            System.err.println("Error transferring funds: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.err.println("Error rolling back transaction: " + ex.getMessage());
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    System.err.println("Error resetting auto-commit: " + e.getMessage());
                }
            }
        }
    }
    
    /**
     * Checks if an account number already exists.
     * 
     * @param accountNumber the account number to check
     * @return true if exists, false otherwise
     */
    public boolean accountExists(String accountNumber) {
        String sql = "SELECT COUNT(*) FROM ACCOUNTS WHERE account_number = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, accountNumber);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking account existence: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Extracts an Account object from a ResultSet.
     * Implements polymorphic loading based on account_type discriminator.
     * 
     * @param rs the ResultSet containing account data
     * @param customer the customer who owns the account
     * @return the extracted Account object (SavingsAccount, InvestmentAccount, or ChequeAccount)
     * @throws SQLException if extraction fails
     */
    private Account extractAccountFromResultSet(ResultSet rs, Customer customer) throws SQLException {
        String accountNumber = rs.getString("account_number");
        double balance = rs.getDouble("balance");
        String branch = rs.getString("branch");
        String accountType = rs.getString("account_type");
        
        Account account = null;
        
        switch (accountType) {
            case "Savings Account":
                account = new SavingsAccount(accountNumber, balance, branch, customer);
                break;
            case "Investment Account":
                account = new InvestmentAccount(accountNumber, balance, branch, customer);
                break;
            case "Cheque Account":
                String employerCompany = rs.getString("employer_company");
                String employerAddress = rs.getString("employer_address");
                account = new ChequeAccount(accountNumber, balance, branch, customer, 
                                          employerCompany, employerAddress);
                break;
            default:
                System.err.println("Unknown account type: " + accountType);
        }
        
        return account;
    }
}
