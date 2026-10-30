package minibank.service;

import java.util.ArrayList;

import minibank.exception.AccountNotFoundException;
import minibank.exception.BankException;
import minibank.exception.InsufficientFundsException;
import minibank.exception.InvalidAmountException;
import minibank.model.Account;
import minibank.model.CurrentAccount;
import minibank.model.FixedDepositAccount;
import minibank.model.SavingsAccount;
import minibank.model.annotation.Audit;
import minibank.util.AuditLogger;
import minibank.util.StatementFormatter;
import minibank.util.Validator;

public class BankService {

    private final Repository<Account> accountRepository;

    public BankService(Repository<Account> accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Audit
    public Account openAccount(String accountType, String ownerName, String mobile,
            long openingBalance, long limitValue) {
        AuditLogger.logIfAudited(this, "openAccount");

        if (!Validator.isValidMobile(mobile)) {
            throw new IllegalArgumentException("Invalid mobile number: " + mobile);
        }

        Account account;
        switch (accountType.toUpperCase()) {
            case "SAVINGS" -> account = new SavingsAccount(ownerName, openingBalance, limitValue);
            case "CURRENT" -> account = new CurrentAccount(ownerName, openingBalance, limitValue);
            case "FD" -> account = new FixedDepositAccount(ownerName, openingBalance);
            default -> throw new IllegalArgumentException("Unknown account type: " + accountType);
        }

        accountRepository.save(account);
        return account;
    }

    @Audit
    public void deposit(String accountNumber, long amount)
            throws AccountNotFoundException, InvalidAmountException {
        AuditLogger.logIfAudited(this, "deposit");
        Account account = findAccountOrThrow(accountNumber);
        account.deposit(amount);
    }

    @Audit
    public void withdraw(String accountNumber, long amount)
            throws AccountNotFoundException, InsufficientFundsException, InvalidAmountException {
        AuditLogger.logIfAudited(this, "withdraw");
        Account account = findAccountOrThrow(accountNumber);
        account.withdraw(amount);
    }

    @Audit
    public void transfer(String fromAccountNumber, String toAccountNumber, long amount)
            throws AccountNotFoundException, BankException {
        AuditLogger.logIfAudited(this, "transfer");
        Account from = findAccountOrThrow(fromAccountNumber);
        Account to = findAccountOrThrow(toAccountNumber);
        from.transfer(to, amount);
    }

    public String statement(String accountNumber) throws AccountNotFoundException {
        Account account = findAccountOrThrow(accountNumber);
        return StatementFormatter.buildStatement(account);
    }

    public ArrayList<Account> listAccounts() {
        return accountRepository.findAll();
    }

    private Account findAccountOrThrow(String accountNumber) throws AccountNotFoundException {
        Account account = accountRepository.findById(accountNumber);
        if (account == null) {
            throw new AccountNotFoundException("No account found with number: " + accountNumber);
        }
        return account;
    }
}