package rushhours.io;

import java.awt.Point;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.nio.file.Paths;
import java.util.*;

public class LoadFile {
    public static class Data {
        public int rows, cols;
        public int numPieces;
        public char[][] board;
        public Map<Character, List<Point>> pieces = new HashMap<>();
        public List<Point> primaryPiecePos = new ArrayList<>();
        public Point exit;
    }

    public static Data parse(String filePath) throws IOException {
        Data data = new Data();
    
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String[] dimensions = reader.readLine().trim().split(" ");
            data.rows = Integer.parseInt(dimensions[0]);
            data.cols = Integer.parseInt(dimensions[1]);
    
            data.numPieces = Integer.parseInt(reader.readLine().trim());
    
            List<String> rawLines = new ArrayList<>();
            int maxCols = data.cols;
    
            for (int i = 0; i < data.rows; i++) {
                String line = reader.readLine();
                rawLines.add(line);
                maxCols = Math.max(maxCols, line.length());
            }
    
            String extraLine = reader.readLine();
            int extraKRow = -1, extraKCol = -1;
            if (extraLine != null) {
                for (int i = 0; i < extraLine.length(); i++) {
                    if (extraLine.charAt(i) == 'K') {
                        extraKRow = data.rows; 
                        extraKCol = i;
                        maxCols = Math.max(maxCols, i + 1);
                        break;
                    }
                }
            }
    
            data.cols = maxCols;
    
            char[][] paddedBoard = new char[data.rows + 2][data.cols + 2];
            for (int r = 0; r < paddedBoard.length; r++) {
                Arrays.fill(paddedBoard[r], '.');
            }
    
            for (int r = 0; r < data.rows; r++) {
                String line = rawLines.get(r);
                for (int c = 0; c < line.length(); c++) {
                    char ch = line.charAt(c);
                    if (ch == ' ') ch = '.';
    
                    paddedBoard[r + 1][c + 1] = ch;
    
                    if (ch == '.') continue;
    
                    if (ch == 'P') {
                        data.primaryPiecePos.add(new Point(r + 1, c + 1));
                    } else if (ch == 'K') {
                        data.exit = new Point(r + 1, c + 1);
                    } else {
                        data.pieces.computeIfAbsent(ch, k -> new ArrayList<>()).add(new Point(r + 1, c + 1));
                    }
                }
            }
    
            if (extraKRow != -1 && extraKCol != -1) {
                paddedBoard[extraKRow + 1][extraKCol + 1] = 'K';
                data.exit = new Point(extraKRow + 1, extraKCol + 1);
            }
    
            data.board = paddedBoard;
            Point p = data.primaryPiecePos.get(0);
            Point k = data.exit;    
            if (data.exit != null) {
                int er = data.exit.x;
                int ec = data.exit.y;
    
                if (k.y == p.y) {
                    for (int c = 0; c < paddedBoard[0].length; c++) {
                        if (paddedBoard[er][c] == '.') {
                            paddedBoard[er][c] = '*';
                        }
                    }
                } else {
                    for (int r = 0; r < paddedBoard.length; r++) {
                        if (paddedBoard[r][ec] == '.') {
                            paddedBoard[r][ec] = '*';
                        }
                    }
                }
            }
        }
        return data;
    }

    public static void printBoard(char[][] board, int rows, int cols) {
        for (int r = 1; r <= rows; r++) {
            for (int c = 1; c <= cols; c++) {
                System.out.print(board[r][c]);
            }
            System.out.println();
        }
    }
    
    public static void main(String[] args) {
        try {
            // Build file path relative to the project root "TUCIL3"
            String filePath = Paths.get(System.getProperty("user.dir"), "test", "miau.txt").toString();
            Data data = parse(filePath);
    
            System.out.println("Ukuran papan: " + data.rows + " x " + data.cols);
            System.out.println("Jumlah piece biasa: " + data.numPieces);
            System.out.println("Papan:");
            for (char[] row : data.board) {
                System.out.println(new String(row));
            }
    
            System.out.println("\nPrimary Piece P:");
            for (Point p : data.primaryPiecePos) {
                System.out.println("(" + p.x + ", " + p.y + ")");
            }
    
            if (data.exit != null) {
                System.out.println("\nExit Position K: (" + data.exit.x + ", " + data.exit.y + ")");
            } else {
                System.out.println("\nExit Position K: Tidak ditemukan");
            }
    
            System.out.println("\nPieces:");
            printBoard(data.board, data.rows, data.cols);
        } catch (IOException e) {
            System.err.println("Gagal membaca file: " + e.getMessage());
        }
    }
}