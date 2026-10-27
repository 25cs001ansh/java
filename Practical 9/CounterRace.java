import java.util.concurrent.CountDownLatch;

public class CounterRace {
    private static final int THREAD_COUNT = 8;
    private static final int INCREMENTS_PER_THREAD = 100_000;

    private static class Counter {
        private int value;

        void incrementUnsynchronized() {
            int current = value;
            Thread.yield();
            value = current + 1;
        }

        synchronized void incrementSynchronized() {
            value++;
        }

        int getValue() {
            return value;
        }
    }

    private static int runCounter(boolean useSynchronization) throws InterruptedException {
        Counter counter = new Counter();
        CountDownLatch start = new CountDownLatch(1);
        Thread[] threads = new Thread[THREAD_COUNT];

        for (int i = 0; i < THREAD_COUNT; i++) {
            threads[i] = new Thread(() -> {
                try {
                    start.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new IllegalStateException("Counter worker was interrupted", e);
                }

                for (int j = 0; j < INCREMENTS_PER_THREAD; j++) {
                    if (useSynchronization) {
                        counter.incrementSynchronized();
                    } else {
                        counter.incrementUnsynchronized();
                    }
                }
            });
            threads[i].start();
        }

        start.countDown();
        for (Thread thread : threads) {
            thread.join();
        }
        return counter.getValue();
    }

    public static void main(String[] args) throws InterruptedException {
        int expected = THREAD_COUNT * INCREMENTS_PER_THREAD;

        System.out.println("Without synchronization: " + runCounter(false)
                + " (expected " + expected + ")");
        System.out.println("With synchronization:    " + runCounter(true)
                + " (expected " + expected + ")");
    }
}
