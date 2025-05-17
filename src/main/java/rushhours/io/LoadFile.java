package rushhours.io;

import java.util.HashSet;
import rushhours.model.*;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;

public class LoadFile {

    private static final String TEST_FOLDER = "test/";

    public static State loadFromFile(String filename) throws IOException {
        String path = TEST_FOLDER + filename;
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {

            // Width , Height
            String[] wh = readNonEmptyLine(br).split("\\s+");
            int width = Integer.parseInt(wh[0])+2;
            int height = Integer.parseInt(wh[1])+2;
            Board board = new Board(height, width);

            // n piece
            int nPiece = Integer.parseInt(readNonEmptyLine(br));

            // Board
            String line = readBoard(br);
            char[][] grid = new char[height][width];
            for (int y = 0; y < height; y++) {
                char[] rowGrid = new char[width];
                char[] row = line.toCharArray();

                if(y == 0 || y == height-1){ 
                    if(lengthExcludingWhitespace(row) == 1){ 
                        if(goalExist(row)){
                            int xGoal = getGoalWidthIndexPosition(row);
                            rowGrid[xGoal+1] = 'K';
                            board.setGoal(xGoal+1, y);
                        }
                        for(int i = 0 ; i < width; i++) {
                            rowGrid[i] = (rowGrid[i] == 'K') ? 'K' : '*';
                        }
                        line = readBoard(br); 
                    } else {
                        for(int i = 0 ; i < width; i++) { 
                            rowGrid[i] = '*';
                        }
                    }
                    grid[y] = rowGrid;
                    // System.out.print(y + " : ");
                    // System.out.println(grid[y]);
                    continue;
                }

                rowGrid = adjustMidRow(row,width, y, board);
                grid[y] = rowGrid;
                line = readBoard(br);
                // System.out.print(y + " dis dude : ");
                // System.out.println(grid[y]);
                if(line == null) {
                    char[] newGrid = new char[width];
                    for(int i = 0 ; i < width ; i++) {
                        newGrid[i] = '*';
                    }
                    grid[++y] = newGrid;
                    // System.out.print("Triggered with value ");
                    // System.out.println(newGrid);
                    break;
                }
            }

            for (int y = 0; y < height; y++) {
                System.out.println(grid[y]);
                for (int x = 0; x < width; x++) {
                    board.setCell(x, y, grid[y][x]);
                }
            }

            // Pieces
            PieceMap pieces = new PieceMap();
            HashSet<Character> processed = new HashSet<>();
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    char id = grid[y][x];
                    if (!Character.isLetter(id) || id == 'K' || processed.contains(id)) continue;
                    int length = 1;
                    char orientation = 'H';
                    if (x + 1 < width && grid[y][x + 1] == id) {
                        orientation = 'H';
                        int tx = x + 1;
                        while (tx < width && grid[y][tx] == id) {
                            length++;
                            tx++;
                        }
                    }
                    else if (y + 1 < height && grid[y + 1][x] == id) {
                        orientation = 'V';
                        int ty = y + 1;
                        while (ty < height && grid[ty][x] == id) {
                            length++;
                            ty++;
                        }
                    }
                    Coordinate coord = new Coordinate();
                    coord.setX(x);
                    coord.setY(y);
                    Piece piece = new Piece(id, length, coord, orientation);
                    pieces.addPiece(id, piece);
                    processed.add(id);
                }
            }
            return new State(board, pieces);
        }
    }

    private static String readNonEmptyLine(BufferedReader br) throws IOException {
        String line;
        do {
            line = br.readLine();
            if (line == null) return null; 
            line = line.trim();
        } while (line.isEmpty());
        return line;
    }

    private static String readBoard(BufferedReader br) throws IOException {
        String line;
        do {
            line = br.readLine();
            if (line == null) return null; 
        } while (line.isEmpty());
        return line;
    }

    //mid
    private static char[] adjustMidRow(char[] row, int width, int y, Board board) {
        char[] result = new char[width];
        int j = 0;
        result[0] = '*';
        result[width - 1] = '*';
        for (int i = 1; i < width - 1; i++) {
            while (j < row.length && row[j] == ' ') j++;
            if (j >= row.length) {
                result[i] = '.';
            } else if (row[j] != 'K') {
                result[i] = row[j];
                j++;
            } else {
                result[i] = '.';
                j++;
            }
        }
        if (row.length > 0 && row[0] == 'K') {
            result[0] = 'K';
            board.setGoal(0,y);
        }
        int lastNonSpace = row.length - 1;
        while (lastNonSpace >= 0 && row[lastNonSpace] == ' ') lastNonSpace--;
        if (lastNonSpace >= 0 && row[lastNonSpace] == 'K') {
            result[width - 1] = 'K';
            board.setGoal(width-1, y);
        }

        return result;
    }

    private static boolean goalExist(char[] row) {
        for (char c : row) {
            if (c == 'K') return true;
        }
        return false;
    }

    private static int getGoalWidthIndexPosition(char[] row) { 
        for (int i = 0; i < row.length; i++) {
            if (row[i] == 'K') return i;
        }
        return -1; 
    }

    private static int lengthExcludingWhitespace(char[] row) {
        int count = 0;
        for (char c : row) {
            if (!Character.isWhitespace(c)) count++;
        }
        return count;
    }
}