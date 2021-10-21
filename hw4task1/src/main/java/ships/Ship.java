package ships;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Class for maintaining ship
 */
public class Ship implements Runnable {
    private GoodsType goodsType;

    private Capacity capacity;

    private Integer goodCount;

    private Tunnel tunnel;
    private Dock dock;

    /**
     * Ship constructor
     * @param capacity
     * @param goodCount
     */
    public Ship(Capacity capacity, Integer goodCount) {
        this.capacity = capacity;
        this.goodCount = goodCount;
    }

    /**
     * Ship constructor
     * @param goodsType
     * @param capacity
     * @param goodCount
     */
    public Ship(GoodsType goodsType, Capacity capacity, Integer goodCount) {
        this.goodsType = goodsType;
        this.capacity = capacity;
        this.goodCount = goodCount;
    }

    public static Ship createRandom(Random random) {
        List<GoodsType> goodsTypes = Arrays.asList(GoodsType.values());
        List<Capacity> capacities = Arrays.asList(Capacity.values());

        GoodsType goodsType = goodsTypes.get(random.nextInt(3));
        Capacity capacity = capacities.get(random.nextInt(3));
        Ship ship = new Ship(goodsType, capacity, 0);

        return ship;
    }

    /**
     * Ship goes through tunnel and then it's loaded with goods
     */
    public void run() {
        try {
            if (tunnel != null) {
                tunnel.goThrough();
            }

            while (goodCount != capacity.getGoodCount()) {
                dock.loadGoods(this);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Analogue of previous method, but with logging
     */
    public void runWithLogs() {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("HH:mm:ss");

        String threadName = Thread.currentThread().getName();
        System.out.println(threadName + " was invoked from waiting queue at " +
                df.format(LocalDateTime.now()));

        try {
            if (tunnel != null) {
                System.out.println(threadName + " trying to go through tunnel at " +
                        df.format(LocalDateTime.now()));
                tunnel.goThrough();
                System.out.println(threadName + " successfully passed through tunnel at " +
                        df.format(LocalDateTime.now()));
            }

            while (goodCount != capacity.getGoodCount()) {
                System.out.println(threadName + " trying to load goods from "
                        + goodsType.toString() + " dock"
                        + " at " +
                        df.format(LocalDateTime.now()));
                dock.loadGoods(this);
                System.out.println(threadName + " successfully loaded part of goods from "
                        + goodsType.toString() + " dock"
                        + " at " +
                        df.format(LocalDateTime.now()));
            }
            System.out.println(threadName + " successfully loaded all of goods at " +
                    df.format(LocalDateTime.now()));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public GoodsType getGoodsType() {
        return goodsType;
    }

    public Capacity getCapacity() {
        return capacity;
    }

    public Integer getGoodCount() {
        return goodCount;
    }

    public void setDock(Dock dock) {
        this.dock = dock;
    }

    public void setTunnel(Tunnel tunnel) {
        this.tunnel = tunnel;
    }

    public void addGoods(Integer count) {
        goodCount += count;
    }
}
