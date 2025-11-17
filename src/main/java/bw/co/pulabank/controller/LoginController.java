package bw.co.pulabank.controller;

import bw.co.pulabank.model.User;
import bw.co.pulabank.model.UserRole;
import bw.co.pulabank.service.AuthService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Text errorText;

    private final AuthService authService = new AuthService();

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorText.setText("Username and password are required.");
            return;
        }

        try {
            User user = authService.login(username, password);
            if (user != null) {
                // Navigate to the appropriate dashboard
                navigateToDashboard(user.getRole());
            } else {
                errorText.setText("Invalid username or password.");
            }
        } catch (Exception e) {
            errorText.setText("An error occurred during login.");
            e.printStackTrace();
        }
    }

    private void navigateToDashboard(UserRole role) {
        try {
            String fxmlPath = "";
            if (role == UserRole.CUSTOMER) {
                fxmlPath = "/fxml/CustomerDashboard.fxml";
            } else if (role == UserRole.STAFF) {
                fxmlPath = "/fxml/StaffDashboard.fxml";
            }

            Parent dashboard = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlPath)));
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(dashboard));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
