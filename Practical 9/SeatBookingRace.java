import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.atomic.AtomicInteger;

public class SeatBookingRace {
    private static final int INITIAL_SEATS = 5;
    private static final int THREAD_COUNT = 10;

    private static class SeatBooking {
        private int seatsLeft = INITIAL_SEATS;
        private final CyclicBarrier checkBarrier;

        SeatBooking(boolean coordinateRace) {
            checkBarrier = coordinateRace ? new CyclicBarrier(THREAD_COUNT) : null;
        }

        boolean bookUnsynchronized() throws InterruptedException, BrokenBarrierException {
            if (seatsLeft <= 0) {
                return false;
            }

            // Hold each caller after its availability check to expose the race.
            checkBarrier.await();
            seatsLeft--;
            return true;
        }

        synchronized boolean bookSynchronized() {
            if (seatsLeft <= 0) {
                return false;
            }

            seatsLeft--;
            return true;
        }

        int getSeatsLeft() {
            return seatsLeft;
        }
    }

    private static int runBookings(boolean useSynchronization) throws InterruptedException {
        SeatBooking booking = new SeatBooking(!useSynchronization);
        AtomicInteger successfulBookings = new AtomicInteger();
        CountDownLatch start = new CountDownLatch(1);
        Thread[] threads = new Thread[THREAD_COUNT];

        for (int i = 0; i < THREAD_COUNT; i++) {
            threads[i] = new Thread(() -> {
                try {
                    start.await();
                    boolean booked = useSynchronization
                            ? booking.bookSynchronized()
                            : booking.bookUnsynchronized();
                    if (booked) {
                        successfulBookings.incrementAndGet();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new IllegalStateException("Booking worker was interrupted", e);
                } catch (BrokenBarrierException e) {
                    throw new IllegalStateException("Booking worker could not complete", e);
                }
            });
            threads[i].start();
        }

        start.countDown();
        for (Thread thread : threads) {
            thread.join();
        }

        System.out.println((useSynchronization ? "With" : "Without")
                + " synchronization: " + successfulBookings.get()
                + " successful bookings, " + booking.getSeatsLeft() + " seats left");
        return successfulBookings.get();
    }

    public static void main(String[] args) throws InterruptedException {
        runBookings(false);
        runBookings(true);
    }
}
