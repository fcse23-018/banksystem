package com.bankingsystem;

import com.bankingsystem.view.LoginView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        LoginView loginView = new LoginView(primaryStage);
        Scene scene = new Scene(loginView, 400, 300);
        primaryStage.setTitle("Banking System Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
