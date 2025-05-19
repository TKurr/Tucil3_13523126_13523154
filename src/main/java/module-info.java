module tucil {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens rushhours.control to javafx.fxml;
    exports rushhours.control;
    exports rushhours.algorithm;
    exports rushhours.io;
    exports rushhours.model;
}
