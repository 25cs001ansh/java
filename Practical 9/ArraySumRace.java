import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;

public class ArraySumRace {

    private static final int SIZE = 10_000_000;
    private static final int THREADS = 4;
    private static final int[] NUMBERS = new int[SIZE];

    // Shared variables for test runs
    private static long unsafeTotal = 0;
    private static long synchronizedTotal = 0;
    private static final Object lock = new Object();
    private static final AtomicLong atomicTotal = new AtomicLong(0);

    public static void main(String[] args) throws InterruptedException {
        // Initialize array with 1s so expected sum is exactly SIZE (10,000,000)
        Arrays.fill(NUMBERS, 1);
        long expectedSum = SIZE;

        System.out.println("=== Multithreaded Array Sum Comparison ===");
        System.out.println("Array Size: " + SIZE + " elements | Threads: " + THREADS);
        System.out.println("Expected Sum: " + expectedSum + "\n");

        int chunkSize = SIZE / THREADS;

        // -------------------------------------------------------------
        // 1. Unsynchronized (Shows Race Condition / Wrong Result)
        // -------------------------------------------------------------
        unsafeTotal = 0;
        Thread[] threads1 = new Thread[THREADS];
        long start1 = System.currentTimeMillis();

        for (int t = 0; t < THREADS; t++) {
            final int startIdx = t * chunkSize;
            final int endIdx = (t == THREADS - 1) ? SIZE : startIdx + chunkSize;
            threads1[t] = new Thread(() -> {
                for (int i = startIdx; i < endIdx; i++) {
                    unsafeTotal += NUMBERS[i]; // Non-atomic write -> race condition
                }
            });
            threads1[t].start();
        }
        for (Thread t : threads1) t.join();
        long time1 = System.currentTimeMillis() - start1;

        System.out.println("1. Unsynchronized Sum : " + unsafeTotal +
                " (Wrong? " + (unsafeTotal != expectedSum) + ") | Time: " + time1 + " ms");

        // -------------------------------------------------------------
        // 2. Fix 1: Synchronized Block (Thread-Safe, but lock contention)
        // -------------------------------------------------------------
        synchronizedTotal = 0;
        Thread[] threads2 = new Thread[THREADS];
        long start2 = System.currentTimeMillis();

        for (int t = 0; t < THREADS; t++) {
            final int startIdx = t * chunkSize;
            final int endIdx = (t == THREADS - 1) ? SIZE : startIdx + chunkSize;
            threads2[t] = new Thread(() -> {
                for (int i = startIdx; i < endIdx; i++) {
                    synchronized (lock) {
                        synchronizedTotal += NUMBERS[i];
                    }
                }
            });
            threads2[t].start();
        }
        // Run with fewer items if synchronized takes too long or use batches
        // To keep fast, synchronize in chunks or Atomic
        for (Thread t : threads2) t.join();
        long time2 = System.currentTimeMillis() - start2;

        System.out.println("2. Fix 1 (Synchronized): " + synchronizedTotal +
                " (Correct? " + (synchronizedTotal == expectedSum) + ") | Time: " + time2 + " ms");

        // -------------------------------------------------------------
        // 3. Fix 2: Thread-Local Partial Sums (Optimal & Thread-Safe)
        // -------------------------------------------------------------
        long[] partialSums = new long[THREADS];
        Thread[] threads3 = new Thread[THREADS];
        long start3 = System.currentTimeMillis();

        for (int t = 0; t < THREADS; t++) {
            final int threadId = t;
            final int startIdx = t * chunkSize;
            final int endIdx = (t == THREADS - 1) ? SIZE : startIdx + chunkSize;
            threads3[t] = new Thread(() -> {
                long localSum = 0;
                for (int i = startIdx; i < endIdx; i++) {
                    localSum += NUMBERS[i];
                }
                partialSums[threadId] = localSum;
            });
            threads3[t].start();
        }
        for (Thread t : threads3) t.join();

        long optimalTotal = 0;
        for (long p : partialSums) optimalTotal += p;
        long time3 = System.currentTimeMillis() - start3;

        System.out.println("3. Fix 2 (Local Sums)  : " + optimalTotal +
                " (Correct? " + (optimalTotal == expectedSum) + ") | Time: " + time3 + " ms");

        System.out.println("\n=== Summary ===");
        System.out.println("- Unsynchronized results in data loss due to lost updates.");
        System.out.println("- Fix 1 guarantees correctness but suffers heavy lock contention overhead.");
        System.out.println("- Fix 2 computes independent thread-local sums and achieves maximum speed & accuracy.");
    }
}

