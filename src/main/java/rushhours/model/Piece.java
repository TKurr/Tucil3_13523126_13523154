package rushhours.model;

public class Piece {
    private char id;
    private int length;
    private Coordinate coordinate;
    private char orientation;

    // constructor
    public Piece(char id, int length, Coordinate coordinate, char orientation) {
        this.id = id;
        this.length = length;
        this.coordinate = coordinate;
        this.length = length;
        this.orientation = orientation;
    }

    // get
    public char getId() { return this.id; }
    public int getLength() { return this.length; } 
    public char getOrientation() { return this.orientation; }
    public Coordinate getCoordinate() { return this.coordinate; }

    // set
    public void setId(char id) { this.id = id; }
    public void setLength(int length) { this.length = length; }
    public void setOrientation(char orientation) { this.orientation = orientation; }
    public void setCoordinate(int x, int y) {
        this.coordinate.setX(x);
        this.coordinate.setY(y);
    }

    // can move?
    public boolean canMoveRight(Board board) {
        if (this.orientation != 'H') return false;
        int targetX = this.coordinate.getX() + this.length;
        int targetY = this.coordinate.getY();
        return board.isAvailable(targetX, targetY);
    }
    public boolean canMoveLeft(Board board) {
        if (this.orientation != 'H') return false;
        int targetX = this.coordinate.getX() - 1;
        int targetY = this.coordinate.getY();
        return board.isAvailable(targetX, targetY);
    }
    public boolean canMoveUp(Board board) {
        if (this.orientation != 'V') return false;
        int targetX = this.coordinate.getX();
        int targetY = this.coordinate.getY() - 1;
        return board.isAvailable(targetX, targetY);
    }
    public boolean canMoveDown(Board board) {
        if (this.orientation != 'V') return false;
        int targetX = this.coordinate.getX();
        int targetY = this.coordinate.getY() + this.length;
        return board.isAvailable(targetX, targetY);
    }

    // piece move
    public void moveRight(Board board) {
        int y = this.coordinate.getY();
        int oldHeadX = this.coordinate.getX();
        int oldTailX = this.coordinate.getX() + this.length - 1;
        board.setCell(oldHeadX, y, '.');
        board.setCell(oldTailX + 1, y, this.id);
        this.setCoordinate(oldHeadX + 1, y);
    }

    public void moveLeft(Board board) {
        int y = this.coordinate.getY();
        int oldHeadX = this.coordinate.getX();
        int oldTailX = this.coordinate.getX() + this.length - 1;
        board.setCell(oldTailX, y, '.');
        board.setCell(oldHeadX - 1, y, this.id);
        this.setCoordinate(oldHeadX - 1, y);
    }

    public void moveUp(Board board) {
        int x = this.coordinate.getX();
        int oldHeadY = this.coordinate.getY();
        int oldTailY = this.coordinate.getY() + this.length - 1;
        board.setCell(x, oldTailY, '.');
        board.setCell(x, oldHeadY - 1, this.id);
        this.setCoordinate(x, oldHeadY - 1);
    }

    public void moveDown(Board board) {
        int x = this.coordinate.getX();
        int oldHeadY = this.coordinate.getY();
        int oldTailY = this.coordinate.getY() + this.length - 1;
        board.setCell(x, oldHeadY, '.');
        board.setCell(x, oldTailY + 1, this.id);
        this.setCoordinate(x, oldHeadY + 1);
    }

    // copy & string
    Piece deepCopy() {
        Coordinate coordCopy = new Coordinate();
        coordCopy.setX(this.coordinate.getX());
        coordCopy.setY(this.coordinate.getY());
        return new Piece(this.id, this.length, coordCopy, this.orientation);
    }

    @Override
    public String toString() {
        return "Length: " + length + ", Coordinate: " + coordinate + ", Orientation: " + orientation;
    }
}


