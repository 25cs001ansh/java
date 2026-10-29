package minibank.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TransactionProcessor {

    private final ExecutorService executorService;

    public TransactionProcessor() {
        this.executorService = Executors.newFixedThreadPool(4);
    }

    public void submit(Runnable task) {
        executorService.execute(task);
    }

    public void stop() {
        executorService.shutdown();
        try {
            executorService.awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}