
package bw.co.pulabank;

import bw.co.pulabank.controller.LoginController;
import bw.co.pulabank.service.InterestScheduler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainApp extends Application {

    private static final String SUPABASE_URL = "https://your-supabase-url.supabase.co";
    private static final String SUPABASE_KEY = "your-supabase-key";

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Initialize Supabase client
        SupabaseClient.initialize(SUPABASE_URL, SUPABASE_KEY);

        // Start interest calculation scheduler
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(new InterestScheduler(), 0, 24, TimeUnit.HOURS);

        // Load and show the login scene
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/Login.fxml")));
        Scene scene = new Scene(root);
        primaryStage.setTitle("Pula Bank");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
