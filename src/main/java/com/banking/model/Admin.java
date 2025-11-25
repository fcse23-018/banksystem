package com.banking.model;

/**
 * Represents a bank administrator with system access privileges.
 * Admins can manage customers, view reports, and perform administrative tasks.
 * 
 * @author Donovan Ntsima (FCSE23-018)
 * @version 2.0
 */
public class Admin {
    
    private String adminId;
    private String username;
    private String password;
    private String fullName;
    
    /**
     * Default constructor for Admin.
     */
    public Admin() {
    }
    
    /**
     * Parameterized constructor for Admin.
     * 
     * @param adminId the unique admin ID
     * @param username the admin's username
     * @param password the admin's hashed password
     * @param fullName the admin's full name
     */
    public Admin(String adminId, String username, String password, String fullName) {
        this.adminId = adminId;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
    }
    
    /**
     * Gets the admin ID.
     * 
     * @return the admin ID
     */
    public String getAdminId() {
        return adminId;
    }
    
    /**
     * Sets the admin ID.
     * 
     * @param adminId the admin ID to set
     */
    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }
    
    /**
     * Gets the username.
     * 
     * @return the username
     */
    public String getUsername() {
        return username;
    }
    
    /**
     * Sets the username.
     * 
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }
    
    /**
     * Gets the password (hashed).
     * 
     * @return the hashed password
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * Sets the password (should be hashed before setting).
     * 
     * @param password the hashed password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    /**
     * Gets the full name.
     * 
     * @return the full name
     */
    public String getFullName() {
        return fullName;
    }
    
    /**
     * Sets the full name.
     * 
     * @param fullName the full name to set
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    @Override
    public String toString() {
        return "Admin{" +
                "adminId='" + adminId + '\'' +
                ", username='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                '}';
    }
}
