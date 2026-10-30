package p3;

import java.util.Objects;

public class Fraction {

    // (a) Private fields
    private final int num;
    private final int den;

    // Helper: greatest common divisor
    private static int gcd(int a, int b) {
        a = Math.abs(a);
        b = Math.abs(b);
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    // (b) Constructor reduces to lowest terms
    public Fraction(int num, int den) {
        if (den == 0) {
            throw new IllegalArgumentException("Denominator cannot be zero");
        }

        int g = gcd(num, den);
        int sign = (den < 0) ? -1 : 1;

        this.num = sign * (num / g);
        this.den = Math.abs(den / g);
    }

    // (c) Override toString() to return num + "/" + den
    @Override
    public String toString() {
        return num + "/" + den;
    }

    // (d) Override equals() and hashCode() using reduced num and den
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Fraction other = (Fraction) obj;
        return this.num == other.num && this.den == other.den;
    }

    @Override
    public int hashCode() {
        return Objects.hash(num, den);
    }
}

