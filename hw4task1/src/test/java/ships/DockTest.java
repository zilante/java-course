package ships;

import org.junit.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class DockTest {
    private Random random = new Random();

    @BeforeClass
    public static void start() {
        System.out.println("Testing started");
    }

    @AfterClass
    public static void finish() {
        System.out.println("Testing finished");
    }

    @Test
    public void serveSingleShipTest() throws InterruptedException {
        Dock dock = new Dock();

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        DateTimeFormatter df = DateTimeFormatter.ofPattern("HH:mm:ss");

        Ship ship = Ship.createRandom(random);

        executorService.submit(() -> {
            while (ship.getCapacity().getGoodCount() != ship.getGoodCount()) {
                try {
                    dock.loadGoods(ship);
                    System.out.println("Part of goods was loaded at "
                            + df.format(LocalDateTime.now()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        TimeUnit.SECONDS.sleep(7);
        executorService.shutdown();
    }

    @Test
    public void serveSeveralShipsTest() throws InterruptedException {
        Dock dock = new Dock();

        final int shipCount = 5;
        ExecutorService executorService = Executors.newFixedThreadPool(shipCount);

        DateTimeFormatter df = DateTimeFormatter.ofPattern("HH:mm:ss");

        for (int i = 0; i < shipCount; ++i) {
            Ship ship = Ship.createRandom(random);

            executorService.submit(() -> {
                while (ship.getCapacity().getGoodCount() != ship.getGoodCount()) {
                    try {
                        dock.loadGoods(ship);
                        System.out.println("Part of goods was loaded at "
                                + df.format(LocalDateTime.now()));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        TimeUnit.SECONDS.sleep(50);
        executorService.shutdown();
    }
}
