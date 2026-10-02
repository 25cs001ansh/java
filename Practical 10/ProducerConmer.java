import java.util.LinkedList;
import java.util.Queue;

public class ProducerConmer {
    private static final int CAPACITY = 3;
    private static final int TOTAL_ITEMS = 10;
    
    private final Queue<Integer> buffer = new LinkedList<>();
    public synchronized void put(int value) throws InterruptedException {
        while (buffer.size() == CAPACITY) {
            wait();
        }
        buffer.add(value);
        System.out.println("Produced: " + value+"buffer="+buffer);
        notifyAll();
        
    }
    public synchronized int take() throws InterruptedException {
        while (buffer.isEmpty()) {
            wait();
        }
        int value = buffer.poll();
        System.out.println("Consumed: " + value+" buffer="+buffer);
        notifyAll();
        return value;
    }
    public static void main(String[] args) {
        ProducerConmer pc = new ProducerConmer();
        Thread consumer = new Thread(() -> {
            try {
                int expected = 1;
                for (int i = 1; i <= TOTAL_ITEMS; i++) {
                    int v=pc.take();
                    if(v!=expected++){
                        System.out.println("Error: Expected "+expected+" but got "+v);
                    }
                    Thread.sleep(120); // Simulate some delay
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "consumer");
        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= TOTAL_ITEMS; i++) {
                    pc.put(i);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "producer");
        producer.start();
        consumer.start();
        try {
            producer.join();
            consumer.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("done" + TOTAL_ITEMS + " items produced and consumed in order.");
    }
}
