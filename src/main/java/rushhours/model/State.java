package rushhours.model;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Stack;
import java.util.Set;
import rushhours.model.Colors.*;


public class State {
    private State parent = null;
    private Board board;
    private PieceMap pieces;
    private char movedPiece;
    private String movedDirection;
    private int pastCost; //g(n)
    private int nextCost; //h(n)
    private int totalCost; //f(n) = h(n) + g(n)
    
    // constructor
    public State(Board board, PieceMap pieces) {
        this.board = board;
        this.pieces = pieces;
    }

    // get
    public String getMoveInfo() { return (movedPiece + "-" + movedDirection); }
    public Board getBoard() { return this.board; }
    public PieceMap getPieces() { return this.pieces; }
    public int getPastCost() { return this.pastCost; }
    public int getTotalCost() { return this.totalCost; }
    public int getNextCost() { return this.nextCost; }
    public Piece getPrimaryInfo() { return pieces.getPieceInfo('P'); }
    public State getParent() { return this.parent; }
    public String getBoardState() { return this.board.toString(); }

    // set
    public void setNextCost(int value) { this.nextCost = value; }
    public void setPastCost(int value) { this.pastCost = value; }
    public void setTotalCost(int value) { this.totalCost = value; }

    // logic
    public Set<State> generateNextStates(HashSet<String> stateSets, String heuristicType) {
        Set<State> childList = new HashSet<>();
        for (char key : this.pieces.getKeys()) {

            // right
            for (int steps = 1; ; steps++) {
                State child = this.createChild();
                boolean valid = true;
                for (int i = 0; i < steps; i++) {
                    if (child.pieces.getPieceInfo(key).canMoveRight(child.board)) {
                        child.pieces.getPieceInfo(key).moveRight(child.board);
                    } else {
                        valid = false;
                        break;
                    }
                }
                if (!valid) break;
                String boardState = child.getBoardState();
                if (!stateSets.contains(boardState)) {
                    int value = child.getHeuristicValue(heuristicType, child.board);
                    child.setPastCost(this.pastCost + 1);
                    child.setNextCost(value);
                    child.setTotalCost(child.pastCost + child.nextCost);
                    child.movedPiece = key;
                    child.movedDirection = "Right";
                    childList.add(child);
                }
            }

            // left
            for (int steps = 1; ; steps++) {
                State child = this.createChild();
                boolean valid = true;
                for (int i = 0; i < steps; i++) {
                    if (child.pieces.getPieceInfo(key).canMoveLeft(child.board)) {
                        child.pieces.getPieceInfo(key).moveLeft(child.board);
                    } else {
                        valid = false;
                        break;
                    }
                }
                if (!valid) break;
                String boardState = child.getBoardState();
                if (!stateSets.contains(boardState)) {
                    int value = child.getHeuristicValue(heuristicType, child.board);
                    child.setPastCost(this.pastCost + 1);
                    child.setNextCost(value);
                    child.setTotalCost(child.pastCost + child.nextCost);
                    child.movedPiece = key;
                    child.movedDirection = "Left";
                    childList.add(child);
                }
            }

            // up
            for (int steps = 1; ; steps++) {
                State child = this.createChild();
                boolean valid = true;
                for (int i = 0; i < steps; i++) {
                    if (child.pieces.getPieceInfo(key).canMoveUp(child.board)) {
                        child.pieces.getPieceInfo(key).moveUp(child.board);
                    } else {
                        valid = false;
                        break;
                    }
                }
                if (!valid) break;
                String boardState = child.getBoardState();
                if (!stateSets.contains(boardState)) {
                    int value = child.getHeuristicValue(heuristicType, child.board);
                    child.setPastCost(this.pastCost + 1);
                    child.setNextCost(value);
                    child.setTotalCost(child.pastCost + child.nextCost);
                    child.movedPiece = key;
                    child.movedDirection = "Up";
                    childList.add(child);
                }
            }

            // down
            for (int steps = 1; ; steps++) {
                State child = this.createChild();
                boolean valid = true;
                for (int i = 0; i < steps; i++) {
                    if (child.pieces.getPieceInfo(key).canMoveDown(child.board)) {
                        child.pieces.getPieceInfo(key).moveDown(child.board);
                    } else {
                        valid = false;
                        break;
                    }
                }
                if (!valid) break;
                String boardState = child.getBoardState();
                if (!stateSets.contains(boardState)) {
                    int value = child.getHeuristicValue(heuristicType, child.board);
                    child.setPastCost(this.pastCost + 1);
                    child.setNextCost(value);
                    child.setTotalCost(child.pastCost + child.nextCost);
                    child.movedPiece = key;
                    child.movedDirection = "Down";
                    childList.add(child);
                }
            }
        }
        return childList;
    }
    public boolean isGoal(Board board) {
        return this.primaryDistanceToGoal(board) == 1;
    }

    // Heuristic
    public int getHeuristicValue(String heuristicType, Board board) {
        switch(heuristicType) {
            case "Distance To Goal": return this.primaryDistanceToGoal(board);
            case "Blocking Piece": return this.blockingPiece(board);
            case "Blocking Piece + Distance": return this.blockingPiece(board) + this.primaryDistanceToGoal(board);
            case "UCS(No Heuristic)" : return 1;
        }
        return -1;
    }

    // 1
    public int primaryDistanceToGoal(Board board) {
        Piece primary = pieces.getPieceInfo('P');
        Coordinate goal = board.getGoalCoordinate();
        if (primary.getOrientation() == 'H') {
            int leftX = primary.getCoordinate().getX(); 
            int rightX = primary.getCoordinate().getX() + primary.getLength() - 1;
            int goalX = goal.getX();
            return Math.min(Math.abs(leftX - goalX), Math.abs(rightX - goalX));
        } else if (primary.getOrientation() == 'V') {
            int topY = primary.getCoordinate().getY();
            int bottomY = primary.getCoordinate().getY() + primary.getLength() - 1;
            int goalY = goal.getY();
            return Math.min(Math.abs(topY - goalY), Math.abs(bottomY - goalY));
        }
        return -1;
    }

    // 2
    public int blockingPiece(Board board) {
        Piece primary = pieces.getPieceInfo('P');
        Coordinate goal = board.getGoalCoordinate();
        int count = 0;

        if (primary.getOrientation() == 'H') {
            int row = primary.getCoordinate().getY();
            int leftX = primary.getCoordinate().getX();
            int rightX = leftX + primary.getLength() - 1;

            if (goal.getX() == board.getWidth() - 1) {
                for (int x = rightX + 1; x < board.getWidth() - 1; x++) {
                    char c = board.getCell(x, row);
                    if (c != '.' && c != '*') count++;
                }
            }
            else if (goal.getX() == 0) {
                for (int x = leftX - 1; x > 0; x--) {
                    char c = board.getCell(x, row);
                    if (c != '.' && c != '*') count++;
                }
            }
        } else if (primary.getOrientation() == 'V') {
            int col = primary.getCoordinate().getX();
            int topY = primary.getCoordinate().getY();
            int bottomY = topY + primary.getLength() - 1;

            if (goal.getY() == board.getHeight() - 1) {
                for (int y = bottomY + 1; y < board.getHeight() - 1; y++) {
                    char c = board.getCell(col, y);
                    if (c != '.' && c != '*') count++;
                }
            }
            else if (goal.getY() == 0) {
                for (int y = topY - 1; y > 0; y--) {
                    char c = board.getCell(col, y);
                    if (c != '.' && c != '*') count++;
                }
            }
        }
        return count;
    }

    // 3
    public int getCompositeValue(Board board){
        return this.blockingPiece(board) + this.primaryDistanceToGoal(board);
    }

    // deep copy 
    public State createChild() {
        Board boardCopy = this.board.deepCopy();
        PieceMap piecesCopy = this.pieces.deepCopy();
        State child = new State(boardCopy, piecesCopy);
        child.parent = this;
        return child;
    }

    public State deepCopy() {
        Board boardCopy = this.board.deepCopy();
        PieceMap piecesCopy = this.pieces.deepCopy();
        State copy = new State(boardCopy, piecesCopy);
        copy.parent = null;
        return copy;
    }

    // output 
    Stack<String> outputFrames(State solvedState, ColorMap colors) { 
        Stack<String> frames = new Stack<>();
        State current = solvedState;
        while (current.parent != null) {
            frames.push(current.getBoard().coloredStringBoard(colors));
            current = current.parent;
        }
        return frames;
    }

    public Stack<String> originalFrames(State solvedState) {
        int moveCount = 0;
        State temp = solvedState;
        while (temp.parent != null) {
            moveCount++;
            temp = temp.parent;
        }

        Stack<String> frames = new Stack<>();
        State current = solvedState;
        int moveNum = moveCount;
        while (true) {
            if (current.parent == null) {
                frames.push("Papan Awal " + current.getBoard().toString());
                break;
            } else {
                String moveInformation = "Move " + moveNum + " : " + current.getMoveInfo();
                frames.push(moveInformation + " " + current.getBoard().toString());
                current = current.parent;
                moveNum--;
            }
        }
        return frames;
    }

    public static List<State> getStatePath(State solvedState) {
        List<State> path = new ArrayList<>();
        State current = solvedState;
    
        while (current != null) {
            path.add(current);
            current = current.parent;
        }
    
        Collections.reverse(path);
        return path;
    }
    

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PastCost (g): ").append(pastCost)
        .append(", NextCost (h): ").append(nextCost)
        .append(", TotalCost (f): ").append(totalCost)
        .append("\nBoard:\n").append(getBoardState())
        .append("Pieces:\n").append(pieces.toString());
        if (parent != null) {
            sb.append("hev\n");
        } else {
            sb.append("no hev\n");
        }
        return sb.toString();
    }

}
