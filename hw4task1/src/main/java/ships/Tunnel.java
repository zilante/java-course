package ships;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Class for accessing tunnel
 */
public class Tunnel {
    private final Semaphore available = new Semaphore(5, true);

    public void goThrough() throws InterruptedException {
        available.acquire();
        TimeUnit.SECONDS.sleep(1);
        available.release();
    }
}
