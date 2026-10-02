# MiniBank

MiniBank is a console-based banking application built as part of the OOP lab course. It is developed incrementally — each part adds a new piece to the same project, and later parts build on the classes created earlier.

## Features implemented

**Menu shell**
- `MiniBank` — public class containing the `main` method in package `service`.

- `BankInfo` — a record with `name` and `branch` fields, printed as the application header.

- `MenuOption` — an enum with constants `OPEN_ACCOUNT`, `DEPOSIT`, `WITHDRAW`, `TRANSFER`, `EXIT`.

- A numbered menu displayed in a loop, read via `Scanner`.

- A switch expression that prints a placeholder message for each selected option.

- The loop exits and prints a goodbye message when `EXIT` is chosen.

**Customer and Account classes**
- `Customer` — private fields `name`, `email`, `mobile`, and a `final` `customerId`. IDs are auto-generated (e.g. `CUST101`) using a private static counter and a private static `generateCustomerId()` method. Constructor and public getters only.

- `Account` — private fields: `final accountNumber`, `ownerName`, `balance` (whole rupees), `active`. Account numbers are auto-generated (e.g. `AC0001`) using a private static counter.

  - Two constructors: `Account(ownerName, openingBalance)` and `Account(ownerName)` which delegates via `this(ownerName, 0)`.
  - `deposit(long amount)` adds to balance.

  - `withdraw(long amount)` delegates to `canWithdraw(amount)` to decide whether the balance changes.

  - No public setter for `balance` — encapsulation is enforced.

**Object behaviour — toString, equals/hashCode, nested Address, clone**
- `Account.toString()` — returns a readable line with class name, `accountNumber`, `ownerName` and `balance`.

- `Account.equals()` / `Account.hashCode()` — two accounts are equal when their `accountNumber` values are equal, so accounts can be safely stored in hash-based collections later.

- `Customer.Address` — a public static nested class with `line`, `city`, `pincode` fields and getters. `Customer` holds an `Address` field with a `getAddress()` method.

- `Customer` implements `Cloneable` and overrides `clone()` to return a copy of the customer.

**Validation and command parsing**
- `Validator` — public static methods using compiled `Pattern` regexes: `isValidMobile(String)`, `isValidEmail(String)`, `isValidPan(String)`, `isValidIfsc(String)`, each returning a boolean.

- `TransactionType` — an enum with constants `DEPOSIT`, `WITHDRAW`, `TRANSFER`.

- `Command` — a record with fields `type` (`TransactionType`), `accountNumber` (`String`) and `amount` (`long`).

- `CommandParser` — a public static `parse(String line)` method that splits a line such as `"DEPOSIT AC0001 500"` and returns a `Command` object.

- `StatementFormatter` — a public static `buildStatement(Account account)` method that uses a `StringBuilder` to assemble a multi-line account statement.

**Polymorphic account types & Interfaces**
- `Transactable` — an interface with `void deposit(long amount)` and `boolean withdraw(long amount)` methods, implemented by `Account`.

- `InterestBearing` — an interface with a default method `double yearlyInterest()` using `interestRate()` and `getBalance()`, implemented by `Account`.

- `WithdrawRule` — a `@FunctionalInterface` with single method `boolean allow(Account account, long amount)`, demonstrated in `main` via anonymous class and lambda expression.

- `Premium` — a marker interface (interface with no methods), implemented by `SavingsAccount`.

- `SavingsAccount` — has a `minBalance` field; `interestRate()` returns `4.0`; `canWithdraw()` allows withdrawal only if the balance stays `>= minBalance`.

- `CurrentAccount` — has an `overdraftLimit` field; `interestRate()` returns `0`; `canWithdraw()` allows the balance to fall to `-overdraftLimit`.

- `FixedDepositAccount` — `interestRate()` returns `7.0`; `canWithdraw()` always returns `false` (locked deposit).

**Package organization & Runnable JAR**
- Code is organized into `model`, `util`, and `service` packages.
- Uses a static import for `buildStatement` (`import static util.StatementFormatter.buildStatement;`).
- Built a runnable JAR file (`minibank.jar`) with manifest specifying `Main-Class: service.MiniBank`.

### Project structure
```
minibank/
├── model/
│   ├── Transactable.java
│   ├── InterestBearing.java
│   ├── Premium.java
│   ├── Account.java
│   ├── SavingsAccount.java
│   ├── CurrentAccount.java
│   ├── FixedDepositAccount.java
│   └── Customer.java
├── util/
│   ├── Validator.java
│   └── StatementFormatter.java
└── service/
    ├── TransactionType.java
    ├── Command.java
    ├── CommandParser.java
    ├── WithdrawRule.java
    └── MiniBank.java
```

### How to run

```bash
# Compile
javac model/*.java util/*.java service/*.java

# Run
java service.MiniBank
```

### Sample output
```
===================================
BankInfo[name=MiniBank, branch=Main Branch]
===================================
Karan Mehta's withdrawal failed — fixed deposit is locked for withdrawals.

----- Account Summary (toString) -----
Account[number=AC0001, owner=Ansh Patel, balance=6500, active=true]
Account[number=AC0002, owner=Riya Shah, balance=3000, active=true]
Account[number=AC0003, owner=Karan Mehta, balance=2000, active=true]
---------------------------------------

----- Interest Rates & Yearly Interest (InterestBearing) -----
AC0001 (SavingsAccount) -> interest rate: 4.0%, yearly interest: 260.0
AC0002 (CurrentAccount) -> interest rate: 0.0%, yearly interest: 0.0
AC0003 (FixedDepositAccount) -> interest rate: 7.0%, yearly interest: 140.0
----------------------------------------------------------------

----- Premium Marker Interface Check -----
AC0001 is marked as Premium account.
-------------------------------------------

----- WithdrawRule Demo -----
Anonymous class rule (limit <= 5000) for AC0001 with amount 3000: true
Lambda expression rule (min balance >= 1000) for AC0001 with amount 6000: false
-----------------------------

AC0002 is a CurrentAccount with overdraft limit: 2000

Original customer: Customer[id=CUST101, name=Ansh Patel, email=ansh@example.com, mobile=9876543210, address=221B Ring Road, Ahmedabad - 380001]
Cloned customer:   Customer[id=CUST101, name=Ansh Patel, email=ansh@example.com, mobile=9876543210, address=221B Ring Road, Ahmedabad - 380001]

----- Validator Tests -----
Mobile 9876543210 (correct): true
Email ansh@example.com (correct): true
----------------------------

----- Parsed Command -----
Type          : DEPOSIT
Account Number: AC0001
Amount        : 500
---------------------------

========== ACCOUNT STATEMENT ==========
Account Number : AC0001
Owner Name     : Ansh Patel
Balance        : 6500
Status         : ACTIVE
========================================

----- MiniBank Menu -----
1. Open Account
2. Deposit
3. Withdraw
4. Transfer
5. Exit
Enter your choice: 5
Thank you for using MiniBank. Goodbye!
```

## Roadmap
Future work will run every menu action through the validation and command-parsing layer, and wire up real account creation, deposits, withdrawals, and transfers using persistent storage.

## Author
Ansh — Roll No: 25CS001