package rushhours.model;

import rushhours.model.Colors.ColorMap;

public class Board {
    private Coordinate goal;
    private int height;
    private int width;
    private char[][] grid;

    // constructor
    public Board(int height, int width) { 
        this.width = width;
        this.height = height;
        this.grid = new char[this.height][this.width];
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                if (x == 0 || y == 0 || x == this.width-1|| y == this.height-1) {
                    this.grid[y][x] = '*';
                } else {
                    this.grid[y][x] = '.';
                }
            }
        }
    }

    // get
    public Coordinate getGoalCoordinate() { return this.goal; }
    public int getWidth() { return this.width; }
    public int getHeight() { return this.height; }
    public char[][] getGrid() { return this.grid; }
    public char getCell(int x, int y) {
        if (insideGrid(x, y)) {
            return this.grid[y][x];
        }
        throw new IndexOutOfBoundsException("Cell out of bounds");
    }

    // set
    public void setGoal(int x, int y) {
        this.goal = new Coordinate();
        this.goal.setX(x);
        this.goal.setY(y);
    }
    public void setCell(int x, int y, char value) {
        if (insideGrid(x, y)) {
            this.grid[y][x] = value;
        } else {
            throw new IndexOutOfBoundsException("Cell out of bounds");
        }
    }

    // valid?
    public boolean isAvailable(int x, int y) {
        return insideGrid(x,y) && this.grid[y][x] == '.';
    }

    private boolean insideGrid(int x, int y) {
        return (0 <= x && x < this.width) && (0 <= y && y < this.height);
    }

    // copy & string
    public Board deepCopy() {
        Board copy = new Board(this.getHeight(),this.getWidth());
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                copy.grid[y][x] = this.grid[y][x];
            }
        }
        copy.goal = new Coordinate();
        copy.goal.setX(this.goal.getX());
        copy.goal.setY(this.goal.getY());
        return copy;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if(grid[y][x] == '*'){
                    continue;
                }
                sb.append(grid[y][x]);
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    public String coloredStringBoard(ColorMap colors) {
        StringBuilder sb = new StringBuilder();
        final String DEFAULT = "\u001B[40m";
        final String PRIMARY = "\u001B[41;30m";
        final String RESET = "\u001B[0m";
        final String BORDER =  "\u001B[100m";
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if(y == 0 || y == height-1) {
                    sb.append(BORDER).append("  ").append(RESET);
                } 
                else if ( x == 0 || x == width -1) {
                    if(grid[y][x] != 'K') {
                         sb.append(BORDER).append("  ").append(RESET);
                    } else {
                        sb.append(DEFAULT).append("  ").append(RESET);
                    }
                }
                else {
                    final String color;
                    if(grid[y][x] == 'P') {
                        color = PRIMARY;
                    } else {
                        if (colors.getColorMap().containsKey(grid[y][x])) {
                            color = colors.getColor(grid[y][x]).getAnsiCode();
                        }
                        else {
                            color = DEFAULT;
                        }
                    }
                    sb.append(color).append("  ").append(RESET);
                }
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}