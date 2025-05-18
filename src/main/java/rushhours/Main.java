package rushhours;

import rushhours.io.*;
import rushhours.model.*;
import rushhours.model.Colors.*;
import rushhours.algorithm.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

public class Main {

    private static volatile boolean keepPrinting = true;
    private static ColorMap colors = LoadFile.getColorMap();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            while (true) {
                // Input file
                clearScreen();
                System.out.print("Input file: test/");
                String filename = scanner.nextLine().trim();
                State initialState = LoadFile.loadFromFile(filename);
                System.out.println("\n" + initialState.getBoard().coloredStringBoard(colors));

                // Input Algorithm
                System.out.println("Choose algorithm:");
                System.out.println("1. UCS");
                System.out.println("2. A*");
                System.out.println("3. Best First Search");
                System.out.print("Enter choice (1/2/3): ");
                int algoChoice = Integer.parseInt(scanner.nextLine().trim());

                String heuristicType = ":V";

                if (algoChoice == 2 || algoChoice == 3) {
                    System.out.println("\nChoose heuristic:");
                    System.out.println("1. Number of blocking piece heuristic");
                    System.out.println("2. Distance to exit heuristic");
                    System.out.println("3. Number of blocking and distance heuristic");
                    System.out.print("Enter choice (1/2/3): ");
                    int heuristicChoice = Integer.parseInt(scanner.nextLine().trim());

                    switch (heuristicChoice) {
                        case 1:
                            heuristicType = "Blocking Piece";
                            break;
                        case 2:
                            heuristicType = "Distance To Goal";
                            break;
                        case 3:
                            heuristicType = "Blocking Piece + Distance";
                            break;
                        default:
                            throw new IllegalArgumentException("Invalid heuristic choice.");
                    }
                }

                // Delay Frame 
                int delay = 100; // default
                while (true) {
                    System.out.print("Enter delay per frame (ms): ");
                    String delayInput = scanner.nextLine().trim();
                    try {
                        delay = Integer.parseInt(delayInput);
                        if (delay >= 0) break;
                        else throw new IllegalArgumentException("Invalid negative delay value.");
                    } catch (NumberFormatException e) {
                        System.out.println(e.getMessage());
                    }
                }

                long startTime = System.currentTimeMillis();
                int visitedNode = 0;

                // Algo 
                State solvedState = null;
                if (algoChoice == 1) { // UCS
                    UCS solver = new UCS();
                    solvedState = solver.solve(initialState);
                    visitedNode = solver.getVisitedNode();
                } 
                else if (algoChoice == 2) { // A*
                    AStar solver = new AStar();
                    solvedState = solver.solve(initialState, heuristicType);
                    visitedNode = solver.getVisitedNode();
                } 
                else if (algoChoice == 3) { // BestFirstSearch
                    BestFirstSearch solver = new BestFirstSearch();
                    solvedState = solver.solve(initialState, heuristicType);
                    visitedNode = solver.getVisitedNode();
                } 
                else {
                    System.out.println("Invalid algorithm choice.");
                    return;
                }

                long endTime = System.currentTimeMillis(); 

                if (solvedState == null) {
                    System.out.println("No solution found.");
                    return;
                }

                // Output
                Stack<String> outputFrame = solvedState.outputFrames(solvedState, colors);
                Stack<String> originalFrame = solvedState.originalFrames(solvedState);
                clearScreen();
                System.out.println("\n\n\nPress ENTER to stop");
                delay(1350);
                final int finalDelay = delay;
                keepPrinting = true;
                Thread printer = new Thread(() -> {
                    while (keepPrinting) {
                        printResultWithDelay(outputFrame, finalDelay);
                    }
                });
                printer.start();
                scanner.nextLine();
                keepPrinting = false;
                printer.join();

                System.out.println("Moves: " + outputFrame.size());
                System.out.println("Execution Time: " + (endTime - startTime) + " ms");
                System.out.println("Total Visited Node: " + visitedNode);

                System.out.print("\nSave wher? : test/");
                String saveFile = scanner.nextLine().trim();
                WriteFile.saveFile(saveFile, originalFrame);
                
                System.out.println("Saved!!");

                System.out.print("\nAgain? \n(Y/N) :");
                String againInput = scanner.nextLine().trim();
                if (!againInput.equalsIgnoreCase("Y")) {
                    break;
                }
            }

        } catch (IOException e) {
            System.out.println("Error reading input file: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    static void clearScreen() {
        String os = System.getProperty("os.name").toLowerCase();
        String env = System.getenv("WSL_DISTRO_NAME"); 

        try {
            if (os.contains("windows") && env == null) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            for (int i = 0; i < 50; i++) System.out.println();
        }
    }

    static void printResultWithDelay(Stack<String> outputFrame, int delay) {
        List<String> framesCopy = new ArrayList<>(outputFrame); 

        for (int idx = framesCopy.size() - 1; idx >= 0 && keepPrinting; idx--) {
            clearScreen();
            System.out.print(framesCopy.get(idx));
            try {
                Thread.sleep(delay);
            } catch (InterruptedException ex) {;
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
    
    static void delay(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}