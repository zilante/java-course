package ships;

public enum Capacity {
    Low (10),
    Medium (50),
    High (100);

    private final int goodCount;

    Capacity(int goodCount) {
        this.goodCount = goodCount;
    }

    public int getGoodCount() {
        return goodCount;
    }
}
