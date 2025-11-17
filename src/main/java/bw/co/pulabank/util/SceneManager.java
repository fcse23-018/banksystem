package bw.co.pulabank.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class SceneManager {
    private static Stage primaryStage;

    public static void setStage(Stage stage) {
        primaryStage = stage;
    }

    public static void loadScene(String fxmlPath, String title) {
        if (primaryStage == null) {
            throw new IllegalStateException("Stage not initialized. Call setStage() first.");
        }
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add("/css/material-theme.css");
            primaryStage.setScene(scene);
            primaryStage.setTitle("Pula Bank • " + title);
            primaryStage.centerOnScreen();
        } catch (IOException e) {
            AlertHelper.showError("Scene Error", "Could not load " + fxmlPath);
            e.printStackTrace();
        }
    }
}