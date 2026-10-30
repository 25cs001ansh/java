package minibank;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import minibank.exception.InsufficientFundsException;
import minibank.exception.InvalidAmountException;
import minibank.model.Account;
import minibank.model.SavingsAccount;
import minibank.service.AccountWorker;

public class AccountTest {

    @Test
    void depositIncreasesBalance() throws InvalidAmountException {
        Account account = new SavingsAccount("Test User", 1000, 0);
        account.deposit(500);
        assertEquals(1500, account.getBalance());
    }

    @Test
    void withdrawalOverLimitThrowsException() {
        Account account = new SavingsAccount("Test User", 1000, 500);
        assertThrows(InsufficientFundsException.class, () -> account.withdraw(600));
    }

    @Test
    void concurrentDepositsKeepBalanceCorrect() throws InterruptedException {
        Account account = new SavingsAccount("Test User", 0, 0);
        int threadCount = 10;
        int timesEach = 100;
        long amountEach = 10;

        Thread[] threads = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(new AccountWorker(account, timesEach, amountEach));
        }
        for (Thread t : threads) {
            t.start();
        }
        for (Thread t : threads) {
            t.join();
        }

        assertEquals((long) threadCount * timesEach * amountEach, account.getBalance());
    }
}