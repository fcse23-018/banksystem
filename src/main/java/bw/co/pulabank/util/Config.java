package bw.co.pulabank.util;

import java.io.IOException;
import java.util.Properties;

/**
 * Configuration utility for loading properties from config.properties
 * Provides secure access to database and application settings
 */
public class Config {
    private static final Properties properties = new Properties();

    static {
        try {
            properties.load(Config.class.getClassLoader()
                    .getResourceAsStream("config.properties"));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    /**
     * Retrieves a configuration value by key
     * @param key the property key
     * @return the property value, or null if not found
     */
    public static String get(String key) {
        return properties.getProperty(key);
    }

    /**
     * Retrieves a configuration value with a default fallback
     * @param key the property key
     * @param defaultValue the default value if key not found
     * @return the property value or default
     */
    public static String get(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    /**
     * Retrieves an integer configuration value
     * @param key the property key
     * @param defaultValue the default value if not found
     * @return the property value as integer
     */
    public static int getInt(String key, int defaultValue) {
        try {
            String value = properties.getProperty(key);
            return value != null ? Integer.parseInt(value) : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Retrieves a double configuration value
     * @param key the property key
     * @param defaultValue the default value if not found
     * @return the property value as double
     */
    public static double getDouble(String key, double defaultValue) {
        try {
            String value = properties.getProperty(key);
            return value != null ? Double.parseDouble(value) : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
