package minibank;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

import minibank.model.Account;
import minibank.model.BankInfo;
import minibank.model.Command;
import minibank.model.CurrentAccount;
import minibank.model.Customer;
import minibank.model.FixedDepositAccount;
import minibank.model.MenuOption;
import minibank.model.SavingsAccount;
import minibank.model.TransactionType;
import minibank.exception.BankException;
import minibank.exception.InsufficientFundsException;
import minibank.exception.InvalidAmountException;
import minibank.service.AccountStore;
import minibank.service.AccountWorker;
import minibank.service.Repository;
import minibank.service.TransactionBuffer;
import minibank.service.TransactionProcessor;
import minibank.service.TransactionTask;
import minibank.service.WithdrawRule;
import minibank.util.AnnotationValidator;
import minibank.util.CommandParser;
import minibank.util.ReportGenerator;
import minibank.util.StatePersister;
import minibank.util.StatementFormatter;
import minibank.util.TransactionLog;
import minibank.util.TransactionLogger;
import minibank.util.Validator;

import static java.lang.System.out;

public class MiniBank {

    public static void main(String[] args) {
        BankInfo bankInfo = new BankInfo("MiniBank", "Main Branch");
        System.out.println("===================================");
        System.out.println(bankInfo);
        System.out.println("===================================");

        // ---- Customer & Account demo (Account is abstract; use subclasses) ----
        Account[] accounts = new Account[3];
        accounts[0] = new SavingsAccount("Ansh Patel", 5000, 1000);
        accounts[1] = new CurrentAccount("Riya Shah", 0, 2000);
        accounts[2] = new FixedDepositAccount("Karan Mehta", 2000);

        try {
            accounts[0].deposit(1500);
            accounts[1].deposit(3000);
        } catch (InvalidAmountException e) {
            System.out.println("Deposit failed: " + e.getMessage());
        } finally {
            System.out.println("Initial deposits processed.");
        }

        try {
            accounts[2].withdraw(500);
        } catch (InsufficientFundsException e) {
            System.out.println(accounts[2].getOwnerName() + "'s withdrawal failed: " + e.getMessage()
                    + " (shortfall: " + e.getShortfall() + ")");
        } catch (InvalidAmountException e) {
            System.out.println(accounts[2].getOwnerName() + "'s withdrawal failed: " + e.getMessage());
        } finally {
            System.out.println(accounts[2].getOwnerName() + "'s withdrawal attempt finished.");
        }

        System.out.println("\n----- Account Summary (toString) -----");
        for (Account acc : accounts) {
            System.out.println(acc);
        }
        System.out.println("---------------------------------------\n");

        // ---- Polymorphism demo: interestRate() resolved at runtime for each type ----
        System.out.println("----- Interest Rates (runtime polymorphism) -----");
        for (Account acc : accounts) {
            System.out.println(acc.getAccountNumber() + " (" + acc.getClass().getSimpleName()
                    + ") -> interest rate: " + acc.interestRate() + "%");
        }
        System.out.println("--------------------------------------------------\n");

        // instanceof pattern check: handle CurrentAccount specially
        for (Account acc : accounts) {
            if (acc instanceof CurrentAccount currentAccount) {
                System.out.println(acc.getAccountNumber() + " is a CurrentAccount with overdraft limit: "
                        + currentAccount.getOverdraftLimit());
            }
        }
        System.out.println();

        // equals() demo: same accountNumber => equal, different => not equal
        Account duplicateOfFirst = accounts[0];
        System.out.println("accounts[0].equals(duplicateOfFirst): "
                + accounts[0].equals(duplicateOfFirst));
        System.out.println("accounts[0].equals(accounts[1]): "
                + accounts[0].equals(accounts[1]));

        // instanceof demo
        Object obj = accounts[2];
        if (obj instanceof Account) {
            System.out.println(obj + " is an instance of Account");
        }

        // Customer with nested Address and clone() demo
        Customer.Address address = new Customer.Address("221B Ring Road", "Ahmedabad", "380001");
        Customer customer = new Customer("Ansh Patel", "ansh@example.com", "9876543210", address);
        Customer clonedCustomer = customer.clone();
        System.out.println("\nOriginal customer: " + customer);
        System.out.println("Cloned customer:   " + clonedCustomer);
        System.out.println();

        // ---- Validator demo: one correct and one wrong input each ----
        System.out.println("----- Validator Tests -----");
        System.out.println("Mobile 9876543210 (correct): " + Validator.isValidMobile("9876543210"));
        System.out.println("Mobile 12345 (wrong):        " + Validator.isValidMobile("12345"));

        System.out.println("Email ansh@example.com (correct): " + Validator.isValidEmail("ansh@example.com"));
        System.out.println("Email ansh@@example (wrong):       " + Validator.isValidEmail("ansh@@example"));

        System.out.println("PAN ABCDE1234F (correct): " + Validator.isValidPan("ABCDE1234F"));
        System.out.println("PAN ABC1234F (wrong):      " + Validator.isValidPan("ABC1234F"));

        System.out.println("IFSC HDFC0001234 (correct): " + Validator.isValidIfsc("HDFC0001234"));
        System.out.println("IFSC HDFC1234 (wrong):       " + Validator.isValidIfsc("HDFC1234"));
        System.out.println("----------------------------\n");

        // ---- CommandParser demo ----
        Command command = CommandParser.parse("DEPOSIT AC0001 500");
        System.out.println("----- Parsed Command -----");
        System.out.println("Type          : " + command.type());
        System.out.println("Account Number: " + command.accountNumber());
        System.out.println("Amount        : " + command.amount());
        System.out.println("---------------------------\n");

        // ---- StatementFormatter demo ----
        System.out.println(StatementFormatter.buildStatement(accounts[0]));
        System.out.println();

        // ---- WithdrawRule demo: anonymous class, then lambda ----
        WithdrawRule anonymousRule = new WithdrawRule() {
            @Override
            public boolean allow(Account account, long amount) {
                return account.getBalance() >= amount;
            }
        };

        WithdrawRule lambdaRule = (account, amount) -> account.canWithdraw(amount);

        out.println("----- WithdrawRule Demo -----");
        out.println("Anonymous class rule allows withdrawing 1000 from " + accounts[0].getAccountNumber()
                + ": " + anonymousRule.allow(accounts[0], 1000));
        out.println("Lambda rule allows withdrawing 1000 from " + accounts[0].getAccountNumber()
                + ": " + lambdaRule.allow(accounts[0], 1000));
        out.println("------------------------------\n");

        // ---- try-with-resources demo using a custom AutoCloseable, plus transfer() ----
        System.out.println("----- Transfer Demo (try-with-resources) -----");
        try (TransactionLogger logger = new TransactionLogger()) {
            logger.log("Attempting transfer of 1000 from " + accounts[0].getAccountNumber()
                    + " to " + accounts[1].getAccountNumber());
            try {
                accounts[0].transfer(accounts[1], 1000);
                logger.log("Transfer succeeded.");
            } catch (BankException e) {
                System.out.println("Transfer failed: " + e.getMessage());
            } finally {
                System.out.println("Transfer block finished.");
            }
        }
        System.out.println("------------------------------------------------\n");

        // ---- AnnotationValidator demo: an account with a negative balance ----
        Account invalidAccount = new SavingsAccount("Test User", -500, 1000);
        String[] validationErrors = AnnotationValidator.validate(invalidAccount);

        System.out.println("----- AnnotationValidator Demo -----");
        if (validationErrors.length == 0) {
            System.out.println("No validation errors found.");
        } else {
            for (String error : validationErrors) {
                System.out.println("Validation error: " + error);
            }
        }
        System.out.println("-------------------------------------\n");

        // ---- AccountWorker thread demo ----
        Account threadDemoAccount = new SavingsAccount("Thread Demo User", 0, 0);

        int threadCount = 10;
        Thread[] threads = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(new AccountWorker(threadDemoAccount, 100, 10));
            threads[i].setName("Worker-" + (i + 1));
        }

        for (Thread t : threads) {
            t.start();
        }

        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("----- Thread Names -----");
        for (Thread t : threads) {
            System.out.println(t.getName());
        }
        System.out.println("-------------------------");

        System.out.println("Final balance after concurrent deposits: " + threadDemoAccount.getBalance());
        System.out.println();

        // ---- TransactionProcessor demo: fixed thread pool ----
        Account poolAccount = new CurrentAccount("Pool Demo User", 0, 5000);
        TransactionProcessor processor = new TransactionProcessor();

        for (int i = 0; i < 20; i++) {
            TransactionType type = (i % 2 == 0) ? TransactionType.DEPOSIT : TransactionType.WITHDRAW;
            processor.submit(new TransactionTask(poolAccount, type, 50));
        }

        processor.stop();
        System.out.println("----- TransactionProcessor Demo -----");
        System.out.println("Final balance after pooled transactions: " + poolAccount.getBalance());
        System.out.println("--------------------------------------\n");

        // ---- Producer-consumer demo: wait()/notify() on a shared buffer ----
        TransactionBuffer buffer = new TransactionBuffer(5);
        Account pcAccount = new SavingsAccount("Producer Consumer User", 1000, 0);

        Thread producer = new Thread(() -> {
            try {
                for (int i = 0; i < 5; i++) {
                    buffer.produce(new TransactionTask(pcAccount, TransactionType.DEPOSIT, 100));
                    System.out.println(Thread.currentThread().getName() + " produced transaction " + (i + 1));
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Producer");

        Thread consumer = new Thread(() -> {
            try {
                for (int i = 0; i < 5; i++) {
                    Runnable task = buffer.consume();
                    task.run();
                    System.out.println(Thread.currentThread().getName() + " consumed transaction " + (i + 1));
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Consumer");

        producer.start();
        consumer.start();

        try {
            producer.join();
            consumer.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("----- Producer-Consumer Demo -----");
        System.out.println("Final balance: " + pcAccount.getBalance());
        System.out.println("-----------------------------------\n");

        // ---- Deadlock avoidance demo: consistent lock ordering in transfer() ----
        Account accA = new SavingsAccount("Account A", 5000, 0);
        Account accB = new SavingsAccount("Account B", 5000, 0);

        Thread transferAtoB = new Thread(() -> {
            try {
                accA.transfer(accB, 100);
            } catch (BankException e) {
                System.out.println("Transfer A->B failed: " + e.getMessage());
            }
        }, "Transfer-A-to-B");

        Thread transferBtoA = new Thread(() -> {
            try {
                accB.transfer(accA, 100);
            } catch (BankException e) {
                System.out.println("Transfer B->A failed: " + e.getMessage());
            }
        }, "Transfer-B-to-A");

        transferAtoB.start();
        transferBtoA.start();

        try {
            transferAtoB.join();
            transferBtoA.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("----- Deadlock Avoidance Demo -----");
        System.out.println("Completed without deadlock. Balance A: " + accA.getBalance()
                + ", Balance B: " + accB.getBalance());
        System.out.println("------------------------------------\n");

        // ---- Persistence demo: save accounts, reload, confirm survival ----
        Path dataDir = Path.of("data");
        Path logsDir = dataDir.resolve("logs");
        Path accountsFile = dataDir.resolve("accounts.dat");
        Path logFile = logsDir.resolve("transactions.log");
        Path reportFile = dataDir.resolve("report.txt");

        try {
            Files.createDirectories(logsDir);
        } catch (IOException e) {
            System.out.println("Could not create data directories: " + e.getMessage());
        }

        try {
            StatePersister.save(accounts, accountsFile);
            System.out.println("Accounts saved to " + accountsFile);
        } catch (IOException e) {
            System.out.println("Save failed: " + e.getMessage());
        }

        try {
            Account[] reloadedAccounts = StatePersister.load(accountsFile);
            System.out.println("----- Reloaded Accounts -----");
            for (Account acc : reloadedAccounts) {
                System.out.println(acc);
            }
            System.out.println("------------------------------\n");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Load failed: " + e.getMessage());
        }

        // ---- Transaction log demo ----
        try {
            TransactionLog.append(logFile, "DEPOSIT " + accounts[0].getAccountNumber() + " 500");
            TransactionLog.append(logFile, "WITHDRAW " + accounts[1].getAccountNumber() + " 200");
            TransactionLog.append(logFile, "DEPOSIT " + accounts[2].getAccountNumber() + " 300");
        } catch (IOException e) {
            System.out.println("Logging failed: " + e.getMessage());
        }

        // ---- Report generation demo ----
        try {
            String report = ReportGenerator.generateReport(logsDir, reportFile);
            System.out.println(report);
        } catch (IOException e) {
            System.out.println("Report generation failed: " + e.getMessage());
        }

        // ---- AccountStore demo: add accounts, print sorted by balance ----
        AccountStore accountStore = new AccountStore();
        for (Account acc : accounts) {
            accountStore.add(acc);
        }

        System.out.println("----- Accounts Sorted By Balance -----");
        for (Account acc : accountStore.sortedByBalance()) {
            System.out.println(acc);
        }
        System.out.println("---------------------------------------\n");

        // ---- Repository<Account> demo ----
        Repository<Account> accountRepository = new Repository<>();
        accountRepository.saveAll(accounts);

        Account fetchedAccount = accountRepository.findById(accounts[0].getAccountNumber());
        System.out.println("----- Repository Demo -----");
        System.out.println("Fetched by id " + accounts[0].getAccountNumber() + ": " + fetchedAccount);
        for (Account acc : accountRepository.findAll()) {
            System.out.println(acc);
        }
        System.out.println("-----------------------------\n");

        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n----- MiniBank Menu -----");
            System.out.println("1. Open Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();

            MenuOption option = switch (choice) {
                case 1 -> MenuOption.OPEN_ACCOUNT;
                case 2 -> MenuOption.DEPOSIT;
                case 3 -> MenuOption.WITHDRAW;
                case 4 -> MenuOption.TRANSFER;
                case 5 -> MenuOption.EXIT;
                default -> null;
            };

            if (option == null) {
                System.out.println("Invalid choice. Please try again.");
                continue;
            }

            switch (option) {
                case OPEN_ACCOUNT -> System.out.println("Open Account — to be implemented in a later lab");
                case DEPOSIT -> System.out.println("Deposit — to be implemented in a later lab");
                case WITHDRAW -> System.out.println("Withdraw — to be implemented in a later lab");
                case TRANSFER -> System.out.println("Transfer — to be implemented in a later lab");
                case EXIT -> System.out.println("Thank you for using MiniBank. Goodbye!");
            }

        } while (choice != 5);

        sc.close();
    }
}