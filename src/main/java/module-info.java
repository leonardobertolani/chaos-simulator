module com.example.chaossimulator {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.example.chaossimulator to javafx.fxml;
    exports com.example.chaossimulator;
}