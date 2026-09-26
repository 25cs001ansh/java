package minibank;

import java.util.Scanner;

public class MiniBank {

    record BankInfo(String name, String branch) {}

    enum MenuOption {
        OPEN_ACCOUNT, DEPOSIT, WITHDRAW, TRANSFER, EXIT
    }

    public static void main(String[] args) {
        BankInfo bankInfo = new BankInfo("MiniBank", "Main Branch");
        System.out.println("===================================");
        System.out.println(bankInfo);
        System.out.println("===================================");

        // ---- Practical 2: Customer & Account demo ----
        Account[] accounts = new Account[3];
        accounts[0] = new Account("Ansh Patel", 5000);
        accounts[1] = new Account("Riya Shah");
        accounts[2] = new Account("Karan Mehta", 2000);

        accounts[0].deposit(1500);
        accounts[1].deposit(3000);
        accounts[2].withdraw(500);

        boolean overdrawn = accounts[1].withdraw(10000);
        if (!overdrawn) {
            System.out.println(accounts[1].getOwnerName() + "'s withdrawal of 10000 failed — insufficient balance.");
        }

        System.out.println("\n----- Account Summary -----");
        for (Account acc : accounts) {
            System.out.println(acc.getAccountNumber() + " | " + acc.getOwnerName()
                    + " | Balance: " + acc.getBalance());
        }
        System.out.println("----------------------------\n");

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