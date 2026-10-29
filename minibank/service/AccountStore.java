package minibank.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import minibank.model.Account;

public class AccountStore {

    private final Map<String, Account> accounts = new HashMap<>();
    private final ArrayList<String> transactionHistory = new ArrayList<>();

    public void add(Account account) {
        accounts.put(account.getAccountNumber(), account);
    }

    public Account get(String accountNumber) {
        return accounts.get(accountNumber);
    }

    public ArrayList<Account> getAll() {
        return new ArrayList<>(accounts.values());
    }

    public void logTransaction(String entry) {
        transactionHistory.add(entry);
    }

    public ArrayList<String> getTransactionHistory() {
        return transactionHistory;
    }

    public ArrayList<Account> sortedByBalance() {
        ArrayList<Account> sorted = new ArrayList<>(accounts.values());
        sorted.sort(Comparator.comparingLong(Account::getBalance));
        return sorted;
    }
}