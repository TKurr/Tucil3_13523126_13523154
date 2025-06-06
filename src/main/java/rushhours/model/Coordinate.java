package rushhours.model;

public class Coordinate {
    private int x;
    private int y;

    // constructor
    public Coordinate() {
        this.x = -1;
        this.y = -1;
    }
    
    // get
    public int getX() { return this.x; }
    public int getY() { return this.y; }

    // set
    public void setX(int x) { this.x = x; } 
    public void setY(int y) { this.y = y; }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}