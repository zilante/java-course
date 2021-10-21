package checkers;

public enum Direction {
    UpRight, UpLeft, DownRight, DownLeft;

    public Direction getOpposite() {
        if (this == UpRight) {
            return DownLeft;
        }
        if (this == UpLeft) {
            return DownRight;
        }
        if (this == DownRight) {
            return UpLeft;
        }
        return UpRight;
    }

    public boolean isRowUp() {
        return this == UpLeft || this == UpRight;
    }

    public boolean isColRight() {
        return this == UpRight || this == DownRight;
    }
}
