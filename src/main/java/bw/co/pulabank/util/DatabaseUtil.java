package bw.co.pulabank.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Pula Bank Botswana – Secure Database Connection
 * Uses config.properties – NO PASSWORD IN CODE!
 * Updated: 17 November 2025, 03:42 PM CAT – Gaborone
 */
public class DatabaseUtil {

    // Values loaded securely from config.properties
    private static final String DB_URL      = Config.get("db.url");
    private static final String DB_USERNAME = Config.get("db.username");
    private static final String DB_PASSWORD = Config.get("db.password");

    /**
     * Returns a live connection to your real Supabase PostgreSQL database
     */
    public static Connection getConnection() throws SQLException {
        try {
            // PostgreSQL driver auto-loaded in Java 21+
            return DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        } catch (Exception e) {
            throw new SQLException("Failed to connect to Pula Bank Supabase database", e);
        }
    }

    // Test it anytime – run this main method
    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            System.out.println("PULA BANK BOTSWANA – DATABASE CONNECTED SUCCESSFULLY!");
            System.out.println("Server: " + conn.getMetaData().getDatabaseProductName());
            System.out.println("URL: " + DB_URL);
            System.out.println("Connected as: " + DB_USERNAME);
            System.out.println("Time: " + java.time.LocalDateTime.now());
            System.out.println("Pula! Your bank is LIVE in Gaborone!");
        } catch (Exception e) {
            System.err.println("Connection failed – check config.properties");
            e.printStackTrace();
        }
    }
}