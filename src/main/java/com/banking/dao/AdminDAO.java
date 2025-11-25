package com.banking.dao;

import com.banking.model.Admin;
import com.banking.util.DatabaseConnection;
import com.banking.util.PasswordUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Admin operations.
 * Handles CRUD operations for admins in the database.
 * 
 * @author Donovan Ntsima (FCSE23-018)
 * @version 2.0
 */
public class AdminDAO {
    
    /**
     * Authenticates an admin using their username and password.
     * 
     * @param username the admin username
     * @param plainPassword the plain text password
     * @return the admin if authentication successful, null otherwise
     */
    public Admin authenticate(String username, String plainPassword) {
        String sql = "SELECT * FROM ADMINS WHERE username = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Admin admin = extractAdminFromResultSet(rs);
                if (PasswordUtil.checkPassword(plainPassword, admin.getPassword())) {
                    return admin;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error authenticating admin: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Retrieves an admin by their ID.
     * 
     * @param adminId the admin ID to search for
     * @return the admin if found, null otherwise
     */
    public Admin getAdminById(String adminId) {
        String sql = "SELECT * FROM ADMINS WHERE admin_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, adminId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractAdminFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving admin: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Retrieves all admins from the database.
     * 
     * @return list of all admins
     */
    public List<Admin> getAllAdmins() {
        List<Admin> admins = new ArrayList<>();
        String sql = "SELECT * FROM ADMINS ORDER BY admin_id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                admins.add(extractAdminFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving admins: " + e.getMessage());
        }
        
        return admins;
    }
    
    /**
     * Extracts an Admin object from a ResultSet.
     * 
     * @param rs the ResultSet containing admin data
     * @return the extracted Admin object
     * @throws SQLException if extraction fails
     */
    private Admin extractAdminFromResultSet(ResultSet rs) throws SQLException {
        return new Admin(
            rs.getString("admin_id"),
            rs.getString("username"),
            rs.getString("password"),
            rs.getString("full_name")
        );
    }
}
