package rushhours;

import rushhours.io.LoadFile;
import rushhours.model.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            State state = LoadFile.loadFromFile("input.txt");
            Board board = state.getBoard();
            PieceMap pieces = state.getPieces();

            // Print board dimensions
            System.out.println("Board size: " + board.getHeight() + " x " + board.getWidth());

            // Print goal position
            Coordinate goal = board.getGoalCoordinate();
            System.out.println("Goal at: (" + goal.getX() + ", " + goal.getY() + ")");

            // Print board grid
            System.out.println("Board:");
            char[][] grid = board.getGrid();
            for (int y = 0; y < board.getHeight(); y++) {
                for (int x = 0; x < board.getWidth(); x++) {
                    System.out.print(grid[y][x]);
                }
                System.out.println();
            }

            // Print pieces
            System.out.println("Pieces:");
            for (char id : pieces.getKeys()) {
                Piece p = pieces.getPieceInfo(id);
                System.out.println("ID: " + id + ", Pos: (" + p.getCoordinate().getX() + ", " + p.getCoordinate().getY() + "), Len: " + p.getLength() + ", Ori: " + p.getOrientation());
            }

        } catch (IOException e) {
            System.out.println("Error reading input.txt: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}