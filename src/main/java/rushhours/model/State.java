package rushhours.model;
import java.util.HashSet;
import java.util.Stack;
import java.util.Set;


public class State {
    private State parent = null;
    private Board board;
    private PieceMap pieces;
    private int pastCost = 0; //g(n)
    private int nextCost = 0; //h(n)
    private int totalCost = 0; //f(n) = h(n) + g(n)
    
    // constructor
    public State(Board board, PieceMap pieces) {
        this.board = board;
        this.pieces = pieces;
    }

    // get
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
    public Set<State> generateNextStates(HashSet<String> stateSets, String heuristicType) { // jangan update hashset, cek for comparison aja
        Set<State> childList = new HashSet<>(); 
        for (char key : this.pieces.getKeys()) {
            Piece current = pieces.getPieceInfo(key);

            if(current.canMoveRight(this.board)) {
                State child = this.createChild();
                child.pieces.getPieceInfo(key).moveRight(child.board);
                String boardState = child.getBoardState();
                if(!stateSets.contains(boardState)) {
                    int value = child.getHeuristicValue(heuristicType, child.board);
                    child.setPastCost(this.pastCost + this.nextCost);
                    child.setNextCost(value);
                    child.setTotalCost(value + this.pastCost + this.nextCost);
                    childList.add(child);
                }
            }

            if (current.canMoveLeft(this.board)) {
                State child = this.createChild();
                child.pieces.getPieceInfo(key).moveLeft(child.board);
                String boardState = child.getBoardState();
                if (!stateSets.contains(boardState)) {
                    int value = child.getHeuristicValue(heuristicType, child.board);
                    child.setPastCost(this.pastCost + this.nextCost);
                    child.setNextCost(value);
                    child.setTotalCost(value + this.pastCost + this.nextCost);
                    childList.add(child);
                }
            }

            if (current.canMoveUp(this.board)) {
                State child = this.createChild();
                child.pieces.getPieceInfo(key).moveUp(child.board);
                String boardState = child.getBoardState();
                if (!stateSets.contains(boardState)) {
                    int value = child.getHeuristicValue(heuristicType, child.board);
                    child.setPastCost(this.pastCost + this.nextCost);
                    child.setNextCost(value);
                    child.setTotalCost(value + this.pastCost + this.nextCost);
                    childList.add(child);
                }
            }

            if (current.canMoveDown(this.board)) {
                State child = this.createChild();
                child.pieces.getPieceInfo(key).moveDown(child.board);
                String boardState = child.getBoardState();
                if (!stateSets.contains(boardState)) {
                    int value = child.getHeuristicValue(heuristicType, child.board);
                    child.setPastCost(this.pastCost + this.nextCost);
                    child.setNextCost(value);
                    child.setTotalCost(value + this.pastCost + this.nextCost);
                    childList.add(child);
                }
            }
        }
        return childList;
    }

    public boolean isGoal(Board board) {
        return this.blockingPiece(board) == 0;
    }

    // Heuristic
    public int getHeuristicValue(String heuristicType, Board board) {
        switch(heuristicType) {
            case "Distance To Goal": return this.primaryDistanceToGoal(board);
            case "Number Of Blocking Piece": return this.blockingPiece(board);
            case "Distance To Goal & Number Of Blocking Piece": return this.blockingPiece(board) + this.primaryDistanceToGoal(board);
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
        int col = -1;
        int row = -1;
        int count = 0;
        Coordinate goal = board.getGoalCoordinate();
        int distance = primaryDistanceToGoal(board);
        if (primary.getOrientation() == 'H') {
            row = goal.getY();
            if(goal.getX() == 0) {
                col = goal.getX() + distance;
            } else if(goal.getX() == board.getWidth()-1) {
                col = goal.getX() - distance;
            }

            int start = Math.min(goal.getX(), col);
            int end = Math.max(goal.getX(), col);
            for (int x = start; x < end; x++) {
                if(board.getCell(x,row) != '.') {
                    count++;
                }
            }
        } else if (primary.getOrientation() == 'V') {
            col = goal.getX();
            if(goal.getY() == 0) {
                row = goal.getY() + distance;
            } else if (goal.getY() == board.getHeight()-1) {
                row = goal.getY() - distance;
            }

            int start = Math.min(goal.getY(), row);
            int end = Math.max(goal.getY(), row);
            for (int y = start; y < end; y++) {
                if(board.getCell(col,y) != '.') {
                    count++;
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

    // output 
    public Stack<String> outputFrames(State solvedState) { 
        Stack<String> frames = new Stack<>();
        State current = solvedState;
        while (current.parent != null) {
            frames.push(current.getBoardState());
            current = current.parent;
        }
        frames.push(current.getBoardState());
        return frames;
    }

    // generateFrames -> buat GUI (ntar)

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

