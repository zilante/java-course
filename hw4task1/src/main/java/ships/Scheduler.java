package ships;

import java.util.concurrent.ExecutorService;

/**
 * Class which is responsible for scheduling tasks
 */
public class Scheduler {
    ExecutorService executorService;

    /**
     * Scheduler constructor
     * @param executorService
     */
    public Scheduler(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public void schedule(Runnable task) {
        executorService.submit(task);
    }

    public void finishAll() {
        executorService.shutdown();
    }
}
