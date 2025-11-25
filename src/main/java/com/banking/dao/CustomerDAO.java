package com.banking.dao;

import com.banking.model.Customer;
import com.banking.util.DatabaseConnection;
import com.banking.util.PasswordUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Customer entity.
 * Handles all database operations for customers.
 * Implements CRUD operations and authentication.
 * 
 * @author Donovan Ntsima (FCSE23-018)
 * @version 1.0
 */
public class CustomerDAO {
    
    /**
     * Creates a new customer in the database.
     * Hashes the password before storage.
     * 
     * @param customer the customer to create
     * @return true if creation successful, false otherwise
     */
    public boolean createCustomer(Customer customer) {
        String sql = "INSERT INTO CUSTOMERS (customer_id, first_name, surname, address, password) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, customer.getCustomerID());
            pstmt.setString(2, customer.getFirstName());
            pstmt.setString(3, customer.getSurname());
            pstmt.setString(4, customer.getAddress());
            pstmt.setString(5, customer.getPassword());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error creating customer: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Retrieves a customer by their ID.
     * 
     * @param customerID the customer ID to search for
     * @return the customer if found, null otherwise
     */
    public Customer getCustomerById(String customerID) {
        String sql = "SELECT * FROM CUSTOMERS WHERE customer_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, customerID);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractCustomerFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving customer: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Retrieves all customers from the database.
     * 
     * @return list of all customers
     */
    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM CUSTOMERS ORDER BY customer_id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                customers.add(extractCustomerFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving customers: " + e.getMessage());
        }
        
        return customers;
    }
    
    /**
     * Updates a customer's information in the database.
     * 
     * @param customer the customer with updated information
     * @return true if update successful, false otherwise
     */
    public boolean updateCustomer(Customer customer) {
        String sql = "UPDATE CUSTOMERS SET first_name = ?, surname = ?, address = ? WHERE customer_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, customer.getFirstName());
            pstmt.setString(2, customer.getSurname());
            pstmt.setString(3, customer.getAddress());
            pstmt.setString(4, customer.getCustomerID());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating customer: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Deletes a customer from the database.
     * Cascades to delete all associated accounts and transactions.
     * 
     * @param customerID the ID of the customer to delete
     * @return true if deletion successful, false otherwise
     */
    public boolean deleteCustomer(String customerID) {
        String sql = "DELETE FROM CUSTOMERS WHERE customer_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, customerID);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting customer: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Authenticates a customer using their ID and password.
     * 
     * @param customerID the customer ID
     * @param plainPassword the plain text password
     * @return the customer if authentication successful, null otherwise
     */
    public Customer authenticate(String customerID, String plainPassword) {
        Customer customer = getCustomerById(customerID);
        
        if (customer != null && PasswordUtil.checkPassword(plainPassword, customer.getPassword())) {
            return customer;
        }
        
        return null;
    }
    
    /**
     * Checks if a customer ID already exists in the database.
     * 
     * @param customerID the customer ID to check
     * @return true if exists, false otherwise
     */
    public boolean customerExists(String customerID) {
        String sql = "SELECT COUNT(*) FROM CUSTOMERS WHERE customer_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, customerID);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking customer existence: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Extracts a Customer object from a ResultSet.
     * 
     * @param rs the ResultSet containing customer data
     * @return the extracted Customer object
     * @throws SQLException if extraction fails
     */
    private Customer extractCustomerFromResultSet(ResultSet rs) throws SQLException {
        return new Customer(
            rs.getString("customer_id"),
            rs.getString("first_name"),
            rs.getString("surname"),
            rs.getString("address"),
            rs.getString("password")
        );
    }
}
