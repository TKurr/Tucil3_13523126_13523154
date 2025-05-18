module tucil {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens com.control to javafx.fxml;
    exports com.control;
    exports com.algorithm;
    exports com.io;
    exports com.model;
}
