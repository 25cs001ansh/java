package p3;

public class Driver {

    public static void main(String[] args) {
        // (e) Create equivalent fractions
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(2, 4);
        Fraction f3 = new Fraction(3, 6);
        Fraction f4 = new Fraction(5, 10);

        System.out.println("f1 (1/2): " + f1);
        System.out.println("f2 (2/4): " + f2);
        System.out.println("f3 (3/6): " + f3);
        System.out.println("f4 (5/10): " + f4);

        // Confirm equality
        System.out.println("\n--- Equality Checks ---");
        System.out.println("f1.equals(f2): " + f1.equals(f2));
        System.out.println("f2.equals(f3): " + f2.equals(f3));
        System.out.println("f3.equals(f4): " + f3.equals(f4));

        // Negative denominator reduction test
        Fraction f5 = new Fraction(-2, -4);
        Fraction f6 = new Fraction(2, -4);
        System.out.println("\nNegative tests:");
        System.out.println("f5 (-2/-4): " + f5 + " equals f1? " + f5.equals(f1));
        System.out.println("f6 (2/-4): " + f6);
    }
}

