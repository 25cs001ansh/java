package minibank.service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import minibank.model.Account;

public class TransactionProcessor {

    private final ExecutorService executorService;
    private final ConcurrentHashMap<String, Account> accounts = new ConcurrentHashMap<>();

    public TransactionProcessor() {
        this.executorService = Executors.newFixedThreadPool(4);
    }

    public void addAccount(Account account) {
        accounts.put(account.getAccountNumber(), account);
    }

    public Account getAccount(String accountNumber) {
        return accounts.get(accountNumber);
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