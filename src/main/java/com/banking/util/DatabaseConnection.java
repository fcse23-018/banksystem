package com.banking.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Utility class for managing PostgreSQL database connections.
 * Handles connection to Supabase PostgreSQL database.
 * Implements singleton pattern for connection management.
 * 
 * @author Donovan Ntsima (FCSE23-018)
 * @version 1.0
 */
public class DatabaseConnection {
    
    private static final String URL = "jdbc:postgresql://db.vgssxkidvqovqebkobvp.supabase.co:5432/postgres";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "PulaBank2025!@#Gaborone";
    private static final String DRIVER = "org.postgresql.Driver";
    
    private static Connection connection = null;
    
    /**
     * Private constructor to prevent instantiation.
     */
    private DatabaseConnection() {
    }
    
    /**
     * Gets a connection to the database.
     * Creates a new connection if one doesn't exist.
     * 
     * @return the database connection
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName(DRIVER);
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("Database connection established successfully.");
            }
            return connection;
        } catch (ClassNotFoundException e) {
            throw new SQLException("PostgreSQL JDBC Driver not found", e);
        }
    }
    
    /**
     * Closes the database connection.
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }
    
    /**
     * Initializes the database schema.
     * Creates tables if they don't exist.
     * 
     * @throws SQLException if initialization fails
     */
    public static void initializeDatabase() throws SQLException {
        Connection conn = getConnection();
        Statement stmt = conn.createStatement();
        
        String createCustomersTable = """
            CREATE TABLE IF NOT EXISTS CUSTOMERS (
                customer_id VARCHAR(50) PRIMARY KEY,
                first_name VARCHAR(100) NOT NULL,
                surname VARCHAR(100) NOT NULL,
                address TEXT NOT NULL,
                password VARCHAR(255) NOT NULL,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """;
        
        String createAccountsTable = """
            CREATE TABLE IF NOT EXISTS ACCOUNTS (
                account_number VARCHAR(50) PRIMARY KEY,
                customer_id VARCHAR(50) NOT NULL,
                account_type VARCHAR(20) NOT NULL,
                balance DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
                branch VARCHAR(100) NOT NULL,
                employer_company VARCHAR(200),
                employer_address TEXT,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (customer_id) REFERENCES CUSTOMERS(customer_id) ON DELETE CASCADE
            )
            """;
        
        String createTransactionsTable = """
            CREATE TABLE IF NOT EXISTS TRANSACTIONS (
                transaction_id VARCHAR(50) PRIMARY KEY,
                account_number VARCHAR(50) NOT NULL,
                amount DECIMAL(15, 2) NOT NULL,
                transaction_type VARCHAR(20) NOT NULL,
                balance_after DECIMAL(15, 2) NOT NULL,
                transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (account_number) REFERENCES ACCOUNTS(account_number) ON DELETE CASCADE
            )
            """;
        
        stmt.execute(createCustomersTable);
        stmt.execute(createAccountsTable);
        stmt.execute(createTransactionsTable);
        
        System.out.println("Database schema initialized successfully.");
        stmt.close();
    }
    
    /**
     * Tests the database connection.
     * 
     * @return true if connection is successful, false otherwise
     */
    public static boolean testConnection() {
        try {
            Connection conn = getConnection();
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Database connection test failed: " + e.getMessage());
            return false;
        }
    }
}
