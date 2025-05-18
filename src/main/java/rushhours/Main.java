package rushhours;

import rushhours.io.LoadFile;
import rushhours.model.*;
import rushhours.model.Colors.*;
import rushhours.algorithm.UCS;
import rushhours.algorithm.AStar; 
import rushhours.algorithm.BestFirstSearch;

import java.io.IOException;
import java.util.Scanner;
import java.util.Stack;

public class Main {

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

                // Algo 
                State solvedState = null;
                if (algoChoice == 1) { // UCS
                    solvedState = UCS.solve(initialState);
                } 
                else if (algoChoice == 2) { // A*
                    solvedState = AStar.solve(initialState, heuristicType);;
                } 
                else if (algoChoice == 3) { // BestFirstSearch
                    solvedState = BestFirstSearch.solve(initialState, heuristicType);
                } 
                else {
                    System.out.println("Invalid algorithm choice.");
                    return;
                }

                if (solvedState == null) {
                    System.out.println("No solution found.");
                    return;
                }

                // Output
                Stack<String> frames = solvedState.outputFrames(solvedState, colors);
                printResultWithDelay(frames, delay);
                System.out.println("Total Costs: " + solvedState.getPastCost());

                System.out.print("\n\nAgain? \n(Y/N) :");
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
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    static void printResultWithDelay(Stack<String> frames, int delay) {
        int i = 0;
        while (!frames.isEmpty()) {
            clearScreen();
            System.out.println("Step: " + (++i));
            System.out.println(frames.pop());
            System.out.println("----------");
            try {
                Thread.sleep(delay);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }
}