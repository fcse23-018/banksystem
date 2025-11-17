module bw.co.pulabank {
    requires javafx.controls;
    requires javafx.fxml;
    requires supabase.java;

    opens bw.co.pulabank to javafx.fxml;
    exports bw.co.pulabank;
}