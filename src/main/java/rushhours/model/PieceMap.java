package rushhours.model;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PieceMap {
    private Map<Character, Piece> pieceMap;

    // constructor
    public PieceMap() { this.pieceMap = new HashMap<>(); }

    // get
    public Piece getPieceInfo(char id) { return pieceMap.get(id); }
    public Set<Character> getKeys() { return pieceMap.keySet(); }

    // add
    public void addPiece(char id, Piece piece) {
        Piece info = piece;
        pieceMap.put(id, info);
    }

    // copy & string
    public PieceMap deepCopy() {
        PieceMap copy = new PieceMap();
        for (Map.Entry<Character, Piece> entry : this.pieceMap.entrySet()) {
            copy.addPiece(entry.getKey(), entry.getValue().deepCopy());
        }
        return copy;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Character, Piece> entry : pieceMap.entrySet()) {
            sb.append("ID: ").append(entry.getKey())
            .append(" -> ").append(entry.getValue().toString())
            .append("\n");
        }
        return sb.toString();
    }
}
