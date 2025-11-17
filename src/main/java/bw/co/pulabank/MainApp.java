package bw.co.pulabank;

import bw.co.pulabank.service.InterestScheduler;
import bw.co.pulabank.util.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * PULA BANK BOTSWANA – Main Application
 * Proudly built in Gaborone, Botswana
 * Launch date: 17 November 2025
 */
public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Set up scene manager
        SceneManager.setStage(primaryStage);

        // Load login screen
        SceneManager.loadScene("/fxml/login.fxml", "Welcome to Pula Bank");

        // Window settings
        primaryStage.setTitle("Pula Bank Botswana • Secure Banking");
        primaryStage.setMinWidth(1280);
        primaryStage.setMinHeight(720);
        primaryStage.centerOnScreen();
        primaryStage.show();

        // Start automatic monthly interest (runs every 1st at 02:00 AM Botswana time)
        InterestScheduler.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}