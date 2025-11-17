module bw.co.pulabank {
    // JavaFX
    requires javafx.controls;
    requires javafx.fxml;

    // External libraries
    requires com.jfoenix;        // Beautiful Material Design controls
    requires java.sql;           // Database access
    requires lombok;             // Clean code (getters/setters)

    // Open controllers package to FXML
    opens bw.co.pulabank.controller to javafx.fxml;

    // Export main package
    exports bw.co.pulabank;
}