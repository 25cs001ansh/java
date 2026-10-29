package minibank.service;

import minibank.exception.InsufficientFundsException;
import minibank.exception.InvalidAmountException;
import minibank.model.Account;
import minibank.model.TransactionType;

public class TransactionTask implements Runnable {

    private final Account account;
    private final TransactionType type;
    private final long amount;

    public TransactionTask(Account account, TransactionType type, long amount) {
        this.account = account;
        this.type = type;
        this.amount = amount;
    }

    @Override
    public void run() {
        try {
            if (type == TransactionType.DEPOSIT) {
                account.deposit(amount);
            } else if (type == TransactionType.WITHDRAW) {
                account.withdraw(amount);
            }
        } catch (InsufficientFundsException | InvalidAmountException e) {
            System.out.println(Thread.currentThread().getName() + " transaction failed: " + e.getMessage());
        }
    }
}