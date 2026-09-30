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

        System.out.println("\n----- Account Summary (toString) -----");
        for (Account acc : accounts) {
            System.out.println(acc);
        }
        System.out.println("---------------------------------------\n");

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