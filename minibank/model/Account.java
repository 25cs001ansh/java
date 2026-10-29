package minibank.model;

import minibank.exception.BankException;
import minibank.exception.InsufficientFundsException;
import minibank.exception.InvalidAmountException;
import minibank.model.annotation.Id;
import minibank.model.annotation.Positive;

public abstract class Account implements Transactable, InterestBearing {

    @Id
    private final String accountNumber;
    private String ownerName;
    @Positive
    private long balance;
    private boolean active;

    private static long accountCounter = 0;

    public Account(String ownerName, long openingBalance) {
        this.accountNumber = generateAccountNumber();
        this.ownerName = ownerName;
        this.balance = openingBalance;
        this.active = true;
    }

    public Account(String ownerName) {
        this(ownerName, 0);
    }

    private static String generateAccountNumber() {
        accountCounter++;
        return String.format("AC%04d", accountCounter);
    }

    public abstract double interestRate();

    public abstract boolean canWithdraw(long amount);

    public synchronized void deposit(long amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Deposit amount must be positive: " + amount);
        }
        balance += amount;
    }

    public synchronized void withdraw(long amount) throws InsufficientFundsException, InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be positive: " + amount);
        }
        if (!canWithdraw(amount)) {
            long shortfall = amount - balance;
            if (shortfall < 0) {
                shortfall = 0;
            }
            throw new InsufficientFundsException(
                    "Insufficient funds in account " + accountNumber, shortfall);
        }
        balance -= amount;
    }

    /**
     * Deadlock-prone version: withdraw() locks "this" first, then deposit()
     * locks "to", with no consistent ordering across accounts. If one thread
     * runs a.transferUnsafe(b, x) while another runs b.transferUnsafe(a, y)
     * at the same time, each thread can end up holding one account's lock
     * while waiting forever for the other's -> deadlock.
     */
    public void transferUnsafe(Account to, long amount) throws BankException {
        try {
            this.withdraw(amount);
            to.deposit(amount);
        } catch (InsufficientFundsException | InvalidAmountException e) {
            throw e;
        } finally {
            System.out.println("Unsafe transfer attempt from " + accountNumber
                    + " to " + to.getAccountNumber() + " processed.");
        }
    }

    /**
     * Deadlock-free version: both accounts are locked in a consistent order
     * (the account with the smaller accountNumber first), regardless of which
     * account transfer() was called on. Two threads transferring in opposite
     * directions (A to B and B to A) then always try to acquire the locks in
     * the same order, so they can never hold one lock while waiting on the
     * other.
     */
    public void transfer(Account to, long amount) throws BankException {
        Account lockFirst = this.accountNumber.compareTo(to.accountNumber) <= 0 ? this : to;
        Account lockSecond = this.accountNumber.compareTo(to.accountNumber) <= 0 ? to : this;

        synchronized (lockFirst) {
            synchronized (lockSecond) {
                try {
                    this.withdraw(amount);
                    to.deposit(amount);
                } catch (InsufficientFundsException | InvalidAmountException e) {
                    throw e;
                } finally {
                    System.out.println("Transfer attempt from " + accountNumber
                            + " to " + to.getAccountNumber() + " processed.");
                }
            }
        }
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public long getBalance() {
        return balance;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public String toString() {
        return "Account[number=" + accountNumber + ", owner=" + ownerName +
                ", balance=" + balance + ", active=" + active + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Account)) {
            return false;
        }
        Account other = (Account) o;
        return accountNumber.equals(other.accountNumber);
    }

    @Override
    public int hashCode() {
        return accountNumber.hashCode();
    }
}