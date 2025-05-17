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

                // ada solo 'K' dipaling atas or bawah
                if(y == 0 || y == height-1){ //paling atas ato paling bawah board
                    if(lengthExcludingWhitespace(row) == 1){ // length == 1 , udah pasti ada goal
                        if(goalExist(row)){
                            int xGoal = getGoalWidthIndexPosition(row);
                            rowGrid[xGoal+1] = 'K';
                            board.setGoal(xGoal+1, y);
                        }
                        for(int i = 0 ; i < width; i++) {
                            rowGrid[i] = (rowGrid[i] == 'K') ? 'K' : '*';
                        }
                        line = readBoard(br); // next line kalo emang ada additoinal line yang length nya 1 excluding white space
                    } else {
                        for(int i = 0 ; i < width; i++) { // no next line 
                            rowGrid[i] = '*';
                        }
                    }
                    grid[y] = rowGrid;
                    continue;
                }
                //==================================================================================

                rowGrid = adjustMidRow(row,width);
                int xGoal = -1;
                if(goalExist(rowGrid)) {
                    if(row[0] != 'K') {
                        xGoal = 0;
                    } 
                    else {
                        xGoal = width-1;
                    }
                    board.setGoal(xGoal,y);
                    rowGrid[xGoal] = 'K';
                }
                grid[y] = rowGrid;
                line = readBoard(br);
            }

            for (int y = 0; y < height; y++) {
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
                    if (id == '.' || processed.contains(id)) continue;

                    // Determine orientation and length
                    int length = 1;
                    char orientation = 'H';
                    // Check right for horizontal
                    if (x + 1 < width && grid[y][x + 1] == id) {
                        orientation = 'H';
                        int tx = x + 1;
                        while (tx < width && grid[y][tx] == id) {
                            length++;
                            tx++;
                        }
                    }
                    // Check down for vertical
                    else if (y + 1 < height && grid[y + 1][x] == id) {
                        orientation = 'V';
                        int ty = y + 1;
                        while (ty < height && grid[ty][x] == id) {
                            length++;
                            ty++;
                        }
                    }
                    // Create and add the piece
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
    private static char[] adjustMidRow(char[] row, int width) {
        char[] result = new char[width];
        int j = 0;
        for (int i = 0; i < width; i++) {
            if (i == 0 || i == width - 1) {
                result[i] = '*';
            } else {
                // Skip spaces
                while (j < row.length && row[j] == ' ') {
                    j++;
                }
                // Fill with '.' if out of input, else copy (skip 'K')
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
        }
        return result;
    }

    //both
    private static boolean goalExist(char[] row) {
        for (char c : row) {
            if (c == 'K') return true;
        }
        return false;
    }

    //both
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