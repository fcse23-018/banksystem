package com.bankingsystem.view;

import com.bankingsystem.controller.LoginController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginView extends VBox {

    private final LoginController controller;

    public LoginView(Stage stage) {
        this.controller = new LoginController(this, stage);

        setAlignment(Pos.CENTER);
        setSpacing(20);
        setPadding(new Insets(25));

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);

        Label userName = new Label("User Name:");
        grid.add(userName, 0, 1);

        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 1);

        Label pw = new Label("Password:");
        grid.add(pw, 0, 2);

        PasswordField pwBox = new PasswordField();
        grid.add(pwBox, 1, 2);

        Button loginButton = new Button("Sign in");
        loginButton.setOnAction(e -> controller.handleLogin(userTextField.getText(), pwBox.getText()));

        getChildren().addAll(grid, loginButton);
    }
}
