package p1;
public class Driver{

    public static void main(String[] args) {

        // (d) Create Point array with repeated coordinates
        Point[] points = {
            new Point(1, 2),
            new Point(3, 4),
            new Point(1, 2), // Repeat
            new Point(5, 6),
            new Point(3, 4)  // Repeat
        };

        int distinctCount = 0;

        // (e) Check every point
        for (int i = 0; i < points.length; i++) {

            boolean alreadyAppeared = false;

            // Check only previous points
            for (int j = 0; j < i; j++) {

                if (points[i].equals(points[j])) {
                    alreadyAppeared = true;
                    break;
                }
            }

            // Count if point did not appear earlier
            if (!alreadyAppeared) {
                distinctCount++;
            }
        }

        // (f) Print distinct count
        System.out.println("Distinct: " + distinctCount);
    }
}