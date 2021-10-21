package ships;

import java.util.Queue;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Class for maintaining dock
 */
public class Dock implements Runnable {
    private Queue<Ship> ships;
    private Scheduler scheduler;

    private boolean isTimeToTerminate = false;

    /**
     * Dock default constructor
     */
    public Dock() {

    }

    /**
     * Dock constructor
     * @param ships - queue of waiting ships
     * @param scheduler - for scheduling execution of ships from waiting queue
     */
    public Dock(Queue<Ship> ships, Scheduler scheduler) {
        this.ships = ships;
        this.scheduler = scheduler;
    }

    /**
     *
     * @param ship - ship on which goods will be loaded
     * @throws InterruptedException
     */
    public void loadGoods(Ship ship) throws InterruptedException {
        // several ships may want to load goods, but only
        // one can be served at the same time
        synchronized(this) {
            TimeUnit.SECONDS.sleep(1);
            ship.addGoods(10);
        }
    }

    /**
     * Method invokes new ships to be served
     * executed by special thread
     */
    public void run() {
        Random random = new Random();

        while(!isTimeToTerminate) {
            try {
                TimeUnit.MILLISECONDS.sleep(random.nextInt(500));

                if (!ships.isEmpty()) {
                    Ship ship = ships.remove();
                    ship.setDock(this);

                    scheduler.schedule(() -> ship.run());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Method is analogue of previous method, but with logging
     */
    public void runWithLogs() {
        Random random = new Random();

        while(!isTimeToTerminate) {
            try {
                TimeUnit.MILLISECONDS.sleep(random.nextInt(500));

                if (!ships.isEmpty()) {
                    Ship ship = ships.remove();
                    ship.setDock(this);

                    scheduler.schedule(() -> ship.runWithLogs());
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
