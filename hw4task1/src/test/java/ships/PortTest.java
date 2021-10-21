package ships;

import org.junit.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PortTest {
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
    public void portTest() throws InterruptedException {
        Port port = new Port();

        port.startWithLogs();
        TimeUnit.SECONDS.sleep(random.nextInt(100));
        port.finish();
    }
}
