package rushhours.control;

import java.io.File;
import java.util.List;
import java.util.Stack;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import rushhours.algorithm.Astar;
import rushhours.algorithm.BestFirstSearch;
import rushhours.algorithm.UCS;
import rushhours.io.LoadFile;
import rushhours.io.WriteFile;
import rushhours.model.Colors.ColorMap;
import rushhours.model.State;

public class MainController {
    @FXML
    private Label resultLabel, moveLabel, timeLabel, nodeLabel, filenameLabel;
    @FXML
    private Button solveButton, saveButton, replayButton;
    @FXML
    private GridPane boardGrid;
    @FXML
    private ComboBox<String> algorithmBox;
    @FXML
    private ComboBox<String> heuristicBox;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private TextField saveFileName, delayFrame;
    @FXML 
    private VBox resultBox;
    
    private State state;
    private ColorMap colorMap;
    private State latestSolvedState;


    @FXML
    public void initialize() {
        algorithmBox.setItems(FXCollections.observableArrayList(
            "Uniform Cost Search",
            "Best First Search",
            "A* Search"
        ));

        heuristicBox.setItems(FXCollections.observableArrayList(
            "Distance To Goal",
            "Number Of Blocking Piece",
            "Distance To Goal & Number Of Blocking Piece"
        ));

        scrollPane.setVisible(false);
        saveButton.setVisible(false);
        saveButton.setDisable(true);
        replayButton.setVisible(false);
        replayButton.setDisable(true);
        heuristicBox.setDisable(true);
        filenameLabel.setVisible(false);
        saveFileName.setVisible(false);
        
        checkAlgo();
    }

    @FXML
    private void checkAlgo() {
        algorithmBox.valueProperty().addListener((obs, oldVal, newVal) -> {
        if ("Uniform Cost Search".equals(newVal)) {
            heuristicBox.setDisable(true);
        } else {
            heuristicBox.setDisable(false);
        }
        });
    }
    

    @FXML
    private void handleChooseFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Text Files", "*.txt"),
            new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            try {
                state = LoadFile.loadFromFile(selectedFile.getAbsolutePath());
                colorMap = new rushhours.model.Colors.ColorMap();
                colorMap.mapColorToPieces(state.getPieces());
                scrollPane.setVisible(true);
                drawBoard(state.getBoard().getGrid()); 
                solveButton.setDisable(false);
                resultLabel.setText("File berhasil diload!");
                
            } catch (Exception e) {
                e.printStackTrace(); 
                resultLabel.setText("Failed to read file: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            }
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

        if (pieceChar == 'K' | pieceChar == '.') {
            rect.setFill(Color.WHITE);
        } else if (pieceChar == '*'){
            rect.setFill(Color.BLACK);
        } else if (pieceChar == 'P'){
            rect.setFill(Color.RED);
        } else {
            Color fxColor = convertAnsiToFx(colorMap.getColor(pieceChar).getAnsiCode());
            rect.setFill(fxColor);
        }

        Label label = new Label();
        cell.getChildren().addAll(rect, label);
        boardGrid.add(cell, col, row);
    }

    private Color convertAnsiToFx(String ansiCode) {
        return switch (ansiCode) {
            case "\u001B[42;30m" -> Color.GREEN;
            case "\u001B[43;30m" -> Color.GOLD;
            case "\u001B[44;37m" -> Color.DODGERBLUE;
            case "\u001B[45;30m" -> Color.MAGENTA;
            case "\u001B[46;30m" -> Color.CYAN;
    
            case "\u001B[102;30m" -> Color.LIMEGREEN;
            case "\u001B[103;30m" -> Color.KHAKI;
            case "\u001B[104;30m" -> Color.LIGHTBLUE;
            case "\u001B[105;30m" -> Color.HOTPINK;
            case "\u001B[106;30m" -> Color.LIGHTCYAN;
    
            case "\u001B[1;42;30m" -> Color.DARKGREEN;
            case "\u001B[1;43;30m" -> Color.ORANGE;
            case "\u001B[1;44;37m" -> Color.ROYALBLUE;
            case "\u001B[1;45;30m" -> Color.DARKMAGENTA;
            case "\u001B[1;46;30m" -> Color.DARKCYAN;
    
            case "\u001B[1;102;30m" -> Color.GREENYELLOW;
            case "\u001B[1;103;30m" -> Color.YELLOW;
            case "\u001B[1;104;37m" -> Color.CORNFLOWERBLUE;
            case "\u001B[1;105;30m" -> Color.DEEPPINK;
            case "\u001B[1;106;30m" -> Color.AQUA;
    
            case "\u001B[100;30m" -> Color.DIMGRAY;
            case "\u001B[1;100;30m" -> Color.DARKGRAY;
            case "\u001B[1;100;37m" -> Color.SILVER;
            case "\u001B[1;100;97m" -> Color.LIGHTGRAY;
    
            default -> Color.GRAY;
        };
    }
    

        
    

    @FXML
    private void solvePuzzle() {
        solveButton.setDisable(true);
        resultLabel.setText("Mikir dulu....");

        new Thread(() -> {
            try {
                long startTime = System.currentTimeMillis();

                State startState = state;
                State steps;

                String algo = algorithmBox.getValue();
                String heur = heuristicBox.getValue();

                if (algo != "Uniform Cost Search"){
                    if (algo == null || heur == null){
                        Platform.runLater(() -> resultLabel.setText("Pilih algoritma dan/atau heuristik terlebih dahulu"));
                        solveButton.setDisable(false);
                        return;    
                    }
                } else if (delayFrame.getText() == null || delayFrame.getText().trim().isEmpty())  {
                    Platform.runLater(() -> resultLabel.setText("Isi dulu delay framesnya.."));  
                    solveButton.setDisable(false);
                    return;
                }

                final int totalNode;
                if (algo == "Best First Search"){
                    BestFirstSearch solver = new BestFirstSearch();
                    steps = solver.solve(startState, heur);
                    totalNode = solver.getVisitedNode();
                } else if (algo == "A* Search") {
                    Astar solver = new Astar();
                    steps = solver.solve(startState, heur);
                    totalNode = solver.getVisitedNode();
                } else if (algo == "Uniform Cost Search") {
                    UCS solver = new UCS();
                    steps = solver.solve(startState);
                    totalNode = solver.getVisitedNode();

                } else {
                    return;
                }

                long elapsedTime = System.currentTimeMillis() - startTime;
                latestSolvedState = steps;

                Platform.runLater(() -> resultLabel.setText("Solving.."));

                Platform.runLater(() -> {
                if (steps != null) {
                    List<State> stateSteps = State.getStatePath(steps);

                    int delayInMS = Integer.parseInt(delayFrame.getText());

                    final int[] index = {0};

                    PauseTransition pause = new PauseTransition(Duration.millis(delayInMS));
                    pause.setOnFinished(event -> {
                        if (index[0] < stateSteps.size()) {
                            drawBoard(stateSteps.get(index[0]).getBoard().getGrid());
                            index[0]++;

                            pause.playFromStart(); 
                        } else {
                            Platform.runLater(() -> resultLabel.setText("Solved!"));
                            Platform.runLater(() -> moveLabel.setText("Moves: " + (stateSteps.size()-1)));
                            Platform.runLater(() -> timeLabel.setText("Time: " + elapsedTime + " ms"));
                            Platform.runLater(() -> nodeLabel.setText("Nodes: " + totalNode));
                            saveButton.setVisible(true);
                            saveButton.setDisable(false);
                            solveButton.setDisable(false);
                            saveFileName.setVisible(true);
                            filenameLabel.setVisible(true);
                            replayButton.setVisible(true);
                            replayButton.setDisable(false);
                            resultBox.setVisible(true);
                        }
                    });

                    pause.play();
                } else {
                    Platform.runLater(() -> resultLabel.setText("No solution found."));
                    Platform.runLater(() -> moveLabel.setText("Moves: 0"));
                    Platform.runLater(() -> timeLabel.setText("Time: " + elapsedTime + " ms"));
                    Platform.runLater(() -> nodeLabel.setText("Nodes: " + totalNode));
                    saveButton.setDisable(true);
                    saveButton.setVisible(false);
                    solveButton.setDisable(false);
                    saveFileName.setVisible(false);
                    filenameLabel.setVisible(false);
                    replayButton.setVisible(false);
                    replayButton.setDisable(true);
                }
            });
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> resultLabel.setText("Error: " + e.getMessage()));
                solveButton.setDisable(true);
            }
        }).start();
    }

    @FXML
    private void replayAnimation() {
        List<State> stateSteps = State.getStatePath(latestSolvedState);
        int delayInMS = Integer.parseInt(delayFrame.getText());
        final int[] index = {0};

        PauseTransition pause = new PauseTransition(Duration.millis(delayInMS));
        pause.setOnFinished(event -> {
            if (index[0] < stateSteps.size()) {
                drawBoard(stateSteps.get(index[0]).getBoard().getGrid());
                index[0]++;
                pause.playFromStart();
            }
        });

        if (!stateSteps.isEmpty()) {
            drawBoard(stateSteps.get(0).getBoard().getGrid()); 
            index[0] = 1;
            pause.play();
        }
    }


    @FXML
    private void saveFileGUI(){
        Stack<String> output;
        output = latestSolvedState.originalFrames(latestSolvedState);
        
        WriteFile.saveFile(saveFileName.getText(), output);
        resultLabel.setText("File berhasil disimpan");
    }
}