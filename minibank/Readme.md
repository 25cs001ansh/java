# MiniBank

MiniBank is a console-based banking application built as part of the OOP lab course. It is developed incrementally — each part adds a new piece to the same project, and later parts build on the classes created earlier.

## Features implemented

**Menu shell**
- `MiniBank` — public class containing the `main` method.

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

**Polymorphic account types**
- `Account` is now an **abstract class** with two abstract methods: `interestRate()` and `canWithdraw(long amount)`.

- `SavingsAccount` — has a `minBalance` field; `interestRate()` returns `4.0`; `canWithdraw()` allows withdrawal only if the balance stays `>= minBalance`.

- `CurrentAccount` — has an `overdraftLimit` field; `interestRate()` returns `0`; `canWithdraw()` allows the balance to fall to `-overdraftLimit`.

- `FixedDepositAccount` — `interestRate()` returns `7.0`; `canWithdraw()` always returns `false` (locked deposit).

- Each subclass calls `super(...)` to initialise the inherited fields.

- `main` places objects of all three types in an `Account[]` array, calls `interestRate()` on each (runtime polymorphism), and uses an `instanceof` pattern check to handle `CurrentAccount` specially.

### Project structure
```
minibank/
├── MiniBank.java
├── Customer.java
├── Account.java
├── SavingsAccount.java
├── CurrentAccount.java
├── FixedDepositAccount.java
├── Validator.java
├── TransactionType.java
├── Command.java
├── CommandParser.java
└── StatementFormatter.java
```

### How to run

```bash
# compile
javac minibank/*.java

# run
java minibank.MiniBank
```

### Sample output
```
===================================
BankInfo[name=MiniBank, branch=Main Branch]
===================================

----- Account Summary (toString) -----
SavingsAccount[number=AC0001, owner=Ansh Patel, balance=6500, active=true]
CurrentAccount[number=AC0002, owner=Riya Shah, balance=3000, active=true]
FixedDepositAccount[number=AC0003, owner=Karan Mehta, balance=2000, active=true]
---------------------------------------

----- Interest Rates (runtime polymorphism) -----
AC0001 (SavingsAccount) -> interest rate: 4.0%
AC0002 (CurrentAccount) -> interest rate: 0.0%
AC0003 (FixedDepositAccount) -> interest rate: 7.0%
--------------------------------------------------

AC0002 is a CurrentAccount with overdraft limit: 2000

----- MiniBank Menu -----
1. Open Account
2. Deposit
3. Withdraw
4. Transfer
5. Exit
Enter your choice:
```

## Roadmap
Future work will run every menu action through the validation and command-parsing layer, and wire up real account creation, deposits, withdrawals, and transfers using persistent storage.

## Author
Ansh — Roll No: 25CS001