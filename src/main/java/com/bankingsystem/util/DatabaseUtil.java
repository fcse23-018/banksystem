package com.bankingsystem.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {

    /**
     * Establishes a connection to the H2 in-memory database.
     * @return A Connection object.
     * @throws SQLException if a database access error occurs.
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Register the H2 driver
            Class.forName("org.h2.Driver");

            // Get the connection from the driver manager
            return DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        } catch (ClassNotFoundException e) {
            throw new SQLException("H2 JDBC Driver not found", e);
        }
    }
}
