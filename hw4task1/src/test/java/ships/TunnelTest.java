package ships;

import org.junit.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TunnelTest {
    @BeforeClass
    public static void start() {
        System.out.println("Testing started");
    }

    @AfterClass
    public static void finish() {
        System.out.println("Testing finished");
    }

    @Test
    public void singleShipThroughTunnelTest() throws InterruptedException {
        Tunnel tunnel = new Tunnel();

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        DateTimeFormatter df = DateTimeFormatter.ofPattern("HH:mm:ss");

        executorService.submit(() -> {
            for (int j = 0; j < 15; ++j) {
                try {
                    tunnel.goThrough();
                    System.out.println("I have passed through tunnel at "
                            + df.format(LocalDateTime.now()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        TimeUnit.SECONDS.sleep(17);
        executorService.shutdown();
    }

    @Test
    public void severalShipsThroughTunnelTest() throws InterruptedException {
        Tunnel tunnel = new Tunnel();

        final int shipCount = 5;
        ExecutorService executorService = Executors.newFixedThreadPool(shipCount);

        DateTimeFormatter df = DateTimeFormatter.ofPattern("HH:mm:ss");

        for (int i = 0; i < shipCount; ++i) {
            executorService.submit(() -> {
                for (int j = 0; j < 5; ++j) {
                    try {
                        tunnel.goThrough();
                        System.out.println("I have passed through tunnel at "
                                + df.format(LocalDateTime.now()));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        TimeUnit.SECONDS.sleep(10);
        executorService.shutdown();
    }

    @Test
    public void severalShipsConsequentlyGoThroughTunnelTest() throws InterruptedException {
        Tunnel tunnel = new Tunnel();

        final int shipCount = 15;
        ExecutorService executorService = Executors.newFixedThreadPool(shipCount);

        DateTimeFormatter df = DateTimeFormatter.ofPattern("HH:mm:ss");

        for (int i = 0; i < shipCount; ++i) {
            TimeUnit.MILLISECONDS.sleep(100);
            executorService.submit(() -> {
                try {
                    tunnel.goThrough();
                    System.out.println("I have passed through tunnel at "
                            + df.format(LocalDateTime.now()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        TimeUnit.SECONDS.sleep(17);
        executorService.shutdown();
    }
}