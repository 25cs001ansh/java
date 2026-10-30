public class ParkingLot {

    // (a) Private fields
    private int twoWheelers;
    private int fourWheelers;
    private final int twoCap;
    private final int fourCap;

    private static long revenue = 0;

    // Constructor
    public ParkingLot(int twoCap, int fourCap) {
        this.twoCap = twoCap;
        this.fourCap = fourCap;
        this.twoWheelers = 0;
        this.fourWheelers = 0;
    }

    // (b) park method: adds vehicle if capacity available, updates revenue
    public void park(String type) {
        if (type.equalsIgnoreCase("two")) {
            if (twoWheelers < twoCap) {
                twoWheelers++;
                revenue += 20;
                System.out.println("Parked 2-wheeler. Occupancy: " + twoWheelers + "/" + twoCap);
            } else {
                System.out.println("2-wheeler section Full! Cannot park.");
            }
        } else if (type.equalsIgnoreCase("four")) {
            if (fourWheelers < fourCap) {
                fourWheelers++;
                revenue += 40;
                System.out.println("Parked 4-wheeler. Occupancy: " + fourWheelers + "/" + fourCap);
            } else {
                System.out.println("4-wheeler section Full! Cannot park.");
            }
        } else {
            System.out.println("Invalid vehicle type: " + type);
        }
    }

    // (c) leave method: decrements count, never below 0
    public void leave(String type) {
        if (type.equalsIgnoreCase("two")) {
            if (twoWheelers > 0) {
                twoWheelers--;
                System.out.println("2-wheeler left. Occupancy: " + twoWheelers + "/" + twoCap);
            } else {
                System.out.println("No 2-wheelers currently parked.");
            }
        } else if (type.equalsIgnoreCase("four")) {
            if (fourWheelers > 0) {
                fourWheelers--;
                System.out.println("4-wheeler left. Occupancy: " + fourWheelers + "/" + fourCap);
            } else {
                System.out.println("No 4-wheelers currently parked.");
            }
        }
    }

    // (d) main: simulate events and print final occupancy and revenue
    public static void main(String[] args) {
        // Create parking lot with capacity: 2 two-wheelers, 2 four-wheelers
        ParkingLot lot = new ParkingLot(2, 2);

        System.out.println("=== Parking Lot Simulation ===");

        // Park sequence
        lot.park("two");   // parked
        lot.park("two");   // parked (now 2)
        lot.park("two");   // should print Full

        lot.park("four");  // parked
        lot.park("four");  // parked (now 2)
        lot.park("four");  // should print Full

        // Leave sequence
        lot.leave("two");  // 1 leaves
        lot.park("two");   // parks again in freed spot

        System.out.println("\n=== Final Summary ===");
        System.out.println("Final 2-wheelers parked: " + lot.twoWheelers + "/" + lot.twoCap);
        System.out.println("Final 4-wheelers parked: " + lot.fourWheelers + "/" + lot.fourCap);
        System.out.println("Total revenue: Rs " + ParkingLot.revenue);
    }
}

