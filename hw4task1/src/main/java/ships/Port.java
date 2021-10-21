package ships;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;

/**
 * Main class for port, from which execution of all tasks started
 */
public class Port {
    Generator generator;
    Tunnel tunnel;
    Dock breadDock;
    Dock bananaDock;
    Dock clothesDock;
    Scheduler scheduler;

    /**
     * Port constructor
     */
    public Port() {
        Queue<Ship> breadShips = new ConcurrentLinkedQueue<>();
        Queue<Ship> bananaShips = new ConcurrentLinkedQueue<>();
        Queue<Ship> clothesShips = new ConcurrentLinkedQueue<>();

        tunnel = new Tunnel();
        generator = new Generator(breadShips, bananaShips, clothesShips, tunnel);

        scheduler = new Scheduler(Executors.newCachedThreadPool());

        breadDock = new Dock(breadShips, scheduler);
        bananaDock = new Dock(bananaShips, scheduler);
        clothesDock = new Dock(clothesShips, scheduler);
    }

    /**
     * Start execution of all tasks
     */
    public void start() {
        scheduler.schedule(() -> generator.run());
        scheduler.schedule(() -> breadDock.run());
        scheduler.schedule(() -> bananaDock.run());
        scheduler.schedule(() -> clothesDock.run());
    }

    /**
     * Method is analogue of previous method, but with logging
     */
    public void startWithLogs() {
        scheduler.schedule(() -> generator.run());
        scheduler.schedule(() -> breadDock.runWithLogs());
        scheduler.schedule(() -> bananaDock.runWithLogs());
        scheduler.schedule(() -> clothesDock.runWithLogs());
    }

    /**
     * Finish execution of all tasks
     */
    public void finish() {
        generator.setTimeToTerminate();
        breadDock.setTimeToTerminate();
        bananaDock.setTimeToTerminate();
        clothesDock.setTimeToTerminate();

        scheduler.finishAll();
    }
}
