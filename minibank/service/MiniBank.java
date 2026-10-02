package service;

import model.Account;
import model.CurrentAccount;
import model.Customer;
import model.FixedDepositAccount;
import model.Premium;
import model.SavingsAccount;
import util.Validator;

import java.util.Scanner;

import static util.StatementFormatter.buildStatement;

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

        // ---- Customer & Account demo ----
        Account[] accounts = new Account[3];
        accounts[0] = new SavingsAccount("Ansh Patel", 5000, 1000);
        accounts[1] = new CurrentAccount("Riya Shah", 0, 2000);
        accounts[2] = new FixedDepositAccount("Karan Mehta", 2000);

        accounts[0].deposit(1500);
        accounts[1].deposit(3000);

        boolean overdrawn = accounts[2].withdraw(500);
        if (!overdrawn) {
            System.out.println(accounts[2].getOwnerName()
                    + "'s withdrawal failed — fixed deposit is locked for withdrawals.");
        }

        System.out.println("\n----- Account Summary (toString) -----");
        for (Account acc : accounts) {
            System.out.println(acc);
        }
        System.out.println("---------------------------------------\n");

        // ---- InterestBearing demo: yearlyInterest() default method ----
        System.out.println("----- Interest Rates & Yearly Interest (InterestBearing) -----");
        for (Account acc : accounts) {
            System.out.println(acc.getAccountNumber() + " (" + acc.getClass().getSimpleName()
                    + ") -> interest rate: " + acc.interestRate() + "%, yearly interest: " + acc.yearlyInterest());
        }
        System.out.println("----------------------------------------------------------------\n");

        // ---- Premium Marker Interface check ----
        System.out.println("----- Premium Marker Interface Check -----");
        for (Account acc : accounts) {
            if (acc instanceof Premium) {
                System.out.println(acc.getAccountNumber() + " is marked as Premium account.");
            }
        }
        System.out.println("-------------------------------------------\n");

        // ---- WithdrawRule Demo (1. Anonymous class, 2. Lambda expression) ----
        System.out.println("----- WithdrawRule Demo -----");
        // 1. Anonymous Class usage
        WithdrawRule anonymousRule = new WithdrawRule() {
            @Override
            public boolean allow(Account account, long amount) {
                return amount <= 5000;
            }
        };
        System.out.println("Anonymous class rule (limit <= 5000) for " + accounts[0].getAccountNumber()
                + " with amount 3000: " + anonymousRule.allow(accounts[0], 3000));

        // 2. Lambda Expression usage
        WithdrawRule lambdaRule = (account, amount) -> (account.getBalance() - amount) >= 1000;
        System.out.println("Lambda expression rule (min balance >= 1000) for " + accounts[0].getAccountNumber()
                + " with amount 6000: " + lambdaRule.allow(accounts[0], 6000));
        System.out.println("-----------------------------\n");

        // instanceof pattern check
        for (Account acc : accounts) {
            if (acc instanceof CurrentAccount currentAccount) {
                System.out.println(acc.getAccountNumber() + " is a CurrentAccount with overdraft limit: "
                        + currentAccount.getOverdraftLimit());
            }
        }
        System.out.println();

        // Customer with nested Address and clone() demo
        Customer.Address address = new Customer.Address("221B Ring Road", "Ahmedabad", "380001");
        Customer customer = new Customer("Ansh Patel", "ansh@example.com", "9876543210", address);
        Customer clonedCustomer = customer.clone();
        System.out.println("Original customer: " + customer);
        System.out.println("Cloned customer:   " + clonedCustomer);
        System.out.println();

        // ---- Validator demo ----
        System.out.println("----- Validator Tests -----");
        System.out.println("Mobile 9876543210 (correct): " + Validator.isValidMobile("9876543210"));
        System.out.println("Email ansh@example.com (correct): " + Validator.isValidEmail("ansh@example.com"));
        System.out.println("----------------------------\n");

        // ---- CommandParser demo ----
        Command command = CommandParser.parse("DEPOSIT AC0001 500");
        System.out.println("----- Parsed Command -----");
        System.out.println("Type          : " + command.type());
        System.out.println("Account Number: " + command.accountNumber());
        System.out.println("Amount        : " + command.amount());
        System.out.println("---------------------------\n");

        // ---- StatementFormatter demo (using static import buildStatement) ----
        System.out.println(buildStatement(accounts[0]));
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
            if (!sc.hasNextInt()) break;
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
