package rushhours.model.Colors;

import java.util.ArrayList;
import java.util.HashMap;

import rushhours.model.PieceMap;

public class ColorMap {
    HashMap <Character, Color> colorMap;
    ArrayList <Color> colorSet;

    // constructor
    public ColorMap() {
        this.colorMap = new HashMap<>();
        this.colorSet = new ArrayList<>();

        this.colorSet.add(new Color("GREEN",              "\u001B[42;30m"));
        this.colorSet.add(new Color("YELLOW",             "\u001B[43;30m"));
        this.colorSet.add(new Color("BLUE",               "\u001B[44;37m"));
        this.colorSet.add(new Color("MAGENTA",            "\u001B[45;30m"));
        this.colorSet.add(new Color("CYAN",               "\u001B[46;30m"));
        
        this.colorSet.add(new Color("BRIGHT_GREEN",       "\u001B[102;30m"));
        this.colorSet.add(new Color("BRIGHT_YELLOW",      "\u001B[103;30m"));
        this.colorSet.add(new Color("BRIGHT_BLUE",        "\u001B[104;30m"));
        this.colorSet.add(new Color("BRIGHT_MAGENTA",     "\u001B[105;30m"));
        this.colorSet.add(new Color("BRIGHT_CYAN",        "\u001B[106;30m"));
        
        this.colorSet.add(new Color("BOLD_GREEN",         "\u001B[1;42;30m"));
        this.colorSet.add(new Color("BOLD_YELLOW",        "\u001B[1;43;30m"));
        this.colorSet.add(new Color("BOLD_BLUE",          "\u001B[1;44;37m"));
        this.colorSet.add(new Color("BOLD_MAGENTA",       "\u001B[1;45;30m"));
        this.colorSet.add(new Color("BOLD_CYAN",          "\u001B[1;46;30m"));
        
        this.colorSet.add(new Color("BRIGHT_BOLD_GREEN",  "\u001B[1;102;30m"));
        this.colorSet.add(new Color("BRIGHT_BOLD_YELLOW", "\u001B[1;103;30m"));
        this.colorSet.add(new Color("BRIGHT_BOLD_BLUE",   "\u001B[1;104;37m"));
        this.colorSet.add(new Color("BRIGHT_BOLD_MAGENTA","\u001B[1;105;30m"));
        this.colorSet.add(new Color("BRIGHT_BOLD_CYAN",   "\u001B[1;106;30m"));
        
        this.colorSet.add(new Color("GRAY",               "\u001B[100;30m"));
        this.colorSet.add(new Color("BRIGHT_GRAY",        "\u001B[1;100;30m"));
        this.colorSet.add(new Color("BOLD_GRAY",          "\u001B[1;100;37m"));
        this.colorSet.add(new Color("BRIGHT_BOLD_GRAY",   "\u001B[1;100;97m"));
    }


    // mapp
    public void mapColorToPieces(PieceMap pieceMap) {
        int colorCount = this.colorSet.size();
        int i = 0;
        this.colorMap.clear(); 
        for (char key : pieceMap.getKeys()) {
            if(key == 'P') continue;
            this.colorMap.put(key, this.colorSet.get(i % colorCount));
            i++;
        }
    }

    public HashMap<Character, Color> getColorMap() { return this.colorMap; }
    public Color getColor(char key) { return this.colorMap.get(key); }
}