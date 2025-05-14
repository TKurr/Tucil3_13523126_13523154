package com.control;

import com.logic.LoadFile;
import com.logic.LoadFile.Data;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.io.IOException;

public class MainController {

    @FXML
    private TextArea boardArea;

    @FXML
    public void initialize() {
        try {
            Data data = LoadFile.parse("src/main/java/com/logic/input.txt");
            StringBuilder sb = new StringBuilder();
            for (int r = 1; r <= data.rows; r++) {
                for (int c = 1; c <= data.cols; c++) {
                    sb.append(data.board[r][c]);
                }
                sb.append("\n");
            }
            boardArea.setText(sb.toString());
        } catch (IOException e) {
            boardArea.setText("Gagal memuat file: " + e.getMessage());
        }
    }
}
