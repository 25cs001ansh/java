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

- `main` creates three `Account` objects in an `Account[]` array, performs sample deposits/withdrawals (including one intentionally failed withdrawal), and prints each account's balance.

**Object behaviour — toString, equals/hashCode, nested Address, clone**
- `Account.toString()` — returns a readable line with `accountNumber`, `ownerName` and `balance`.

- `Account.equals()` / `Account.hashCode()` — two accounts are equal when their `accountNumber` values are equal, so accounts can be safely stored in hash-based collections later.

- `Customer.Address` — a public static nested class with `line`, `city`, `pincode` fields and getters. `Customer` holds an `Address` field with a `getAddress()` method.

- `Customer` implements `Cloneable` and overrides `clone()` to return a copy of the customer.

- `main` prints accounts via `toString()`, compares two `Account` objects with `equals()`, and uses `instanceof` to check an object's type.

**Validation and command parsing**
- `Validator` — public static methods using compiled `Pattern` regexes: `isValidMobile(String)`, `isValidEmail(String)`, `isValidPan(String)`, `isValidIfsc(String)`, each returning a boolean.

- `TransactionType` — an enum with constants `DEPOSIT`, `WITHDRAW`, `TRANSFER`.

- `Command` — a record with fields `type` (`TransactionType`), `accountNumber` (`String`) and `amount` (`long`).

- `CommandParser` — a public static `parse(String line)` method that splits a line such as `"DEPOSIT AC0001 500"` and returns a `Command` object.

- `StatementFormatter` — a public static `buildStatement(Account account)` method that uses a `StringBuilder` to assemble a multi-line account statement.

- `main` tests each validator with one correct and one wrong input, parses a sample command and prints its three parts, and prints a formatted statement for an account.

### Project structure
```
minibank/
├── MiniBank.java
├── Customer.java
├── Account.java
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
Account[number=AC0001, owner=Ansh Patel, balance=6500, active=true]
Account[number=AC0002, owner=Riya Shah, balance=3000, active=true]
Account[number=AC0003, owner=Karan Mehta, balance=1500, active=true]
---------------------------------------

accounts[0].equals(duplicateOfFirst): true
accounts[0].equals(accounts[1]): false
Account[number=AC0003, owner=Karan Mehta, balance=1500, active=true] is an instance of Account

Original customer: Customer[id=CUST101, name=Ansh Patel, email=ansh@example.com, mobile=9876543210, address=221B Ring Road, Ahmedabad - 380001]
Cloned customer:   Customer[id=CUST101, name=Ansh Patel, email=ansh@example.com, mobile=9876543210, address=221B Ring Road, Ahmedabad - 380001]

----- Validator Tests -----
Mobile 9876543210 (correct): true
Mobile 12345 (wrong):        false
Email ansh@example.com (correct): true
Email ansh@@example (wrong):       false
PAN ABCDE1234F (correct): true
PAN ABC1234F (wrong):      false
IFSC HDFC0001234 (correct): true
IFSC HDFC1234 (wrong):       false
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
Enter your choice:
```

## Roadmap
Future work will run every menu action through the validation and command-parsing layer, and implement real logic behind each option — account creation, deposits, withdrawals, and transfers — with proper data storage.

## Author
Ansh — Roll No: 25CS001