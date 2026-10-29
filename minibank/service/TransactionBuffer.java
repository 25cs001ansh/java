package minibank.service;

public class TransactionBuffer {

    private final Runnable[] buffer;
    private int count = 0;

    public TransactionBuffer(int capacity) {
        this.buffer = new Runnable[capacity];
    }

    public synchronized void produce(Runnable task) throws InterruptedException {
        while (count == buffer.length) {
            wait();
        }
        buffer[count] = task;
        count++;
        notify();
    }

    public synchronized Runnable consume() throws InterruptedException {
        while (count == 0) {
            wait();
        }
        count--;
        Runnable task = buffer[count];
        buffer[count] = null;
        notify();
        return task;
    }
}