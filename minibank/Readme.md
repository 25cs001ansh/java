# MiniBank

MiniBank is a console-based banking application built as part of the OOP lab course.
It is developed incrementally — each part adds a new piece to the same project, and later parts build on the classes created earlier.

---

## Features implemented

### Menu shell

- `MiniBank` — public class containing the `main` method.

- `BankInfo` — a record with `name` and `branch` fields, printed as the application header.

- `MenuOption` — an enum with constants `OPEN_ACCOUNT`, `DEPOSIT`, `WITHDRAW`, `TRANSFER`, `EXIT`.

- A numbered menu displayed in a loop, read via `Scanner`.

- A switch expression that prints a placeholder message for each selected option.

- The loop exits and prints a goodbye message when `EXIT` is chosen.

---

### Customer and Account classes

- `Customer` — private fields `name`, `email`, `mobile`, and a `final` `customerId`.
  IDs are auto-generated (e.g. `CUST101`) using a private static counter and a private static `generateCustomerId()` method.
  Constructor and public getters only.

- `Account` — private fields: `final accountNumber`, `ownerName`, `balance` (whole rupees), `active`.
  Account numbers are auto-generated (e.g. `AC0001`) using a private static counter.

  - Two constructors: `Account(ownerName, openingBalance)` and `Account(ownerName)` which delegates via `this(ownerName, 0)`.

  - `deposit(long amount)` adds to balance.

  - `withdraw(long amount)` subtracts from balance and returns `true` if sufficient funds;
    returns `false` and leaves balance unchanged otherwise.

  - No public setter for `balance` — encapsulation is enforced.

- `main` creates three `Account` objects in an `Account[]` array, performs sample deposits/withdrawals
  (including one intentionally failed withdrawal), and prints each account's balance before the menu loop starts.

---

### toString, equals, hashCode and nested Address

**Account**

- `toString()` — returns a readable summary:
  `Account[number=AC0001, owner=Ansh Patel, balance=6500]`

- `equals(Object o)` — two `Account` objects are considered equal when their `accountNumber` values are equal.
  Uses an `instanceof` guard before casting.

- `hashCode()` — delegates to `Objects.hash(accountNumber)`, keeping it consistent with `equals`.
  This is required for correct behaviour when accounts are stored in `HashSet` or `HashMap` (used in Practical 12).

**Customer**

- Implements `Cloneable`.

- `clone()` — calls `super.clone()` and returns a copy of the `Customer` object.

- Static nested class `Address` added inside `Customer`:
  - String fields: `line`, `city`, `pincode`.
  - Public getters: `getLine()`, `getCity()`, `getPincode()`.
  - `toString()` — returns `"12 MG Road, Mumbai - 400001"` style text.

- `address` field added to `Customer` with `getAddress()` and `setAddress()` methods.

**MiniBank (main demo)**

- Prints all accounts using `toString()` (implicit via `println`).

- Compares two `Account` objects with `equals()` and prints the result.

- Creates a `Customer`, attaches an `Address` via the nested class, and prints both.

- Clones a `Customer` and confirms the clone is a different object (`==` returns `false`).

- Uses `instanceof` to check the runtime type of objects stored as `Object`.

---

## Project structure

```
minibank/
├── MiniBank.java
├── Account.java
├── Customer.java
└── Readme.md
```

---

## How to run

```bash
# compile
javac minibank/*.java

# run
java minibank.MiniBank
```

---

## Sample output

```
===================================
BankInfo[name=MiniBank, branch=Main Branch]
===================================
Riya Shah's withdrawal of 10000 failed — insufficient balance.

----- Account Summary (toString) -----
Account[number=AC0001, owner=Ansh Patel, balance=6500]
Account[number=AC0002, owner=Riya Shah, balance=3000]
Account[number=AC0003, owner=Karan Mehta, balance=1500]
--------------------------------------

----- equals demo -----
accounts[0].equals(accounts[1]) : false
accounts[0].equals(accounts[0]) : true

----- Customer + Address demo -----
Customer[id=CUST101, name=Ansh Patel, email=ansh@email.com, mobile=9876543210, address=12 MG Road, Mumbai - 400001]
City : Mumbai

----- clone demo -----
Original : Customer[id=CUST101, name=Ansh Patel, ...]
Clone    : Customer[id=CUST101, name=Ansh Patel, ...]
Same reference? false

----- instanceof demo -----
obj1 instanceof Account  : true
obj1 instanceof Customer : false
obj2 instanceof Customer : true
obj2 instanceof Account  : false
-------------------------------

----- MiniBank Menu -----
1. Open Account
2. Deposit
3. Withdraw
4. Transfer
5. Exit
Enter your choice:
```

---

## Roadmap

Future work will implement real logic behind each menu option — account creation, deposits, withdrawals, and transfers — along with proper data storage and validation.

---

## Author
Ansh — Roll No: 25CS001
