import java.util.Scanner;

// Functional interface for discount rule
@FunctionalInterface
interface DiscountRule {
    double apply(double price);
}

public class DiscountEngine {

    // Applies the chosen lambda rule to an array of prices
    public static double[] applyDiscount(double[] prices, DiscountRule rule) {
        double[] discounted = new double[prices.length];
        for (int i = 0; i < prices.length; i++) {
            double result = rule.apply(prices[i]);
            discounted[i] = Math.max(0.0, result); // Price cannot be negative
        }
        return discounted;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        double[] prices = { 100.0, 250.0, 500.0, 1200.0, 50.0 };

        System.out.println("=== Discount Engine (Lambda Expressions) ===");
        System.out.print("Original prices: ");
        for (double p : prices) {
            System.out.printf("Rs.%.2f  ", p);
        }
        System.out.println("\n");

        System.out.println("Choose a discount rule to apply:");
        System.out.println("1. 10% Festive Discount");
        System.out.println("2. Flat Rs. 50 Coupon Discount");
        System.out.println("3. 50% Clearance Discount");
        System.out.println("4. 20% Discount on items above Rs. 200");
        System.out.print("Enter your choice (1-4): ");

        int choice = 1;
        if (sc.hasNextInt()) {
            choice = sc.nextInt();
        }

        // Rule selected at runtime as a lambda expression
        DiscountRule selectedRule = switch (choice) {
            case 1 -> price -> price * 0.90;                     // 10% off
            case 2 -> price -> price - 50.0;                     // Rs. 50 off
            case 3 -> price -> price * 0.50;                     // 50% off
            case 4 -> price -> price > 200.0 ? price * 0.80 : price; // 20% if > 200
            default -> price -> price;                           // No discount
        };

        double[] discountedPrices = applyDiscount(prices, selectedRule);

        System.out.println("\n--- Applied Discount Summary ---");
        for (int i = 0; i < prices.length; i++) {
            System.out.printf("Rs.%7.2f  ==>  Rs.%7.2f%n", prices[i], discountedPrices[i]);
        }

        sc.close();
    }
}

