package com.banking.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utility class for password hashing and verification using BCrypt.
 * Ensures secure password storage and authentication.
 * 
 * @author Donovan Ntsima (FCSE23-018)
 * @version 1.0
 */
public class PasswordUtil {
    
    /**
     * Private constructor to prevent instantiation.
     */
    private PasswordUtil() {
    }
    
    /**
     * Hashes a plain text password using BCrypt.
     * 
     * @param plainPassword the plain text password
     * @return the hashed password
     */
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
    }
    
    /**
     * Verifies a plain text password against a hashed password.
     * 
     * @param plainPassword the plain text password to verify
     * @param hashedPassword the hashed password to compare against
     * @return true if passwords match, false otherwise
     */
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        try {
            return BCrypt.checkpw(plainPassword, hashedPassword);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
