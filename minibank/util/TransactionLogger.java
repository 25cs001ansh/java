package minibank.util;

public class TransactionLogger implements AutoCloseable {

    public TransactionLogger() {
        System.out.println("TransactionLogger opened.");
    }

    public void log(String message) {
        System.out.println("[LOG] " + message);
    }

    @Override
    public void close() {
        System.out.println("TransactionLogger closed.");
    }
}