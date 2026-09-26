package p1;
import java.util.Objects;

public class Point {

    // (a) Private variables
    private int x;
    private int y;

    // Constructor
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // (b) Override toString()
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    // (c) Override equals()
    @Override
    public boolean equals(Object obj) {

        // Check if both objects are the same object
        if (this == obj) {
            return true;
        }

        // Check if obj is null or not a Point
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        // Convert Object to Point
        Point p = (Point) obj;

        // Compare x and y coordinates
        return this.x == p.x && this.y == p.y;
    }

    // Override hashCode()
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}