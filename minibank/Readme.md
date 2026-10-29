# MiniBank

MiniBank is a console-based banking application built as part of the OOP lab course. It is developed incrementally — each part adds a new piece to the same project, and later parts build on the classes created earlier.

## Package structure

```
minibank/
├── MiniBank.java                 (package minibank)
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
│       └── MaxLength.java
├── util/                         (package minibank.util)
│   ├── Validator.java
│   ├── CommandParser.java
│   ├── StatementFormatter.java
│   ├── AnnotationValidator.java
│   ├── TransactionLogger.java
│   ├── StatePersister.java
│   ├── TransactionLog.java
│   └── ReportGenerator.java
└── service/                      (package minibank.service)
    ├── WithdrawRule.java
    ├── AccountWorker.java
    ├── TransactionProcessor.java
    ├── TransactionTask.java
    └── TransactionBuffer.java
```

## Features implemented

**Menu shell**
- `MiniBank` — public class containing the `main` method.
- `BankInfo` — a record with `name` and `branch` fields, printed as the application header.
- `MenuOption` — an enum with constants `OPEN_ACCOUNT`, `DEPOSIT`, `WITHDRAW`, `TRANSFER`, `EXIT`.
- A numbered menu displayed in a loop, read via `Scanner`.
- A switch expression that prints a placeholder message for each selected option.
- The loop exits and prints a goodbye message when `EXIT` is chosen.

**Customer and Account classes**
- `Customer` — private fields `name`, `email`, `mobile`, and a `final` `customerId`, auto-generated (e.g. `CUST101`) via a private static counter and `generateCustomerId()`. Constructor and public getters only.
- `Account` — private fields: `final accountNumber`, `ownerName`, `balance` (whole rupees), `active`. Account numbers are auto-generated (e.g. `AC0001`).
  - Two constructors: `Account(ownerName, openingBalance)` and `Account(ownerName)` which delegates via `this(ownerName, 0)`.
  - `deposit(long amount)` adds to balance; `withdraw(long amount)` delegates to `canWithdraw(amount)`.
  - No public setter for `balance` — encapsulation is enforced.

**Object behaviour — toString, equals/hashCode, nested Address, clone**
- `Account.toString()` — readable line with `accountNumber`, `ownerName`, `balance`.
- `Account.equals()` / `Account.hashCode()` — two accounts are equal when their `accountNumber` values are equal.
- `Customer.Address` — a public static nested class with `line`, `city`, `pincode` fields and getters; `Customer` holds an `Address` field with `getAddress()`.
- `Customer` implements `Cloneable` and overrides `clone()`.

**Validation and command parsing**
- `Validator` — static regex-based methods using compiled `Pattern`: `isValidMobile`, `isValidEmail`, `isValidPan`, `isValidIfsc`.
- `TransactionType` — enum: `DEPOSIT`, `WITHDRAW`, `TRANSFER`.
- `Command` — record: `type`, `accountNumber`, `amount`.
- `CommandParser.parse(String line)` — splits a line like `"DEPOSIT AC0001 500"` into a `Command`.
- `StatementFormatter.buildStatement(Account)` — builds a multi-line statement using `StringBuilder`.

**Polymorphic account types**
- `Account` is an **abstract class** with abstract `interestRate()` and `canWithdraw(long amount)`.
- `SavingsAccount` — `minBalance` field; interest `4.0`; withdrawal allowed only if balance stays `>= minBalance`.
- `CurrentAccount` — `overdraftLimit` field; interest `0`; balance can fall to `-overdraftLimit`.
- `FixedDepositAccount` — interest `7.0`; withdrawals always blocked.
- `main` loops through an `Account[]` array calling `interestRate()` on each (runtime polymorphism), and uses an `instanceof` pattern check for `CurrentAccount`.

**Interfaces, packages, and a runnable JAR**
- `Transactable` — interface with `deposit(long amount)` and `withdraw(long amount)`; `Account` implements it.
- `InterestBearing` — interface with a default method `yearlyInterest()` built from `interestRate()` and `getBalance()`; `Account` implements it, so every subclass gets `yearlyInterest()` for free.
- `WithdrawRule` — a functional interface (`boolean allow(Account account, long amount)`) in the `service` package, demonstrated in `main` both as an anonymous class and as a lambda expression.
- `Premium` — a marker interface with no methods.
- All classes live under the `model`, `util`, `service` and `exception` packages, with proper `package` statements and imports; `MiniBank.java` uses one static import (`import static java.lang.System.out;`).
- The project builds into a runnable JAR with `Main-Class: minibank.MiniBank`, launched with `java -jar`.

**Annotations and reflection-based validation**
- All overridden methods (`toString`, `equals`, `hashCode` in `Account`; `interestRate()` and `canWithdraw()` in the subclasses; `clone()` and `toString()` in `Customer`) are marked with `@Override`. `WithdrawRule` is marked `@FunctionalInterface`.
- `model.annotation` package — three custom annotations:
  - `@Id` — marker annotation, `@Target(FIELD)` and `@Retention(RUNTIME)`.
  - `@Positive` — has `String message()` defaulting to `"must be > 0"`.
  - `@MaxLength` — has `int value()`.
- `Account.accountNumber` is annotated `@Id`; `Account.balance` is annotated `@Positive`.
- `AnnotationValidator.validate(Object obj)` — uses reflection (`getClass().getDeclaredFields()`, `field.isAnnotationPresent(...)`, `field.setAccessible(true)`, `field.get(obj)`) to check `@Positive` and `@MaxLength` rules and returns the error messages as a `String[]`. It walks up the class hierarchy so inherited fields (like `balance`, declared in the abstract `Account` class) are checked even when validating a subclass instance.
- `main` creates a `SavingsAccount` with a negative balance and runs `AnnotationValidator.validate()`, printing the resulting error.

**Custom exception hierarchy and fault tolerance**
- `exception` package — a checked base class `BankException extends Exception`, with three subclasses:
  - `InsufficientFundsException` — carries a `long shortfall` field.
  - `AccountNotFoundException`.
  - `InvalidAmountException`.
- `Account.deposit(long amount)` throws `InvalidAmountException` for a non-positive amount, instead of silently doing nothing.
- `Account.withdraw(long amount)` throws `InsufficientFundsException` (with the shortfall) when the balance is too low, and `InvalidAmountException` when the amount is non-positive — replacing the old boolean "flag" return value. The `Transactable` interface was updated to match.
- `Account.transfer(Account to, long amount)` — withdraws from `this` and deposits into `to`, using `try`/`catch`/`finally`, and re-throws a `BankException` when the withdrawal or deposit fails.
- `TransactionLogger` — a small `AutoCloseable` class used in a try-with-resources block in `main` to log the start/end of a transfer.
- `main` wraps every deposit/withdraw/transfer call in try-catch blocks with friendly messages, uses `finally` blocks to confirm each attempt completed, and demonstrates try-with-resources around a transfer between two accounts.

**Multithreading and synchronization**
- `AccountWorker` — in the `service` package, implements `Runnable` with fields `account`, `times`, `amount`; `run()` calls `account.deposit(amount)` in a loop `times` times.
- `main` creates one account and starts about 10 `AccountWorker` threads against it, joins each thread, then prints the thread names and the final balance.
- `Account.deposit()` and `Account.withdraw()` are `synchronized`, so concurrent deposits no longer race and the final balance comes out correct.

## How to run

### Compile
```bash
# from the folder that contains the "minibank" package folder
javac minibank/*.java minibank/exception/*.java minibank/model/*.java minibank/model/annotation/*.java minibank/util/*.java minibank/service/*.java -d out
```

### Run directly (without a JAR)
```bash
java -cp out minibank.MiniBank
```

### Build a runnable JAR
```bash
cd out
jar cfe minibank.jar minibank.MiniBank .
cd ..
```
`cfe` means: create (`c`), specify a jar file (`f`), and set the entry point / `Main-Class` (`e`) — this writes the manifest automatically so you don't need to hand-write `MANIFEST.MF`.

### Run the JAR
```bash
java -jar out/minibank.jar
```

## Roadmap
Future work will run every menu action through the validation, command-parsing, and withdraw-rule layers, and wire up real account creation, deposits, withdrawals, and transfers using persistent storage.

## Author
Ansh — Roll No: 25CS001

## Thread pools, producer-consumer, and deadlock avoidance

- `TransactionProcessor` — wraps an `ExecutorService` created with `Executors.newFixedThreadPool(4)`. `submit(Runnable task)` calls `execute()`; `stop()` calls `shutdown()` then `awaitTermination()`.
- `TransactionTask` — a `Runnable` that performs a single deposit or withdrawal on an `Account`. `main` submits 20 of these (alternating deposit/withdraw) to a `TransactionProcessor`, then stops it and prints the final balance.
- `TransactionBuffer` — a simple bounded array buffer with synchronized `produce()`/`consume()` methods using `wait()`/`notify()`. `main` runs one producer thread adding `TransactionTask`s to the buffer and one consumer thread taking and running them, then prints the final balance.
- Deadlock in `Account`:
  - `transferUnsafe(Account to, long amount)` — the original transfer logic, kept as a reference. It locks `this` (via `withdraw()`) then `to` (via `deposit()`) with no consistent ordering, so two threads calling `a.transferUnsafe(b, x)` and `b.transferUnsafe(a, y)` at the same time can deadlock.
  - `transfer(Account to, long amount)` — the fixed version. It always locks the two accounts in a consistent order (the one with the smaller `accountNumber` first) using nested `synchronized` blocks before calling `withdraw()`/`deposit()`, so opposite-direction transfers can never deadlock.
  - `main` starts two threads doing `A.transfer(B, 100)` and `B.transfer(A, 100)` concurrently and shows both complete cleanly.

## Persistence and end-of-day reporting

- `Account` (and `SavingsAccount`, `CurrentAccount`, `FixedDepositAccount`) and `Customer` (and its nested `Address`) now implement `Serializable`, each with its own `private static final long serialVersionUID`.
- `StatePersister` — `save(Account[] accounts, Path file)` writes the array with an `ObjectOutputStream` inside try-with-resources; `load(Path file)` reads it back with an `ObjectInputStream`.
- `TransactionLog` — `append(Path file, String line)` uses `Files.write(...)` with `CREATE` and `APPEND` to add one line to an append-only log file.
- `ReportGenerator` — `generateReport(Path logsFolder, Path reportFile)` walks the logs folder with `Files.newDirectoryStream`, reads each file line by line with a `BufferedReader`, sums total deposits and withdrawals, reads each file's size and last-modified time with `Files.readAttributes`, and writes the summary to a report file.
- `main` saves the current accounts to `data/accounts.dat`, reloads them into a new array and prints them to confirm the data survived, appends a few sample transaction lines to `data/logs/transactions.log`, then generates and prints the end-of-day report (`data/report.txt`).