package ships;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Class for maintaining ship generator
 */
public class Generator implements Runnable {
    private Map<GoodsType, Queue<Ship>> ships;
    private Tunnel tunnel;

    private boolean isTimeToTerminate = false;

    /**
     * Generator constructor
     * @param breadShips - waiting queue for ships with breads
     * @param bananaShips - waiting queue for ships with bananas
     * @param clothesShips - waiting queue for ships with clothes
     */
    public Generator(Queue<Ship> breadShips, Queue<Ship> bananaShips, Queue<Ship> clothesShips,
                     Tunnel tunnel) {
        ships = new HashMap<>();

        ships.put(GoodsType.Bread, breadShips);
        ships.put(GoodsType.Banana, bananaShips);
        ships.put(GoodsType.Clothes, clothesShips);

        this.tunnel = tunnel;
    }

    /**
     * Generating random ships
     * executed in special thread
     */
    public void run() {
        Random random = new Random();

        while(!isTimeToTerminate) {
            try {
                TimeUnit.MILLISECONDS.sleep(random.nextInt(500));

                int shipCount = random.nextInt(5);

                for (int i = 0; i < shipCount; ++i) {
                    Ship ship = Ship.createRandom(random);
                    ship.setTunnel(tunnel);

                    ships.get(ship.getGoodsType()).add(ship);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void setTimeToTerminate() {
        isTimeToTerminate = true;
    }
}
