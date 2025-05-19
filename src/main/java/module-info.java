module rushhours {
    requires transitive javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    exports rushhours.control;
    exports rushhours.model.Colors;

    opens rushhours.control to javafx.fxml;
    exports rushhours.algorithm;
    exports rushhours.io;
    exports rushhours.model;
}