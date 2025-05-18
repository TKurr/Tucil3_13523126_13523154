package com.control;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Stack;

import com.algorithm.BestFirstSearch;
import com.model.Board;
import com.model.PieceMap;
import com.model.State;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class MainController {
    @FXML
    private TextField widthField, heightField, goalField;
    @FXML
    private TextArea boardInput;
    @FXML
    private Label resultLabel, moveLabel, timeLabel;
    @FXML
    private Button solveButton, saveButton;
    @FXML
    private GridPane boardGrid;
    @FXML
    private ComboBox<String> algorithmBox;
    @FXML
    private ComboBox<String> heuristicBox;


    private Board board;
    private PieceMap pieceMap;
    private List<State> solutionSteps;
    private final Map<Character, Color> pieceColors = new HashMap<>();
    private final Random random = new Random();

    @FXML
    public void initialize() {
        algorithmBox.setItems(FXCollections.observableArrayList(
            "Uninformed Cost Search",
            "Best First Search",
            "A* Search"
        ));

        heuristicBox.setItems(FXCollections.observableArrayList(
            "Distance To Goal",
            "Number Of Blocking Piece",
            "Distance To Goal & Number Of Blocking Piece"
        ));
    }


    @FXML
    private void handleDrawBoard() {
        try {
            int width = Integer.parseInt(widthField.getText().trim());
            int height = Integer.parseInt(heightField.getText().trim());
            String[] goalParts = goalField.getText().trim().split(",");
            int goalX = Integer.parseInt(goalParts[1].trim());
            int goalY = Integer.parseInt(goalParts[0].trim());

            String[] lines = boardInput.getText().trim().split("\\n");
            if (lines.length != height) {
                resultLabel.setText("Height mismatch with input.");
                return;
            }

            char[][] grid = new char[height][width];
            for (int i = 0; i < height; i++) {
                String line = lines[i];
                if (line.length() != width) {
                    resultLabel.setText("Width mismatch at line " + (i + 1));
                    return;
                }
                for (int j = 0; j < width; j++) {
                    grid[i][j] = line.charAt(j);
                }
            }

            board = new Board(height, width);
            pieceMap = new PieceMap(); 
            drawBoard(grid);
            resultLabel.setText("Board loaded!");
        } catch (Exception e) {
            resultLabel.setText("Error parsing input: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void drawBoard(char[][] grid) {
        boardGrid.getChildren().clear();
        int rows = grid.length;
        int cols = grid[0].length;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                drawCell(row, col, grid[row][col]);
            }
        }
    }

    private void drawCell(int row, int col, char pieceChar) {
        StackPane cell = new StackPane();
        Rectangle rect = new Rectangle(50, 50);
        rect.setStroke(Color.BLACK);

        if (pieceChar == '.') {
            rect.setFill(Color.LIGHTGRAY);
        } else {
            pieceColors.putIfAbsent(pieceChar, getRandomColor());
            rect.setFill(pieceColors.get(pieceChar));
        }

        Label label = new Label(String.valueOf(pieceChar));
        cell.getChildren().addAll(rect, label);
        boardGrid.add(cell, col, row);
    }

    private Color getRandomColor() {
        return Color.color(random.nextDouble(), random.nextDouble(), random.nextDouble());
    }

    @FXML
    private void solvePuzzle() {
        if (board == null || pieceMap == null) {
            resultLabel.setText("Draw board first!");
            return;
        }

        solveButton.setDisable(true);
        resultLabel.setText("Solving...");

        new Thread(() -> {
            try {
                State startState = new State(board, pieceMap);

                long startTime = System.currentTimeMillis();

                Stack<String> steps = BestFirstSearch.solve(startState, "Distance To Goal & Number Of Blocking Piece");

                long elapsedTime = System.currentTimeMillis() - startTime;

                Platform.runLater(() -> {
                    if (steps != null && !steps.isEmpty()) {
                        resultLabel.setText("Solved!");
                        moveLabel.setText("Moves: " + steps.size());
                        timeLabel.setText("Time: " + elapsedTime + " ms");

                        State current = startState;
                        for (String move : steps) {
                            current = current.applyMove(move);
                        }
                        drawBoard(current.getBoard().getGrid());


                        drawBoard(current.getBoard().getGrid()); 
                        saveButton.setDisable(false);
                    } else {
                        resultLabel.setText("No solution found.");
                        moveLabel.setText("Moves: 0");
                        timeLabel.setText("Time: " + elapsedTime + " ms");
                        saveButton.setDisable(true);
                    }

                    solveButton.setDisable(false);
                });
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> resultLabel.setText("Error: " + e.getMessage()));
            }
        }).start();
    }
}