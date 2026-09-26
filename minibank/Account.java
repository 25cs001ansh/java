package minibank;

import java.util.Objects;

public class Account {

    private final String accountNumber;
    private String ownerName;
    private long balance;
    private boolean active;

    private static long accountCounter = 0;

    // ----------------------------------------------------------------
    // Constructors
    // ----------------------------------------------------------------

    public Account(String ownerName, long openingBalance) {
        this.accountNumber = generateAccountNumber();
        this.ownerName = ownerName;
        this.balance = openingBalance;
        this.active = true;
    }

    public Account(String ownerName) {
        this(ownerName, 0);
    }

    // ----------------------------------------------------------------
    // Private helper
    // ----------------------------------------------------------------

    private static String generateAccountNumber() {
        accountCounter++;
        return String.format("AC%04d", accountCounter);
    }

    // ----------------------------------------------------------------
    // Business methods
    // ----------------------------------------------------------------

    public void deposit(long amount) {
        balance += amount;
    }

    public boolean withdraw(long amount) {
        if (amount <= balance) {
            balance -= amount;
            return true;
        }
        return false;
    }

    // ----------------------------------------------------------------
    // Getters
    // ----------------------------------------------------------------

    public String  getAccountNumber() { return accountNumber; }
    public String  getOwnerName()     { return ownerName; }
    public long    getBalance()       { return balance; }
    public boolean isActive()         { return active; }

    // ----------------------------------------------------------------
    // (1) toString – readable line with accountNumber, ownerName, balance
    // ----------------------------------------------------------------

    @Override
    public String toString() {
        return "Account[number=" + accountNumber
                + ", owner=" + ownerName
                + ", balance=" + balance + "]";
    }

    // ----------------------------------------------------------------
    // (2) equals – two accounts are equal when accountNumber matches
    // ----------------------------------------------------------------

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;                // same reference
        if (!(o instanceof Account)) return false; // null or wrong type
        Account other = (Account) o;
        return accountNumber.equals(other.accountNumber);
    }

    // ----------------------------------------------------------------
    // (2) hashCode – consistent with equals
    // ----------------------------------------------------------------

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber);
    }
}
