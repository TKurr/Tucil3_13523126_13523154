package com.control;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Memuat file FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/control/primary.fxml"));
        Parent root = loader.load();

        // Set judul dan ukuran awal window
        primaryStage.setTitle("Rush Hour Solver");
        primaryStage.setScene(new Scene(root, 800, 600)); // Ukuran default bisa disesuaikan
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
