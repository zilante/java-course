package checkers;

/**
 * The class for maintaining field in the checkers game.
 * It's not just position on the board, but also figure on the field
 */
class Field {
    private int row;
    private int column;
    private Figure figure;

    /**
     * Constructor of field with figure (man or king).
     * @param position must be correct position in checkers
     * @param colour mustn't be Colour.NO
     */
    public Field(String position, Colour colour) {
        row = getRowAt(position);
        column = getColumnAt(position);

        FigureType type = FigureType.Man;
        if (Character.isUpperCase(position.charAt(0))) {
            type = FigureType.King;
        }

        figure = new Figure(colour, type);
    }

    /**
     * Constructor for field
     * @param row
     * @param column
     * @param figure
     */
    public Field(int row, int column, Figure figure) {
        this.row = row;
        this.column = column;
        this.figure = figure;
    }

    /**
     * Copy constructor for Field
     * @param field
     */
    public Field(Field field) {
        this.row = field.getRow();
        this.column = field.getColumn();
        this.figure = new Figure(field.getFigure().getColour(),
                field.getFigure().getType());
    }

    /**
     * Get column index by position.
     * @param position must be correct position in checkers
     * @return column index
     */
    static public int getColumnAt(String position) {
        final char col = position.charAt(0);
        final int colAsciiCode = (int)(col); // getting ascii code
        int aAsciiCode = 0;

        if (Character.isUpperCase(col)) {
            aAsciiCode = (int)('A');
        } else {
            aAsciiCode = (int)('a');
        }

        return colAsciiCode - aAsciiCode;
    }

    /**
     * Get row index by position.
     * @param position must be correct position in checkers
     * @return row index
     */
    static public int getRowAt(String position) {
        return Integer.parseInt(Character.toString(position.charAt(1))) - 1;
    }

    /**
     *
     * @param figure set this.figure as copy of a figure
     */
    public void setFigure(Figure figure) {
        this.figure.setType(figure.getType());
        this.figure.setColour(figure.getColour());
    }

    public void removeFigure() {
        figure.reset();
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    public Figure getFigure() {
        return figure;
    }

    /**
     * Neighbours of field - are fields, which can be achieved from the field
     * by men by one move without beating.
     * @param field
     * @return is field neighbour of this field
     */
    public boolean isNeighbour(Field field) {
        int row = field.getRow();
        int col = field.getColumn();

        if (Math.abs(row - this.row) != 1) {
            return false;
        }

        if (Math.abs(col - this.column) != 1) {
            return false;
        }

        return true;
    }

    /**
     * White field is opponent to black field.
     * No-coloured field isn't opponent to any fields.
     * @param field
     * @return is field opponent to this field
     */
    public boolean isOpponent(Field field) {
        if (figure.getColour() == Colour.NO ||
                field.getFigure().getColour() == Colour.NO) {
            return false;
        }

        return figure.getColour() != field.getFigure().getColour();
    }

    /**
     * Field is empty, if it has figure with FigureType Nobody
     * @return
     */
    public boolean isEmpty() {
        return figure.getType() == FigureType.Nobody;
    }

    /**
     * Method is to check if field is in the same diagonal with other field
     * @param field
     * @return
     */
    public boolean isSameDiagonal(Field field) {
        int row = field.getRow();
        int col = field.getColumn();

        return Math.abs(this.row - row) == Math.abs(this.column - col);
    }

    public boolean isMan() {
        return figure.getType() == FigureType.Man;
    }

    public boolean isKing() {
        return figure.getType() == FigureType.King;
    }

    /**
     * When Man achieves last line of the board, it becomes King. So this
     * method allows to check if piece has achieved last line, and if it
     * has, method changes it's FigureType.
     * @param colSize - size of columns on the board.
     */
    public void renewIfKingAvailable(final int colSize) {
        if (isEmpty()) {
            return;
        }

        if (figure.getColour() == Colour.WHITE) {
            if (row == colSize - 1) {
                figure.setType(FigureType.King);
            }
        } else {
            if (row == 0) {
                figure.setType(FigureType.King);
            }
        }
    }

    /**
     * For empty field it returns empty string.
     * For other types of field it returns their position.
     * For example, "e4", if it's Man, or "E4", if it's King
     * @return
     */
    @Override
    public String toString() {
        if (isEmpty()) {
            return "";
        }

        int aAsciiCode = isMan() ? (int)('a') : (int)('A');
        char col = (char)(aAsciiCode + this.column);
        String row = Integer.toString(this.row + 1);

        return col + row;
    }

    public boolean isWhiteField() {
        return (row + column) % 2 == 1;
    }
}
