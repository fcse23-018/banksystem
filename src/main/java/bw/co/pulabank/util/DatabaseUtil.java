package bw.co.pulabank.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {

    // YOUR REAL SUPABASE CONNECTION STRING (Botswana project)
    private static final String SUPABASE_URL = "jdbc:postgresql://db.vgssxkidvqovqebkobvp.supabase.co:5432/postgres";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "@DNkudumatse1997";  // Change this later to env var!

    // Optional: Use environment variables (recommended for production)
    static {
        String envUrl = System.getenv("SUPABASE_JDBC_URL");
        String envPass = System.getenv("SUPABASE_PASSWORD");
        if (envUrl != null && envPass != null) {
            // Override with env vars if set
            java.util.Properties props = System.getProperties();
            props.setProperty("SUPABASE_JDBC_URL", envUrl);
            props.setProperty("SUPABASE_PASSWORD", envPass);
        }
    }

    public static Connection getConnection() throws SQLException {
        try {
            // PostgreSQL driver is auto-loaded in Java 21+
            String url = System.getProperty("SUPABASE_JDBC_URL", SUPABASE_URL);
            String pass = System.getProperty("SUPABASE_PASSWORD", PASSWORD);

            return DriverManager.getConnection(url, USERNAME, pass);
        } catch (Exception e) {
            throw new SQLException("Failed to connect to Supabase PostgreSQL", e);
        }
    }

    // Test connection (run this once)
    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            System.out.println("Connected to Pula Bank Supabase DB successfully!");
            System.out.println("Database: " + conn.getMetaData().getDatabaseProductName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}