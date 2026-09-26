import java.util.Scanner;

public class VendingMachine {

    // (a) Enum Coin
    enum Coin {
        ONE, TWO, FIVE, TEN
    }

    public static void main(String[] args) {

        // (b) Snack price and running total
        int price = 15;
        int total = 0;

        Scanner sc = new Scanner(System.in);

        System.out.println("Snack price: " + price);
        System.out.println("Enter coins: ONE, TWO, FIVE, TEN");

        // (c) and (d) Continue until total >= price
        while (total < price) {

            System.out.print("Enter coin: ");

            String input = sc.nextLine().toUpperCase();

            // Convert input to Coin enum
            Coin coin = Coin.valueOf(input);

            // Switch expression to get coin value
            int value = switch (coin) {
                case ONE -> 1;
                case TWO -> 2;
                case FIVE -> 5;
                case TEN -> 10;
            };

            // Add coin value to total
            total = total + value;

            System.out.println("Total so far: " + total);
        }

        // (e) Calculate and print change
        int change = total - price;

        System.out.println("Paid. Change: " + change);

        sc.close();
    }
}