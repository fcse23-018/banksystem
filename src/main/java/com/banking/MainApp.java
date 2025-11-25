package com.banking;

import com.banking.util.DatabaseConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;

/**
 * Main entry point for the Banking System JavaFX application.
 * Initializes the database and launches the login view.
 * 
 * CSE202 Assignment - Part B
 * Student: Donovan Ntsima (FCSE23-018)
 * 
 * @author Donovan Ntsima
 * @version 1.0
 */
public class MainApp extends Application {
    
    /**
     * Starts the JavaFX application.
     * Loads the login view as the initial scene.
     * 
     * @param primaryStage the primary stage for this application
     * @throws Exception if FXML loading fails
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoginView.fxml"));
        Parent root = loader.load();
        
        Scene scene = new Scene(root, 450, 400);
        
        primaryStage.setTitle("Banking System - Login");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
    
    /**
     * Initializes the application.
     * Sets up the database connection and creates tables if they don't exist.
     * 
     * @throws Exception if initialization fails
     */
    @Override
    public void init() throws Exception {
        super.init();
        
        System.out.println("=".repeat(60));
        System.out.println("Banking System - CSE202 Assignment Part B");
        System.out.println("Student: Donovan Ntsima (FCSE23-018)");
        System.out.println("=".repeat(60));
        System.out.println();
        
        try {
            System.out.println("Initializing database connection...");
            
            if (DatabaseConnection.testConnection()) {
                System.out.println("✓ Database connection successful!");
                
                System.out.println("Initializing database schema...");
                DatabaseConnection.initializeDatabase();
                System.out.println("✓ Database schema initialized!");
                
            } else {
                System.err.println("✗ Failed to connect to database!");
                System.err.println("Please check your database credentials and connection.");
            }
            
        } catch (SQLException e) {
            System.err.println("✗ Database initialization error: " + e.getMessage());
            System.err.println("The application will continue, but database operations may fail.");
        }
        
        System.out.println();
        System.out.println("Application initialized successfully.");
        System.out.println("=".repeat(60));
        System.out.println();
    }
    
    /**
     * Cleanup method called when the application stops.
     * Closes the database connection.
     * 
     * @throws Exception if cleanup fails
     */
    @Override
    public void stop() throws Exception {
        super.stop();
        
        System.out.println();
        System.out.println("Shutting down application...");
        DatabaseConnection.closeConnection();
        System.out.println("Application closed successfully.");
    }
    
    /**
     * Main method to launch the JavaFX application.
     * 
     * @param args command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
