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
  - `withdraw(long amount)` subtracts from balance and returns `true` if sufficient funds; returns `false` and leaves balance unchanged otherwise.
  - No public setter for `balance` — encapsulation is enforced.
- `main` creates three `Account` objects in an `Account[]` array, performs sample deposits/withdrawals (including one intentionally failed withdrawal), and prints each account's balance before the menu loop starts.

### Project structure
```
minibank/
├── MiniBank.java
├── Customer.java
└── Account.java
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

----- Account Summary -----
AC0001 | Ansh Patel | Balance: 6500
AC0002 | Riya Shah | Balance: 3000
AC0003 | Karan Mehta | Balance: 1500
----------------------------

----- MiniBank Menu -----
1. Open Account
2. Deposit
3. Withdraw
4. Transfer
5. Exit
Enter your choice:
```

## Roadmap
Future work will implement real logic behind each menu option — account creation, deposits, withdrawals, and transfers — along with proper data storage and validation.

## Author
Ansh — Roll No: 25CS001
