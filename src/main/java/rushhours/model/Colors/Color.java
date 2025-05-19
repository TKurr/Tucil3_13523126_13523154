package rushhours.model.Colors;

public class Color {
    private String colorName;
    private String ansiCode;

    // constructur
    public Color(String name, String code) {
        this.colorName = name;
        this.ansiCode = code;
    }

    // get set
    public String getColorName() { return this.colorName; }
    public String getAnsiCode() { return this.ansiCode; }
} 