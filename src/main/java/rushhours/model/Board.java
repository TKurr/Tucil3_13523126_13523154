package rushhours.model;

public class Board {
    private Coordinate goal;
    private int height;
    private int width;
    private char[][] grid;

    // constructor
    public Board(int width, int height) {
        this.width = width+2;
        this.height = height+2;
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
    Coordinate getGoalCoordinate() { return this.goal; }
    public int getWidth() { return this.width-2; }
    public int getHeight() { return this.height-2; }
    public char[][] getGrid() { return this.grid; }
    public char getCell(int x, int y) {
        int targetX = x+1;
        int targetY = y+1;
        if (insideGrid(targetX, targetY)) {
            return this.grid[targetY][targetX];
        }
        throw new IndexOutOfBoundsException("Cell out of bounds");
    }

    // set
    public void setCell(int x, int y, char value) {
        int targetX = x+1;
        int targetY = y+1;
        if (insideGrid(targetX, targetY)) {
            this.grid[targetY][targetX] = value;
        } else {
            throw new IndexOutOfBoundsException("Cell out of bounds");
        }
    }

    // valid?
    public boolean isAvailable(int x, int y) {
        int targetX = x+1;
        int targetY = y+1;
        return insideGrid(targetX, targetY) && this.grid[targetY][targetX] == '.';
    }

    private boolean insideGrid(int x, int y) {
        return (1 <= x && x < this.width-1) && (1 <= y && y < this.height-1);
    }

    // copy & string
    public Board deepCopy() {
        Board copy = new Board(this.getWidth(), this.getHeight());
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
                sb.append(grid[y][x]);
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}