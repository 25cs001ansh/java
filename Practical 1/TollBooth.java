import java.util.Scanner;

public class TollBooth {

    // (a) Record Vehicle
    record Vehicle(String number, String type) {}

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // (b) Running toll total and counters
        int totalToll = 0;

        int bikeCount = 0;
        int carCount = 0;
        int truckCount = 0;

        System.out.println("Enter vehicle number (type 'done' to stop):");

        // (c) Loop until user enters "done"
        while (true) {

            System.out.print("Vehicle number: ");
            String number = sc.nextLine();

            // Stop condition
            if (number.equalsIgnoreCase("done")) {
                break;
            }

            System.out.print("Vehicle type (bike/car/truck): ");
            String type = sc.nextLine().toLowerCase();

            // Create Vehicle object using record
            Vehicle vehicle = new Vehicle(number, type);

            // Switch expression for toll
            int toll = switch (vehicle.type()) {
                case "bike" -> 20;
                case "car" -> 50;
                case "truck" -> 150;
                default -> 0;
            };

            // Add toll to total
            totalToll += toll;

            // Increment appropriate counter
            switch (vehicle.type()) {

                case "bike" -> bikeCount++;

                case "car" -> carCount++;

                case "truck" -> truckCount++;

                default -> System.out.println("Invalid vehicle type!");
            }
        }

        // (d) Find vehicle type with highest count
        String mostFrequent;

        if (bikeCount >= carCount && bikeCount >= truckCount) {
            mostFrequent = "bike";
        }
        else if (carCount >= bikeCount && carCount >= truckCount) {
            mostFrequent = "car";
        }
        else {
            mostFrequent = "truck";
        }

        // Print result
        System.out.println("\nTotal toll: " + totalToll);
        System.out.println("Most frequent: " + mostFrequent);

        sc.close();
    }
}