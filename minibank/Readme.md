# MiniBank

MiniBank is a console-based banking application built as part of the OOP lab course. It is developed incrementally — each part adds a new piece to the same project, and later parts build on the classes created earlier. This is the final, capstone version: the Practical 1 menu now calls real `BankService` operations instead of placeholder text.

## Package structure

```
minibank/
├── MiniBank.java                 (package minibank)
├── AccountTest.java               (package minibank — JUnit 5 tests)
├── exception/                    (package minibank.exception)
│   ├── BankException.java
│   ├── InsufficientFundsException.java
│   ├── AccountNotFoundException.java
│   └── InvalidAmountException.java
├── model/                        (package minibank.model)
│   ├── BankInfo.java
│   ├── MenuOption.java
│   ├── Customer.java
│   ├── Account.java
│   ├── SavingsAccount.java
│   ├── CurrentAccount.java
│   ├── FixedDepositAccount.java
│   ├── TransactionType.java
│   ├── Command.java
│   ├── Transactable.java
│   ├── InterestBearing.java
│   ├── Premium.java
│   └── annotation/               (package minibank.model.annotation)
│       ├── Id.java
│       ├── Positive.java
│       ├── MaxLength.java
│       └── Audit.java
├── util/                         (package minibank.util)
│   ├── Validator.java
│   ├── CommandParser.java
│   ├── StatementFormatter.java
│   ├── AnnotationValidator.java
│   ├── TransactionLogger.java
│   ├── StatePersister.java
│   ├── TransactionLog.java
│   ├── ReportGenerator.java
│   └── AuditLogger.java
└── service/                      (package minibank.service)
    ├── WithdrawRule.java
    ├── AccountWorker.java
    ├── TransactionProcessor.java
    ├── TransactionTask.java
    ├── TransactionBuffer.java
    ├── AccountStore.java
    ├── Repository.java
    └── BankService.java
```

## Features implemented

**Menu shell**
- `MiniBank` — public class containing the `main` method.
- `BankInfo` — a record with `name` and `branch` fields, printed as the application header.
- `MenuOption` — an enum with constants `OPEN_ACCOUNT`, `DEPOSIT`, `WITHDRAW`, `TRANSFER`, `PROCESS_BATCH`, `REPORT`, `EXIT`.
- A numbered menu displayed in a loop, read via `Scanner`.

**Customer and Account classes**
- `Customer` — private fields `name`, `email`, `mobile`, and a `final` `customerId`, auto-generated (e.g. `CUST101`). Constructor and public getters only.
- `Account` — private fields: `final accountNumber`, `ownerName`, `balance` (whole rupees), `active`. Account numbers are auto-generated (e.g. `AC0001`).
  - Two constructors: `Account(ownerName, openingBalance)` and `Account(ownerName)` which delegates via `this(ownerName, 0)`.
  - No public setter for `balance` — encapsulation is enforced.

**Object behaviour — toString, equals/hashCode, nested Address, clone**
- `Account.toString()`, `Account.equals()`/`hashCode()` (by `accountNumber`), `Account.compareTo()` (by `accountNumber`).
- `Customer.Address` — a public static nested class with `line`, `city`, `pincode` fields and getters.
- `Customer` implements `Cloneable` and overrides `clone()`.

**Validation and command parsing**
- `Validator` — static regex-based methods: `isValidMobile`, `isValidEmail`, `isValidPan`, `isValidIfsc`.
- `TransactionType` — enum: `DEPOSIT`, `WITHDRAW`, `TRANSFER`.
- `Command` — record: `type`, `accountNumber`, `amount`.
- `CommandParser.parse(String line)` — splits a line like `"DEPOSIT AC0001 500"` into a `Command`.
- `StatementFormatter.buildStatement(Account)` — builds a multi-line statement using `StringBuilder`.

**Polymorphic account types**
- `Account` is an **abstract class** with abstract `interestRate()` and `canWithdraw(long amount)`.
- `SavingsAccount` — `minBalance` field; interest `4.0`; withdrawal allowed only if balance stays `>= minBalance`.
- `CurrentAccount` — `overdraftLimit` field; interest `0`; balance can fall to `-overdraftLimit`.
- `FixedDepositAccount` — interest `7.0`; withdrawals always blocked.

**Interfaces, packages, and a runnable JAR**
- `Transactable`, `InterestBearing` (default `yearlyInterest()`), `WithdrawRule` (functional interface), `Premium` (marker interface).
- All classes live under `model`, `util`, `service` and `exception` packages.

**Annotations and reflection-based validation**
- `model.annotation` package: `@Id`, `@Positive(message)`, `@MaxLength(value)`, `@Audit`.
- `Account.accountNumber` is `@Id`; `Account.balance` is `@Positive`.
- `AnnotationValidator.validate(Object obj)` — reflection-based field validation, walking the class hierarchy.

**Custom exception hierarchy and fault tolerance**
- `BankException` (checked base) with `InsufficientFundsException` (carries `shortfall`), `AccountNotFoundException`, `InvalidAmountException`.
- `Account.deposit()`/`withdraw()` throw these instead of returning a boolean flag.
- `Account.transfer(Account to, long amount)` — try/catch/finally, re-throws `BankException`.
- `TransactionLogger` — a small `AutoCloseable` class for try-with-resources demos.

**Multithreading, thread pools, producer-consumer, deadlock avoidance**
- `AccountWorker` (`Runnable`, repeated deposits), `Account.deposit()`/`withdraw()` are `synchronized`.
- `TransactionProcessor` wraps a fixed thread pool (`submit`/`stop`), now also keeps shared accounts in a `ConcurrentHashMap`.
- `TransactionTask` — one deposit or withdrawal as a `Runnable`.
- `TransactionBuffer` — bounded buffer with `wait()`/`notify()` for a producer-consumer demo.
- `Account.transferUnsafe()` (deadlock-prone, kept as reference) vs `Account.transfer()` (deadlock-free via consistent lock ordering by `accountNumber`).

**Persistence and end-of-day reporting**
- `Account` hierarchy and `Customer` implement `Serializable` with `serialVersionUID`.
- `StatePersister.save()/load()` — `Account[]` via `ObjectOutputStream`/`ObjectInputStream`.
- `TransactionLog.append()` — append-only log via `Files.write(..., CREATE, APPEND)`.
- `ReportGenerator.generateReport()` — walks a logs folder with NIO, sums deposits/withdrawals, reads file attributes, writes a summary report.

**Collections Framework storage**
- `AccountStore` — `HashMap<String, Account>`-backed store with `add`/`get`/`getAll`, an `ArrayList<String>` transaction history, and `sortedByBalance()`.
- `Account implements Comparable<Account>` (by `accountNumber`).

**Generic Repository<T>**
- `Repository<T>` — generic `HashMap<String, T>`-backed store with `save`, `findById`, `findAll`, `delete`.
- `save()` finds each entity's key by reflection on the `@Id` field, walking the class hierarchy.
- `saveAll(T... items)` — `@SafeVarargs`.
- `average(N[] values)` — generic method with a bounded type parameter (`<N extends Number>`).

**Capstone: BankService, the real menu, batch processing, auditing, and tests**
- `BankService` — wires `Repository<Account>`, `Validator`, and the exception hierarchy together:
  - `openAccount(type, ownerName, mobile, openingBalance, limitValue)` — validates the mobile number with `Validator.isValidMobile()`, then creates a `SavingsAccount`, `CurrentAccount`, or `FixedDepositAccount` and saves it to the repository.
  - `deposit(accountNumber, amount)`, `withdraw(accountNumber, amount)`, `transfer(from, to, amount)` — look the account(s) up in the repository, throwing `AccountNotFoundException` if missing, and otherwise delegate to `Account`.
  - `statement(accountNumber)` — returns a formatted statement via `StatementFormatter`.
  - `listAccounts()` — returns every account in the repository.
- The Practical 1 menu is now wired to real operations:
  - **Open Account** / **Deposit** / **Withdraw** / **Transfer** call the matching `BankService` method, reading input with `Scanner` and printing friendly error messages on exception. Deposits and withdrawals are also appended to the transaction log.
  - **Process Batch** registers every account into a `TransactionProcessor`'s thread-safe `ConcurrentHashMap` and submits a batch of sample deposit/withdraw `TransactionTask`s, processed safely across the thread pool.
  - **Report** generates and prints the end-of-day report from the logs folder.
  - **Exit** saves every account in the repository back to `data/accounts.dat` before printing the goodbye message.
  - On start-up, if `data/accounts.dat` exists, its accounts are loaded into the repository before the menu runs; otherwise MiniBank starts with an empty, real account book (all the standalone feature demos from earlier labs still run as before, on their own separate sandbox accounts, so they're unaffected by whether any real accounts have been saved yet).
- `@Audit` — a marker annotation (`model.annotation.Audit`) placed on `BankService.openAccount`, `deposit`, `withdraw`, and `transfer`.
- `AuditLogger.logIfAudited(target, methodName)` — reflectively checks whether the named method on `target`'s class carries `@Audit`, and if so prints an audit line with a timestamp. Called at the top of every audited `BankService` method.
- `AccountTest` (`minibank.AccountTest`) — JUnit 5 tests using `@Test`:
  - `depositIncreasesBalance()` — a deposit increases the balance by the deposited amount.
  - `withdrawalOverLimitThrowsException()` — withdrawing beyond what the account allows throws `InsufficientFundsException`.
  - `concurrentDepositsKeepBalanceCorrect()` — 10 `AccountWorker` threads depositing concurrently still produce the correct final balance, thanks to `synchronized` deposit/withdraw.

## How to run

### Compile
```bash
# from the folder that contains the "minibank" package folder
javac minibank/*.java minibank/exception/*.java minibank/model/*.java minibank/model/annotation/*.java minibank/util/*.java minibank/service/*.java -d out
```
`AccountTest.java` needs JUnit 5 on the classpath to compile/run (see "Running the tests" below) — don't include it in the compile line above unless `junit-jupiter` is on the classpath.

### Run directly (without a JAR)
```bash
java -cp out minibank.MiniBank
```

### Running the tests
JUnit 5 isn't bundled with the JDK, so add it to your IDE's project (IntelliJ/Eclipse/VS Code's Java extension can add the "JUnit 5" test library with a couple of clicks) or, if compiling by hand, download `junit-platform-console-standalone` and run:
```bash
javac -cp junit-platform-console-standalone.jar minibank/*.java minibank/exception/*.java minibank/model/*.java minibank/model/annotation/*.java minibank/util/*.java minibank/service/*.java -d out
java -jar junit-platform-console-standalone.jar -cp out --select-class minibank.AccountTest
```

### Build a runnable JAR
```bash
cd out
jar cfe minibank.jar minibank.MiniBank .
cd ..
```
`cfe` means: create (`c`), specify a jar file (`f`), and set the entry point / `Main-Class` (`e`) — this writes the manifest automatically so you don't need to hand-write `MANIFEST.MF`. Don't include `AccountTest.class` in the JAR — it needs JUnit on the classpath to run and isn't part of the application itself.

### Run the JAR
```bash
java -jar out/minibank.jar
```

## Roadmap
MiniBank is feature-complete for this course: a real BankService-driven menu, validation, custom exceptions, multithreading with synchronization and deadlock avoidance, persistence and reporting, Collections-based and generic storage, reflection-based validation and auditing, and a small JUnit test suite. Future extensions could add a proper CLI argument parser, richer reporting (per-account breakdowns), and a persistent audit log file alongside the console output.

## Author
Ansh — Roll No: 25CS001