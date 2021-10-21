package checkers;

import java.util.ArrayList;

/**
 * The class for maintaining configuration of the checkers game.
 */
class CheckersConfiguration {
    final private int rowSize = 8;
    private ArrayList<Field> fields = new ArrayList<>(rowSize * rowSize);

    /**
     * Constructor of CheckersConfiguration
     * @param whitePositionsStr
     * @param blackPositionsStr
     */
    public CheckersConfiguration(String whitePositionsStr,
                                 String blackPositionsStr) {
        final int colSize = rowSize;
        for (int row = 0; row < colSize; ++row) {
            for (int col = 0; col < rowSize; ++col) {
                Field field = new Field(row, col,
                        new Figure(Colour.NO, FigureType.Nobody));
                fields.add(row * rowSize + col, field);
            }
        }

        String[] whitePositions = whitePositionsStr.split(" ");
        String[] blackPositions = blackPositionsStr.split(" ");

        for (String position : whitePositions) {
            Field field = new Field(position, Colour.WHITE);
            int row = field.getRow();
            int col = field.getColumn();
            fields.set(row * rowSize + col, field);
        }

        for (String position : blackPositions) {
            Field field = new Field(position, Colour.BLACK);
            int row = field.getRow();
            int col = field.getColumn();
            fields.set(row * rowSize + col, field);
        }
    }

    /**
     * Get string with positions of all pieces
     * @return
     */
    public String getAllPieces() {
        String white_pieces = getPiecesWithColour(Colour.WHITE);
        String black_pieces = getPiecesWithColour(Colour.BLACK);

        StringBuilder pieces = new StringBuilder(white_pieces);
        pieces.append("\n");
        pieces.append(black_pieces);

        return pieces.toString();
    }

    private String getPiecesWithColour(Colour colour) {
        StringBuilder kings = new StringBuilder();
        StringBuilder men = new StringBuilder();

        for (int j = 0; j < rowSize; ++j) {
            for (int i = 0; i < rowSize; ++i) {
                Field field = fields.get(i * rowSize + j);

                if (field.getFigure().getColour() == colour) {
                    if (field.getFigure().getType() == FigureType.King) {
                        kings.append(field.toString() + " ");
                    } else {
                        men.append(field.toString() + " ");
                    }
                }
            }
        }

        StringBuilder pieces = new StringBuilder(kings);
        pieces.append(men);
        pieces.deleteCharAt(pieces.length() - 1);

        return pieces.toString();
    }

    /**
     * Make move of whites and blacks. It's supposed that move notation
     * is correct.
     * @param movesString
     * @throws CheckersErrorException
     * @throws InvalidMoveException
     */
    public void makeMoves(String movesString) throws CheckersErrorException,
            InvalidMoveException {
        String[] moves = movesString.split(" ");

        String whitesMove = moves[0];
        String blacksMove = moves[1];

        if (isBeatingMove(whitesMove)) {
            makeBeats(Colour.WHITE, whitesMove);
        } else {
            movePiece(Colour.WHITE, whitesMove, false);
        }

        if (isBeatingMove(blacksMove)) {
            makeBeats(Colour.BLACK, blacksMove);
        } else {
            movePiece(Colour.BLACK, blacksMove, false);
        }
    }

    // it's supposed that move notation is correct
    // move must be beating move (with ":" between positions)
    private void makeBeats(Colour playerColour, String move) throws
            CheckersErrorException, InvalidMoveException {
        String[] fields = move.split(":");

        for (int i = 0; i < fields.length - 1; ++i) {
            String from = fields[i];
            String to = fields[i + 1];
            movePiece(playerColour, from + ":" + to, true);
        }
    }

    // it's supposed that move notation is correct
    // move in format xx-xx or xx:xx (not xx:xx:xx... !)
    private void movePiece(Colour playerColour, String move,
                           boolean isBeating) throws CheckersErrorException,
            InvalidMoveException {
        String[] fields;
        if (!isBeating) {
            fields = move.split("-");
        } else {
            fields = move.split(":");
        }
        Field from = getFieldAt(fields[0]);
        Field to = getFieldAt(fields[1]);

        if (!to.isEmpty()) {
            throw new InvalidMoveException("busy cell");
        }

        if (to.isWhiteField()) {
            throw new InvalidMoveException("white cell");
        }

        if (!isMoveOnOwnPiece(playerColour, move)) {
            throw new CheckersErrorException("error");
        }

        if (!isCorrectMovingFigureType(move)) {
            throw new CheckersErrorException("error");
        }

        if (!isBeating) {
            for (Field field : this.fields) {
                if (field.getFigure().getColour() != playerColour) {
                    continue;
                }

                if (isBeatingNeeded(field)) {
                    throw new InvalidMoveException("invalid move");
                }
            }

            if (!isAvailable(from, to)) {
                throw new CheckersErrorException("error");
            }
        } else {
            if (!from.isSameDiagonal(to)) {
                throw new CheckersErrorException("error");
            }

            Direction direction = getDirection(from, to);

            if (!hasAnyFigureFieldOnDirection(from, direction)) {
                throw new CheckersErrorException("error");
            }

            Field victim = getClosestFigureFieldOnDirection(from, direction);

            if (victim !=
                    getClosestFigureFieldOnDirection(to,
                            direction.getOpposite())) {
                throw new CheckersErrorException("error");
            }

            if (!isBeatingNeeded(from, victim)) {
                throw new CheckersErrorException("error");
            }

            Field victimCopy = new Field(victim);
            victim.setFigure(from.getFigure());

            if (!isAvailable(victim, to)) {
                victim.setFigure(victimCopy.getFigure());
                throw new CheckersErrorException("error");
            }

            victim.removeFigure();
        }

        to.setFigure(from.getFigure());
        from.removeFigure();

        to.renewIfKingAvailable(rowSize);
    }

    // supposed that move notation is correct
    private boolean isCorrectMovingFigureType(String move) {
        String fromString = move.substring(0, 2);
        Field from = getFieldAt(fromString);

        if (Character.isUpperCase(move.charAt(0))) {
            return from.getFigure().getType() == FigureType.King;
        }

        return from.getFigure().getType() != FigureType.Nobody;
    }

    // supposed that move notation is correct
    private boolean isMoveOnOwnPiece(Colour playerColour, String move) {
        if (playerColour == Colour.NO) {
            return false;
        }

        String fromString = move.substring(0, 2);
        Field from = getFieldAt(fromString);
        return from.getFigure().getColour() == playerColour;
    }

    private Field getFieldAt(String position) {
        int row = Field.getRowAt(position);
        int column = Field.getColumnAt(position);
        return fields.get(row * rowSize + column);
    }

    private boolean isBeatingNeeded(Field from, Direction direction) {
        if (from.isEmpty()) {
            return false;
        }

        if (from.isMan()) {
            if (getNeighbour(from, direction).isEmpty()) {
                return false;
            }
        } else {
            if (!hasAnyFigureFieldOnDirection(from, direction)) {
                return false;
            }
        }

        Field supposedVictim = getClosestFigureFieldOnDirection(from, direction);

        if (from.isOpponent(supposedVictim)) {
            if (!hasNeighbour(supposedVictim, direction)) {
                return false;
            }

            Field supposedVictimNeighb = getNeighbour(supposedVictim, direction);

            if (supposedVictimNeighb.isEmpty()) {
                return true;
            }
        }

        return false;
    }

    private boolean isBeatingNeeded(Field from, Field victim) {
        Direction direction = getDirection(from, victim);

        if (victim.isEmpty() || from.isEmpty()) {
            return false;
        }

        if (from.isMan()) {
            if (!from.isNeighbour(victim)) {
                return false;
            }
        } else {
            if (getClosestFigureFieldOnDirection(from, direction) !=
                    victim) {
                return false;
            }
        }

        return isBeatingNeeded(from, direction);
    }

    private boolean isBeatingNeeded(Field from) {
        for (Direction direction : Direction.values()) {
            if (isBeatingNeeded(from, direction)) {
                return true;
            }
        }

        return false;
    }

    // it's suposed that move is in the correct move notation
    private boolean isBeatingMove(String move) {
        return move.charAt(2) == ':';
    }

    // return fields between two fields if to look diagonally
    private Field[] getBetween(Field field1, Field field2) {
        Direction direction = getDirection(field1, field2);
        int fieldCount = Math.abs(field1.getRow() - field2.getRow()) - 1;
        Field[] betweenFields = new Field[fieldCount];

        Field prevField = field1;
        for (int i = 0; i < fieldCount; ++i) {
            betweenFields[i] = getNeighbour(prevField, direction);
            prevField = betweenFields[i];
        }

        return betweenFields;
    }

    // can we move from "from" to "to" without beating, even if it's necessary
    private boolean isAvailable(Field from, Field target) {
        if (from.isEmpty() || !target.isEmpty()) {
            return false;
        }

        if (from.isMan()) {
            return from.isNeighbour(target);
        }

        if (!from.isSameDiagonal(target)) {
            return false;
        }

        Field[] betweens = getBetween(from, target);

        for (Field between : betweens) {
            if (!between.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    // if doesn't exist, returns "from" field
    private Field getNeighbour(Field from, Direction direction) {
        int fromRow = from.getRow();
        int fromCol = from.getColumn();

        int rowStep = direction.isRowUp() ? 1 : -1;
        int colStep = direction.isColRight() ? 1 : -1;

        int row = fromRow + rowStep;
        int col = fromCol + colStep;

        int colSize = rowSize;
        if (row < rowSize && col < colSize && row >= 0 && col >= 0) {
            return fields.get(row * rowSize + col);
        }

        return from;
    }

    private boolean hasNeighbour(Field from, Direction direction) {
        return getNeighbour(from, direction) != from;
    }

    // if doesn't exist, returns "from" field
    private Field getClosestFigureFieldOnDirection(Field from, Direction direction) {
        Field prevField = from;
        while (hasNeighbour(prevField, direction)) {
            Field currField = getNeighbour(prevField, direction);
            if (!currField.isEmpty()) {
                return currField;
            }
            prevField = currField;
        }

        return from;
    }

    private boolean hasAnyFigureFieldOnDirection(Field from, Direction direction) {
        Field closest = getClosestFigureFieldOnDirection(from, direction);
        return closest != from;
    }

    // "from" and "to" must be on the same diagonal
    // they also must be distinct
    private Direction getDirection(Field from, Field to) {
        int row1 = from.getRow();
        int row2 = to.getRow();
        int col1 = from.getColumn();
        int col2 = to.getColumn();

        if (row2 - row1 > 0) {
            if (col2 - col1 > 0) {
                return Direction.UpRight;
            } else {
                return Direction.UpLeft;
            }
        } else {
            if (col2 - col1 > 0) {
                return Direction.DownRight;
            } else {
                return Direction.DownLeft;
            }
        }
    }
}
