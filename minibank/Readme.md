# MiniBank

MiniBank is a console-based banking application built incrementally across the semester as part of the OOP lab course. Each lab attaches a new banking feature behind the menu options defined.

## Project Setup & Menu Shell

This sets up the project skeleton: the main class, core data types, and the navigable menu loop that later labs will extend.

### Features implemented
- `MiniBank` — public class containing the `main` method.
- `BankInfo` — a record with `name` and `branch` fields, printed as the application header.
- `MenuOption` — an enum with constants `OPEN_ACCOUNT`, `DEPOSIT`, `WITHDRAW`, `TRANSFER`, `EXIT`.
- A numbered menu displayed in a loop, read via `Scanner`.
- A switch expression that prints a placeholder message for each selected option.
- The loop exits and prints a goodbye message when `EXIT` is chosen.

### Project structure
```
minibank/
└── MiniBank.java
```

### How to run

```bash
# compile
javac minibank/MiniBank.java

# run
java minibank.MiniBank
```

### Sample output
```
===================================
BankInfo[name=MiniBank, branch=Master Branch]
===================================

----- MiniBank Menu -----
1. Open Account
2. Deposit
3. Withdraw
4. Transfer
5. Exit
Enter your choice:
```

## Roadmap
Future labs will implement real logic behind each menu option: account creation, deposits, withdrawals, and transfers, along with proper data storage and validation.

## Author
Ansh — Roll No: 25CS001