package com.bankingsystem.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {

    /**
     * Establishes a connection to the database using environment variables.
     * @return A Connection object.
     * @throws SQLException if a database access error occurs.
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Register the PostgreSQL driver
            Class.forName("org.postgresql.Driver");

            // Get database credentials from environment variables
            String dbUrl = System.getenv("DB_URL");
            String dbUser = System.getenv("DB_USER");
            String dbPassword = System.getenv("DB_PASSWORD");

            // Check if the environment variables are set
            if (dbUrl == null || dbUser == null || dbPassword == null) {
                throw new SQLException("Database credentials are not set in the environment variables (DB_URL, DB_USER, DB_PASSWORD)");
            }

            // Get the connection from the driver manager
            return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        } catch (ClassNotFoundException e) {
            throw new SQLException("PostgreSQL JDBC Driver not found", e);
        }
    }
}
