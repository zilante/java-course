package checkers;

/**
 * Class for maintaining the figure from the checkers game.
 */
public class Figure {
    private Colour colour;
    private FigureType type;

    public Figure(Colour colour, FigureType type) {
        this.colour = colour;
        this.type = type;
    }

    public Colour getColour() {
        return colour;
    }

    public FigureType getType() {
        return type;
    }

    public void setColour(Colour colour) { this.colour = colour; }

    public void setType(FigureType type) {
        this.type = type;
    }

    public void reset() {
        colour = Colour.NO;
        type = FigureType.Nobody;
    }
}
